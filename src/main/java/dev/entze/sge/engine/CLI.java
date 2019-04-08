package dev.entze.sge.engine;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.engine.loader.AgentLoader;
import dev.entze.sge.engine.loader.GameLoader;
import dev.entze.sge.game.Game;
import dev.entze.sge.game.GameASCIIVisualiser;
import dev.entze.sge.util.Pair;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarFile;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "Strategy Game Engine", description = "A sequential game engine.", version = "sge "
    + CLI.version, aliases = "sge")
public class CLI implements Callable<Void> {

  private Logger log;
  private ExecutorService pool;

  @Option(names = {"-h", "--help"}, usageHelp = true, description = "print this message")
  private boolean helpRequested = false;

  @Option(names = {"-V", "--version"}, versionHelp = true, description = "print the version")
  private boolean versionRequested = false;

  @Option(names = {"-L",
      "--log"}, arity = "1", description = "the log level (from -2 (=trace) to 2 (=error))")
  private int logLevel = 0;

  @Option(names = {"-g",
      "--game"}, required = true, arity = "1", description = "the JAR file of the game")
  private File gameFile;

  @Option(names = {"-a",
      "--agents"}, required = true, arity = "1..*", description = "JAR file(s) of the agents")
  private File[] agentFiles;

  @Option(names = {"-c",
      "--computation-time"}, defaultValue = "60", description = "Amount of computational time given for each turn.")
  private long computationTime = 60;

  @Option(names = {"-u",
      "--time-unit"}, defaultValue = "SECONDS", description = "Time unit in which -c is.")
  private TimeUnit timeUnit = TimeUnit.SECONDS;

  @Option(names = {"-i",
      "--interactive"}, description = "start engine in interactiveMode mode, mutually exclusive with -t")
  private boolean interactiveMode;

  @Option(names = {"-t",
      "--tournament"}, arity = "0..1", description = "start engine in tournament mode, mutually exclusive with -i")
  private TournamentMode tournamentMode = TournamentMode.SINGLE_ELEMINATION;

  public static final String version = "0.0.0";

  public static void main(String[] args) {
    CLI cli = new CLI();
    try {
      CommandLine.call(cli, args);
    } catch (Exception e) {
      cli.log.error_("");
      cli.log.error("Shutting down because of exception.");
      e.printStackTrace();
    }
    try {
      cli.cleanUpPool();
    } catch (InterruptedException e) {
      cli.log.error_("");
      cli.log.error("Interrupted.");
    }
  }

  @Override
  public Void call() {
    log = new Logger(logLevel, "[sge ", "",
        "trace]: ", System.out, "",
        "debug]: ", System.out, "",
        "info]: ", System.out, "",
        "warn]: ", System.err, "",
        "error]: ", System.err, "");

    log.info("Welcome to Strategy Game Engine " + version);
    log.info_("------------");
    log.debug("Got " + gameFile.getPath() + " as game file.");
    log.debug("Got " + agentFiles.length + " agents.");

    log.tra("Initialising ThreadPool");
    pool = Executors.newFixedThreadPool(
        (Runtime.getRuntime().availableProcessors() > 2 ? Runtime.getRuntime().availableProcessors()
            : 2));
    log.trace_(".done");

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

    return null;
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
        log.trace_("");
        log.info("ThreadPool did not yet shutdown. Forcing.");
        pool.shutdownNow();
      } else {
        log.tra_(".");
      }
    } catch (
        InterruptedException e) {
      log.trace_("");
      log.warn("ThreadPool termination was interrupted.");
      throw e;
    }
    log.trace_("done");
  }

}
