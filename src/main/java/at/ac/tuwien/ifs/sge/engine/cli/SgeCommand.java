package at.ac.tuwien.ifs.sge.engine.cli;

import at.ac.tuwien.ifs.sge.agent.GameAgent;
import at.ac.tuwien.ifs.sge.agent.HumanAgent;
import at.ac.tuwien.ifs.sge.engine.Logger;
import at.ac.tuwien.ifs.sge.engine.factory.AgentFactory;
import at.ac.tuwien.ifs.sge.engine.factory.GameFactory;
import at.ac.tuwien.ifs.sge.engine.loader.AgentLoader;
import at.ac.tuwien.ifs.sge.engine.loader.GameLoader;
import at.ac.tuwien.ifs.sge.game.Game;
import at.ac.tuwien.ifs.sge.util.Util;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
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
    //InteractiveCommand.class,
    TournamentCommand.class,
    //BatchCommand.class
})
public class SgeCommand implements Callable<Void> {

  private static final long AWAIT_TERMINATION_TIME = 5;
  private static final TimeUnit AWAIT_TERMINATION_TIMEUNIT = TimeUnit.SECONDS;

  private static final String SGE_TYPE = "Sge-Type";
  private static final String SGE_TYPE_GAME = "game";
  private static final String SGE_TYPE_AGENT = "agent";
  private static final String SGE_AGENT_CLASS = "Agent-Class";
  private static final String SGE_AGENT_NAME = "Agent-Name";
  private static final String SGE_GAME_CLASS = "Game-Class";
  static final String version = "1.0.2";

  Logger log;
  ExecutorService pool;
  List<AgentFactory> agentFactories = null;
  GameFactory<Game<Object, Object>> gameFactory = null;

  @Option(names = {"-h", "--help"}, usageHelp = true, description = "Prints this message.")
  boolean helpRequested = false;
  @Option(names = {"-V", "--version"}, versionHelp = true, description = "Prints the version.")
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

    if (args.length == 0) {
      cli.usage(System.err);
      System.exit(1);
    }

    if (args.length == 1 && !args[0].startsWith("-")) {
      cli.getSubcommands().getOrDefault(args[0], cli).usage(System.err);
      System.exit(1);
    }

    cli.setCaseInsensitiveEnumValuesAllowed(true);

    //AnsiConsole.systemInstall();
    try {
      List<Object> ran = cli.parseWithHandler(new RunAll(), args);
    } catch (Exception e) {
      sge.log.printStackTrace(e);
    }

    try {
      sge.cleanUpPool();
    } catch (InterruptedException e) {
      sge.log.error_();
      sge.log.error("Interrupted.");
    }
    //AnsiConsole.systemUninstall();

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

    int threads = Math.max(Runtime.getRuntime().availableProcessors(), 2);
    log.traf_("Initialising ThreadPool with %d threads", threads);
    pool = Executors.newFixedThreadPool(Math.max(Runtime.getRuntime().availableProcessors(), 2));
    log._trace(", done.");

    return null;
  }

  public int determineArguments(List<String> arguments, List<File> files, List<File> directories,
      List<String> agentConfiguration) {

    int argumentsSize = arguments.size();
    log.traProcess_("Interpreting arguments", 0, argumentsSize);

    int processed = 0;

    for (int i = 0; i < arguments.size(); i++) {
      log._tra_("\r");
      log.traProcess_("Interpreting arguments", i + 1, argumentsSize);
      String argument = arguments.get(i);
      if (argument.equalsIgnoreCase("Human")) {
        agentConfiguration.add(argument);
      } else {
        File file = new File(argument);
        if (file.exists()) {
          if (file.isDirectory()) {
            directories.add(file);
          } else if (file.isFile()) {
            files.add(file);
          } else {
            log._trace(", failed.");
            log.warn(argument + " seems to be malformed");
            processed--;
          }
        } else {
          agentConfiguration.add(argument);
        }
      }
      processed++;
    }

    log._trace(", done.");
    return processed;
  }

  public int processDirectories(List<File> files, List<File> directories) {
    log.traEnum_("Enumerating directories", 0);
    if (directories.size() <= 0) {
      log._trace(", done.");
      return 0;
    }

    int filesSize = files.size();

    for (int i = 0; i < directories.size(); i++) {
      log._tra_("\r");
      log.traEnum_("Enumerating directories", i + 1);

      File directory = directories.get(i);
      File[] subFiles;
      if (directory != null && (subFiles = directory.listFiles()) != null) {
        for (File file : subFiles) {
          if (file.exists() && file.isDirectory()) {
            directories.add(file);
          }
        }
      }
    }

    log._trace(", done.");
    int directoriesSize = directories.size();
    log.traProcess_("Processing directories", 0, directoriesSize);

    for (int i = 0; i < directoriesSize; i++) {
      File directory = directories.get(i);

      log._tra_("\r");
      log.traProcess_("Processing directories", i + 1, directoriesSize);

      File[] subFiles;
      if (directory != null && (subFiles = directory.listFiles()) != null) {
        for (File file : subFiles) {
          if (file.exists() && file.isFile()) {
            files.add(file);
          }
        }
      }
    }

    log._trace(", done.");

    return files.size() - filesSize;
  }

  public void loadFiles(List<File> files) {

    int filesSize = files.size();
    log.traProcess_("Processing files", 0, filesSize);

    List<URL> urlList = new ArrayList<>(files.size());
    String gameClassName = null;
    List<AgentLoader> agentLoaders = new ArrayList<>(filesSize - 1);
    List<String> agentClassNames = new ArrayList<>(filesSize - 1);
    List<String> agentNames = new ArrayList<>(filesSize - 1);

    for (int i = 0; i < files.size(); i++) {
      File file = files.get(i);

      log._tra_("\r");
      log.traProcess_("Processing files", i + 1, filesSize);

      JarFile jarFile;
      try {
        jarFile = new JarFile(file);
      } catch (IOException e) {
        log._trace(", failed.");
        log.warn("Could not interpret " + file.getPath() + " as jar.");
        continue;
      }

      Attributes attributes;
      try {
        attributes = jarFile.getManifest().getMainAttributes();
      } catch (IOException e) {
        log._trace(", failed.");
        log.warn("Could not access " + file.getPath() + "'s manifest.");
        continue;
      }

      String type = attributes.getValue(SGE_TYPE);

      if (type == null) {
        log._trace(", failed.");
        log.warn("Could not determine whether " + file.getPath()
            + " is a game or an agent. Is \"" + SGE_TYPE + "\" set in Main-Attributes?");
        continue;
      }

      try {
        urlList.add(file.toURI().toURL());
      } catch (MalformedURLException e) {
        log._trace(", failed.");
        log.warn("Could not get URL of " + file.getPath() + ".");
        continue;
      }

      if (SGE_TYPE_AGENT.equalsIgnoreCase(type)) {
        String agentClass = attributes.getValue(SGE_AGENT_CLASS);
        if (agentClass == null) {
          log._trace(", failed.");
          log.warn(
              "Agent: " + file.getPath() + " could not determine class path. Is \""
                  + SGE_AGENT_CLASS
                  + "\" set in Main-Attributes?");
        }

        String agentName = attributes.getValue(SGE_AGENT_NAME);
        if (agentName == null) {
          log._trace(", failed.");
          log.warn(
              "Agent: " + file.getPath() + " could not determine name. Is \"" + SGE_AGENT_NAME
                  + "\" set in Main-Attributes?");
        }

        if (agentClass == null || agentName == null) {
          continue;
        }

        agentClassNames.add(agentClass);
        agentNames.add(agentName);
      } else if (SGE_TYPE_GAME.equalsIgnoreCase(type)) {

        String gameClass = attributes.getValue(SGE_GAME_CLASS);
        if (gameClass == null) {
          log._trace(", failed.");
          log.warn(
              "Game: " + file.getPath() + " could not determine class path. Is \"" + SGE_GAME_CLASS
                  + "\" set in Main-Attributes?");
          continue;
        }

        gameClassName = gameClass;

      } else {
        log._trace();
        log.warn("Unknown type in " + file.getPath() + ". Has to be either \"" + SGE_TYPE_GAME
            + "\" or \"" + SGE_TYPE_AGENT + "\".");
      }

    }

    log._trace(", done.");

    if (gameClassName == null) {
      log.error("No game found.");
      throw new IllegalArgumentException("No game was specified, or could be loaded.");
    }

    URLClassLoader classLoader = URLClassLoader.newInstance(urlList.toArray(new URL[0]));

    GameLoader<Game<Object, Object>> gameLoader = new GameLoader<>(gameClassName,
        classLoader, log);
    for (int i = 0; i < agentClassNames.size(); i++) {
      agentLoaders
          .add(new AgentLoader(agentNames.get(i), agentClassNames.get(i), classLoader, log));
    }

    filesSize = agentNames.size() + 1;

    log.traProcess_("Loading files", 1, filesSize);

    try {
      gameFactory = gameLoader.call();
    } catch (ClassNotFoundException e) {
      log._trace(", failed.");
      log.error("Could not find class of game.");
      log.printStackTrace(e);
      throw new IllegalStateException("Could not load files correctly.");
    } catch (NoSuchMethodException e) {
      log._trace(", failed.");
      log.error("Could not find required constructors of game.");
      log.printStackTrace(e);
      throw new IllegalStateException("Could not load files correctly.");
    } catch (IllegalAccessException e) {
      log._trace(", failed.");
      log.error("Not allowed to access constructors of game.");
      log.printStackTrace(e);
      throw new IllegalStateException("Could not load files correctly.");
    } catch (InvocationTargetException e) {
      log._trace(", failed.");
      log.error("Error while invoking constructors of game.");
      log.printStackTrace(e);
      throw new IllegalStateException("Could not load files correctly.");
    } catch (InstantiationException e) {
      log._trace(", failed.");
      log.error("Error while instantiating game.");
      log.printStackTrace(e);
      throw new IllegalStateException("Could not load files correctly.");
    }

    agentFactories = new ArrayList<>(agentNames.size());

    for (int i = 0; i < agentLoaders.size(); i++) {
      try {
        log._tra_("\r");
        log.traProcess_("Loading files", i + 2, filesSize);
        agentFactories.add(agentLoaders.get(i).call());
      } catch (ClassNotFoundException e) {
        log._trace(", failed.");
        log.error("Could not find class of agent " + agentNames.get(i));
        log.printStackTrace(e);
        throw new IllegalStateException("Could not load files correctly.");
      } catch (NoSuchMethodException e) {
        log._trace(", failed.");
        log.error("Could not load constructor of agent " + agentNames.get(i));
        log.printStackTrace(e);
        throw new IllegalStateException("Could not load files correctly.");
      }
    }

    log._trace(", done.");

    assert gameFactory != null;
    assert agentFactories != null;

  }

  /**
   * Fills the agent configuration until it has the minimum amount of players
   *
   * @param agentConfiguration agent configuration
   * @param minimumPlayers minimum number of players required
   * @return number of agents added to the configuration
   */
  public int fillAgentList(List<String> agentConfiguration, int minimumPlayers) {
    int added = 0;
    List<String> agentConfigurationLowercase = agentConfiguration.stream()
        .map(String::toLowerCase)
        .collect(Collectors.toList());

    log.traProcess_("Adding agents", agentConfiguration.size(), minimumPlayers);

    for (int i = 0; i < agentFactories.size() && agentConfiguration.size() < minimumPlayers;
        i++) {
      String agentName = agentFactories.get(i).getAgentName();
      if (!agentConfigurationLowercase.contains(agentName.toLowerCase())) {
        agentConfigurationLowercase.add(agentName.toLowerCase());
        agentConfiguration.add(agentName);
        log._tra_("\r");
        log.traProcess_("Adding agents", agentConfiguration.size(), minimumPlayers);
        added++;
      }
    }

    while (agentConfiguration.size() < minimumPlayers) {
      agentConfiguration.add("Human");
      log._tra_("\r");
      log.traProcess_("Adding agents", agentConfiguration.size(), minimumPlayers);
      added++;
    }

    log._trace(", done.");

    return added;
  }

  /**
   * Fills the agent configuration until every agent in the agentFactories is present.
   *
   * @param agentConfiguration agent configuration
   * @return number of agents added to the configuration
   */
  public int fillAgentList(List<String> agentConfiguration) {
    int added = 0;
    List<String> agentConfigurationLowercase = agentConfiguration.stream()
        .map(String::toLowerCase)
        .collect(Collectors.toList());

    log.traProcess_("Processing agents", 0, agentFactories.size());
    for (int a = 0; a < agentFactories.size(); a++) {
      log.tra_("\r");
      log.traProcess_("Processing agents", a + 1, agentFactories.size());
      String agentName = agentFactories.get(a).getAgentName();
      if (!agentConfigurationLowercase.contains(agentName.toLowerCase())) {
        agentConfigurationLowercase.add(agentName.toLowerCase());
        agentConfiguration.add(agentName);
        added++;
      }
    }
    log._trace(", done.");

    return added;
  }

  public List<GameAgent<Game<Object, Object>, Object>> createAgentListFromConfiguration(
      List<String> configuration) {

    List<GameAgent<Game<Object, Object>, Object>> agentList = new ArrayList<>(configuration.size());
    boolean everyPlayerMatches = true;

    for (String player : configuration) {
      boolean playerMatches = false;
      if (player.equalsIgnoreCase("Human")) {
        agentList.add(new HumanAgent<>());
        playerMatches = true;
      }
      for (AgentFactory agentFactory : agentFactories) {
        if (!playerMatches) {
          if (agentFactory.getAgentName().equalsIgnoreCase(player)) {
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
      throw new IllegalArgumentException("Could not create a valid agent list");
    }

    return agentList;

  }

  public String interpretBoardString(String board) {
    if (board == null || board.isEmpty()) {
      return null;
    }

    if (!board.contains("\n")) {
      File file = new File(board);
      if (file.exists() && file.isFile()) {
        try {
          board = readString(file);
        } catch (IOException e) {
          log.warn("Could not read board from file. Using null instead");
          board = null;
        }
      }
    }
    return board;
  }

  private void cleanUpPool() throws InterruptedException {
    if (pool == null) {
      return;
    }
    log.tra_("Shutting down ThreadPool");
    pool.shutdown();
    log._trace(", done.");
    log.tra_("Waiting " + Util
        .convertUnitToReadableString(AWAIT_TERMINATION_TIME, AWAIT_TERMINATION_TIMEUNIT)
        + " for termination");
    long startTime = System.nanoTime();
    try {
      if (!pool.awaitTermination(AWAIT_TERMINATION_TIME, AWAIT_TERMINATION_TIMEUNIT)) {
        log._trace(", failed.");
        log.info("ThreadPool did not yet shutdown. Forcing.");
        List<Runnable> stillRunning = pool.shutdownNow();
      } else {
        long endTime = System.nanoTime();
        log._trace(", done in " + Util
            .convertUnitToMinimalString(endTime - startTime, TimeUnit.NANOSECONDS) + ".");
      }
    } catch (
        InterruptedException e) {
      log._trace(", failed.");
      log.warn("ThreadPool termination was interrupted.");
      throw e;
    }
  }


  static String readString(Path path) throws IOException {
    return Files.readString(path);
  }

  static String readString(File file) throws IOException {
    return readString(file.toPath());
  }

}
