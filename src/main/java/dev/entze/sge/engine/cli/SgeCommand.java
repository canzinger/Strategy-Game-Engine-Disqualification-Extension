package dev.entze.sge.engine.cli;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.agent.HumanAgent;
import dev.entze.sge.engine.Logger;
import dev.entze.sge.engine.factory.AgentFactory;
import dev.entze.sge.engine.factory.GameFactory;
import dev.entze.sge.engine.loader.AgentLoader;
import dev.entze.sge.engine.loader.GameLoader;
import dev.entze.sge.game.Game;
import dev.entze.sge.game.GameASCIIVisualiser;
import dev.entze.sge.util.pair.ImmutablePair;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.RunAll;


@Command(name = "sge", description = "A sequential game engine.", version = "sge "
    + SgeCommand.version, subcommands = {
    MatchCommand.class,
    InteractiveCommand.class,
    TournamentCommand.class,
    BatchCommand.class
})
public class SgeCommand implements Callable<Void> {

  public static final String SGE_TYPE = "Sge-Type";
  public static final String SGE_TYPE_GAME = "game";
  public static final String SGE_TYPE_AGENT = "agent";
  public static final String SGE_AGENT_CLASS = "Agent-Class";
  public static final String SGE_AGENT_NAME = "Agent-Name";
  public static final String SGE_GAME_CLASS = "Game-Class";
  public static final String SGE_GAMEASCIIVISUALISER_CLASS = "GameASCIIVisualiser-Class";
  public static final String version = "0.0.0";
  Logger log;
  ExecutorService pool;
  List<AgentFactory> agentFactories = null;
  GameFactory gameFactory = null;
  GameASCIIVisualiser<Game<Object, Object>> gameASCIIVisualiser = null;
  @Option(names = {"-h", "--help"}, usageHelp = true, description = "print this message")
  boolean helpRequested = false;
  @Option(names = {"-V", "--version"}, versionHelp = true, description = "print the version")
  boolean versionRequested = false;
  @Option(names = {"-q",
      "--quiet"}, description = "Found once: Log only warnings. Twice: only errors. Thrice: no output.")
  boolean[] quiet = new boolean[0];
  @Option(names = {"-v",
      "--verbose"}, description = "Found once: Log debug information. Twice: with trace information.")
  boolean[] verbose = new boolean[0];

  @Option(names = "--debug", description = "Starts engine in debug mode. No timeouts and verbose is turned on once.")
  boolean debug = false;

  public static void main(String[] args) {

    SgeCommand sge = new SgeCommand();
    CommandLine cli = new CommandLine(sge);

    cli.setCaseInsensitiveEnumValuesAllowed(true);

    List<Object> ran = cli.parseWithHandler(new RunAll(), args);

    try {
      sge.cleanUpPool();
    } catch (InterruptedException e) {
      sge.log.error_();
      sge.log.error("Interrupted.");
    }

  }

  @Override
  public Void call() {
    int logLevel = quiet.length - (verbose.length + (debug ? 1 : 0));
    log = new Logger(logLevel, "[sge ", "",
        "trace]: ", System.out, "",
        "debug]: ", System.out, "",
        "info]: ", System.out, "",
        "warn]: ", System.err, "",
        "error]: ", System.err, "");

    log.tra("Initialising ThreadPool");
    pool = Executors.newFixedThreadPool(Math.max(Runtime.getRuntime().availableProcessors(), 2));
    log.trace_(".done");

    return null;
  }

  public int determineArguments(List<String> arguments, List<File> files, List<File> directories,
      List<String> agentConfiguration) {

    log.tra("Interpreting " + arguments.size() + " arguments.");

    int processed = 0;

    for (String argument : arguments) {
      if (argument.toLowerCase().equals("human")) {
        agentConfiguration.add(argument);
      } else {
        File file = new File(argument);
        if (file.exists()) {
          if (file.isDirectory()) {
            directories.add(file);
          } else if (file.isFile()) {
            files.add(file);
          } else {
            log.warn(argument + " seems to be malformed");
            processed--;
          }
        } else {
          agentConfiguration.add(argument);
        }
      }
      processed++;
      log.tra_(".");
    }

    log.trace_("done");
    return processed;
  }

  public int loadDirectories(List<File> files, List<File> directories) {
    if (directories.size() <= 0) {
      return 0;
    }
    List<File> subdirectories = new ArrayList<>();
    int loaded = 0;
    log.tra("Loading " + directories.size() + " directories");
    for (File directory : directories) {
      File[] subFiles;
      if (directory != null && (subFiles = directory.listFiles()) != null) {
        for (File file : subFiles) {
          if (file.exists()) {
            if (file.isDirectory()) {
              subdirectories.add(file);
            } else if (file.isFile()) {
              files.add(file);
              loaded++;
            }
          }
        }
      }
      log.tra_(".");
    }
    log.trace_("done");

    if (subdirectories.size() > 0) {
      loaded += loadDirectories(files, subdirectories);
    }

    return loaded;
  }

  public void loadFiles(List<File> files) {

    log.tra("Processing " + files.size() + " files");

    List<URL> urlList = new ArrayList<>(files.size());
    String gameClassName = null;
    String gameASCIIVisualiserClassName = null;
    List<AgentLoader> agentLoaders = new ArrayList<>(files.size() - 1);
    List<String> agentClassNames = new ArrayList<>(files.size() - 1);
    List<String> agentNames = new ArrayList<>(files.size() - 1);

    for (File file : files) {

      JarFile jarFile;
      try {
        jarFile = new JarFile(file);
      } catch (IOException e) {
        log.trace_();
        log.warn("Could not interpret " + file.getPath() + " as jar.");
        continue;
      }

      Attributes attributes;
      try {
        attributes = jarFile.getManifest().getMainAttributes();
      } catch (IOException e) {
        log.trace_();
        log.warn("Could not access " + file.getPath() + "'s manifest.");
        continue;
      }

      String type = attributes.getValue(SGE_TYPE);

      if (type == null) {
        log.trace_();
        log.warn("Could not determine whether " + file.getPath()
            + " is a game or an agent. Is \"" + SGE_TYPE + "\" set in Main-Attributes?");
        continue;
      }

      type = type.toLowerCase();

      try {
        urlList.add(file.toURI().toURL());
      } catch (MalformedURLException e) {
        log.trace_();
        log.warn("Could not get URL of " + file.getPath() + ".");
        continue;
      }

      if (SGE_TYPE_AGENT.toLowerCase().equals(type)) {
        String agentClass = attributes.getValue(SGE_AGENT_CLASS);
        if (agentClass == null) {
          log.trace_();
          log.warn(
              "Agent: " + file.getPath() + " could not determine class path. Is \""
                  + SGE_AGENT_CLASS
                  + "\" set?");
        }

        String agentName = attributes.getValue(SGE_AGENT_NAME);
        if (agentName == null) {
          log.trace_();
          log.warn(
              "Agent: " + file.getPath() + " could not determine name. Is \"" + SGE_AGENT_NAME
                  + "\" set?");
        }

        if (agentClass == null || agentName == null) {
          continue;
        }

        agentClassNames.add(agentClass);
        agentNames.add(agentName);
      } else if (SGE_TYPE_GAME.toLowerCase().equals(type)) {

        String gameClass = attributes.getValue(SGE_GAME_CLASS);
        if (gameClass == null) {
          log.trace_();
          log.warn(
              "Game: " + file.getPath() + " could not determine class path. Is \"" + SGE_GAME_CLASS
                  + "\" set?");
        }

        String gameASCIIVisualiserClass = attributes.getValue(SGE_GAMEASCIIVISUALISER_CLASS);
        if (gameASCIIVisualiserClass == null) {
          log.trace_();
          log.warn(
              "Game: " + file.getPath() + " could not determine class path of visualiser. Is \""
                  + SGE_GAMEASCIIVISUALISER_CLASS + "\" set?");
        }

        if (gameClass == null || gameASCIIVisualiserClass == null) {
          continue;
        }

        gameClassName = gameClass;
        gameASCIIVisualiserClassName = gameASCIIVisualiserClass;

      } else {
        log.trace_();
        log.warn("Unknown type in " + file.getPath() + ". Has to be either \"" + SGE_TYPE_GAME
            + "\" or \"" + SGE_TYPE_AGENT + "\".");
        continue;
      }

      log.tra_(".");
    }

    if (gameClassName == null || gameASCIIVisualiserClassName == null) {
      log.error("No game found.");
      throw new IllegalArgumentException("No game was specified, or could be loaded.");
    }

    log.trace_("done");

    URLClassLoader classLoader = URLClassLoader.newInstance(urlList.toArray(new URL[0]));

    GameLoader gameLoader = new GameLoader(gameClassName, gameASCIIVisualiserClassName,
        classLoader, log);
    for (int i = 0; i < agentClassNames.size(); i++) {
      agentLoaders
          .add(new AgentLoader(agentNames.get(i), agentClassNames.get(i), classLoader, log));
    }

    log.tra("Loading " + files.size() + " files");

    try {
      ImmutablePair<GameFactory, GameASCIIVisualiser<Game<Object, Object>>> loadedGame = gameLoader
          .call();
      gameFactory = loadedGame.getA();
      gameASCIIVisualiser = loadedGame.getB();
    } catch (ClassNotFoundException e) {
      log.trace_();
      log.error("Could not find class of game.");
      e.printStackTrace();
      throw new IllegalStateException("Could not load files correctly.");
    } catch (NoSuchMethodException e) {
      log.trace_();
      log.error("Could not instantiate GameASCIIVisualiser.");
      e.printStackTrace();
      throw new IllegalStateException("Could not load files correctly.");
    } catch (IllegalAccessException e) {
      log.trace_();
      log.error("Not allowed to access constructor(s) of game.");
      e.printStackTrace();
      throw new IllegalStateException("Could not load files correctly.");
    } catch (InvocationTargetException e) {
      log.trace_();
      log.error("Error while invoking constructor(s) of game.");
      e.printStackTrace();
      throw new IllegalStateException("Could not load files correctly.");
    } catch (InstantiationException e) {
      log.trace_();
      log.error("Error while instantiating game.");
      e.printStackTrace();
      throw new IllegalStateException("Could not load files correctly.");
    }

    log.tra_(".");

    agentFactories = new ArrayList<>(agentNames.size());

    for (int i = 0; i < agentLoaders.size(); i++) {
      try {
        agentFactories.add(agentLoaders.get(i).call());
        log.tra_(".");
      } catch (ClassNotFoundException e) {
        log.trace_();
        log.error("Could not find class of agent " + agentNames.get(i));
        e.printStackTrace();
        throw new IllegalStateException("Could not load files correctly.");
      } catch (NoSuchMethodException e) {
        log.trace_();
        log.error("Could not load constructor of agent " + agentNames.get(i));
        e.printStackTrace();
        throw new IllegalStateException("Could not load files correctly.");
      }
    }

    log.trace_("done");

    assert gameFactory != null;
    assert gameASCIIVisualiser != null;
    assert agentFactories != null;

  }

  public int fillAgentList(List<String> agentConfiguration, int numberOfPlayers) {
    int added = 0;
    List<String> agentConfigurationLowercase = agentConfiguration.stream()
        .map(String::toLowerCase)
        .collect(Collectors.toList());

    for (int i = 0; i < agentFactories.size() && agentConfiguration.size() < numberOfPlayers;
        i++) {
      String agentName = agentFactories.get(i).getAgentName();
      if (!agentConfigurationLowercase.contains(agentName.toLowerCase())) {
        agentConfigurationLowercase.add(agentName.toLowerCase());
        agentConfiguration.add(agentName);
        added++;
      }
    }

    while (agentConfiguration.size() < numberOfPlayers) {
      agentConfiguration.add("Human");
      added++;
    }

    return added;
  }

  public List<GameAgent<Game<Object, Object>, Object>> createAgentListFromConfiguration(
      int numberOfPlayers,
      List<String> configuration) {
    if (numberOfPlayers != configuration.size() || !(
        gameFactory.getMinimumNumberOfPlayers() <= numberOfPlayers && numberOfPlayers <= gameFactory
            .getMaximumNumberOfPlayers())) {
      return null;
    }

    List<GameAgent<Game<Object, Object>, Object>> agentList = new ArrayList<>(numberOfPlayers);
    boolean everyPlayerMatches = true;

    for (String player : configuration) {
      boolean playerMatches = false;
      if (player.toLowerCase().equals("human")) {
        agentList.add(new HumanAgent<>());
        playerMatches = true;
      }
      for (AgentFactory agentFactory : agentFactories) {
        if (!playerMatches) {
          if (agentFactory.getAgentName().toLowerCase().equals(player.toLowerCase())) {
            agentList.add(agentFactory.newInstance());
            playerMatches = true;
          }
        }
      }

      if (!playerMatches) {
        log.warn("Could not find an agentFactory for " + player);
      }

      everyPlayerMatches = everyPlayerMatches && playerMatches;
    }

    if (!everyPlayerMatches) {
      return null;
    }

    return agentList;

  }

  private void cleanUpPool() throws InterruptedException {
    if (pool == null) {
      return;
    }
    log.tra("Shutting down ThreadPool");
    pool.shutdown();
    log.tra_(".");
    try {
      if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
        log.trace_();
        log.info("ThreadPool did not yet shutdown. Forcing.");
        pool.shutdownNow();
      } else {
        log.tra_(".");
      }
    } catch (
        InterruptedException e) {
      log.warn_();
      log.warn("ThreadPool termination was interrupted.");
      throw e;
    }
    log.trace_("done");
  }


}
