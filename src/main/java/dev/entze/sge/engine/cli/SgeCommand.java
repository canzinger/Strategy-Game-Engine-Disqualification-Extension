package dev.entze.sge.engine.cli;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.engine.Logger;
import dev.entze.sge.engine.loader.AgentLoader;
import dev.entze.sge.engine.loader.GameLoader;
import dev.entze.sge.game.Game;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
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
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.RunAll;


@Command(name = "Strategy Game Engine", description = "A sequential game engine.", version = "sge "
    + SgeCommand.version, aliases = "sge", subcommands = {
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

  Logger log;
  ExecutorService pool;
  List<Constructor<GameAgent<Game<?, ?>, ?>>> agentFactories;
  Constructor<Game<?, ?>> gameFactory;

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

  public static final String version = "0.0.0";

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

    /*
    try {
      CommandLine.call(cli, args);
    } catch (Exception e) {
      //cli.log.error_("");
      //cli.log.error("Shutting down because of exception.");
      e.printStackTrace();
    }

    */
  }

  @Override
  public Void call() {
    int logLevel = quiet.length - verbose.length;
    log = new Logger(logLevel, "[sge ", "",
        "trace]: ", System.out, "",
        "debug]: ", System.out, "",
        "info]: ", System.out, "",
        "warn]: ", System.err, "",
        "error]: ", System.err, "");

    log.tra("Initialising ThreadPool");
    pool = Executors.newFixedThreadPool(Math.max(Runtime.getRuntime().availableProcessors(), 2));
    log.trace_(".done");

    /*
    log.info("Welcome to Strategy Game Engine " + version);
    log.info_("------------");
    log.debug("Got " + gameFile.getPath() + " as game file.");
    log.debug("Got " + agentFiles.length + " agents.");


    log.tra("Initialising Loaders");

    JarFile gameJar = null;
    JarFile[] agentJars = new JarFile[agentFiles.length];

    try {
      gameJar = new JarFile(gameFile);
      log.tra_(".");
    } catch (IOException e) {
      log.trace_("");
      log.error("Game file: " + gameFile.getPath() + " could not be read as a JarFile.");
    }

    for (int i = 0; i < agentFiles.length; i++) {
      try {
        agentJars[i] = new JarFile(agentFiles[i]);
        log.tra_(".");
      } catch (IOException e) {
        log.trace_("");
        log.error("Agent file: " + agentFiles[i].getPath() + " could not be read as a JarFile.");
      }
    }

    URL[] urls = new URL[1 + agentFiles.length];
    try {
      urls[0] = gameFile.toURI().toURL();
      log.tra_(".");
    } catch (MalformedURLException e) {
      log.trace_("");
      log.error("Game file: " + gameFile.getPath() + " could not be loaded.");
    }
    for (int i = 1; i < urls.length; i++) {
      try {
        urls[i] = agentFiles[i - 1].toURI().toURL();
        log.tra_(".");
      } catch (MalformedURLException e) {
        log.trace_("");
        log.error("Agent file: " + agentFiles[i].getPath() + " could not be loaded.");
      }
    }
    URLClassLoader classLoader = URLClassLoader.newInstance(urls);
    log.tra_(".");

    GameLoader gameLoader = null;
    try {
      gameLoader = new GameLoader(gameJar, classLoader);
      log.tra_(".");
    } catch (IOException e) {
      log.trace_("");
      log.error("Game file: " + gameFile.getPath() + " could not read manifest.");
    }
    log.tra_(".");
    AgentLoader[] agentLoaders = new AgentLoader[agentFiles.length];
    for (int i = 0; i < agentFiles.length; i++) {
      try {
        agentLoaders[i] = new AgentLoader(agentJars[i], classLoader);
        log.tra_(".");
      } catch (IOException e) {
        log.trace_("");
        log.error("Agent file: " + agentFiles[i].getPath() + " could not read manifest.");
      }
    }
    log.trace_("done");

    log.trace("Loading game");
    Future<Pair<Constructor<Game<?, ?>>, GameASCIIVisualiser<Game<?, ?>>>> gameFuture = pool
        .submit(gameLoader);
    List<Future<GameAgent<Game<?, ?>, ?>>> gameAgentFutures = new ArrayList<>(agentLoaders.length);

    log.trace("Loading agents");
    for (AgentLoader agentLoader : agentLoaders) {
      gameAgentFutures.add(pool.submit(agentLoader));
    }

    Constructor<Game<?, ?>> gameConstructor = null;
    GameASCIIVisualiser<Game<?, ?>> gameASCIIVisualiser = null;
    try {
      gameConstructor = gameFuture.get().getA();
      gameASCIIVisualiser = gameFuture.get().getB();
    } catch (InterruptedException e) {
      log.trace_("");
      log.warn("Interrupted.");
    } catch (ExecutionException e) {
      log.trace_("");
      log.warn("Exception while loading game.");
    }

    log.trace("Loaded game.");

    List<GameAgent<Game<?, ?>, ?>> gameAgents = new ArrayList<>(agentLoaders.length);

    for (
        Future<GameAgent<Game<?, ?>, ?>> gameAgentFuture : gameAgentFutures) {
      try {
        gameAgents.add(gameAgentFuture.get());
      } catch (InterruptedException e) {
        log.trace_("");
        log.warn("Interrupted.");
      } catch (ExecutionException e) {
        log.trace_("");
        log.warn("Exception while loading agent.");
      }
    }

    log.trace("Loaded agents.");

    log.tra("Initialising Engine");
    Engine engine = new Engine(log, pool, gameConstructor, gameASCIIVisualiser, gameAgents,
        computationTime, timeUnit, interactiveMode, tournamentMode);
    log.trace_(".done");

    engine.run();

    */
    return null;
  }

  public void loadFiles(List<File> files) {

    log.tra("Processing " + files.size() + " files");

    URLClassLoader classLoader = null;
    List<URL> urlList = new ArrayList<>(files.size());
    GameLoader gameLoader = null;
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

      if (!attributes.containsKey(SGE_TYPE)) {
        log.trace_();
        log.warn("Could not determine whether " + file.getPath()
            + " is a game or an agent. Is " + SGE_TYPE + " set in Main-Attributes?");
        continue;
      }

      try {
        urlList.add(file.toURI().toURL());
      } catch (MalformedURLException e) {
        log.trace_();
        log.warn("Could not get URL of " + file.getPath() + ".");
        continue;
      }

      String type = attributes.getValue(SGE_TYPE).toLowerCase();

      if (SGE_TYPE_AGENT.toLowerCase().equals(type)) {
        boolean error = false;
        if (!attributes.containsKey(SGE_AGENT_CLASS)) {
          log.trace_();
          log.warn(
              "Agent: " + file.getPath() + " could not determine class path. Is " + SGE_AGENT_CLASS
                  + " set?");
          error = true;
        }

        if (!attributes.containsKey(SGE_AGENT_NAME)) {
          log.trace_();
          log.warn(
              "Agent: " + file.getPath() + " could not determine name. Is " + SGE_AGENT_NAME
                  + " set?");
          error = true;
        }

        if (error) {
          continue;
        }

        agentClassNames.add(attributes.getValue(SGE_AGENT_CLASS));
        agentNames.add(attributes.getValue(SGE_AGENT_NAME));
      } else if (SGE_TYPE_GAME.toLowerCase().equals(type)) {

        //TODO: Load Game-Class, GameASCIIVisualiser-Class

      } else {
        log.trace_();
        log.warn("Unknown type in " + file.getPath() + ". Has to be either \"" + SGE_TYPE_GAME
            + "\" or \"" + SGE_TYPE_AGENT + "\".");
        continue;
      }

      log.tra_(".");
    }

    if (gameClassName == null) {
      //TODO: no game added
    }

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
