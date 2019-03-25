package dev.entze.sge.engine;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.engine.loader.AgentLoader;
import dev.entze.sge.engine.loader.GameLoader;
import dev.entze.sge.game.Game;
import dev.entze.sge.game.GameBoardTranslator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "Strategy Game Engine", description = "A sequential game engine.", version = "sge "
    + CLI.version, aliases = "sge")
public class CLI implements Callable<Void> {

  @Option(names = {"-h", "--help"}, usageHelp = true, description = "print this message")
  private boolean helpRequested = false;

  @Option(names = {"-V", "--version"}, versionHelp = true, description = "print the version")
  private boolean versionRequested = false;

  @Option(names = {"-L",
      "--log"}, arity = "1", description = "the log level (from -2 (=trace) to 2 (=error))")
  private int logLevel = 0;

  @Option(names = {"-g",
      "--game"}, required = true, arity = "1", description = "the JAR file of the game")
  private File gameJar;

  @Option(names = {"-a",
      "--agents"}, required = true, arity = "1..*", description = "JAR file(s) of the agents")
  private File[] agentJars;

  public static final String version = "0.0.0";

  public static void main(String[] args) {
    CommandLine.call(new CLI(), args);
  }

  @Override
  public Void call() throws IOException, InterruptedException {
    Logger log = new Logger(logLevel, "[sge ", "",
        "trace]: ", System.out, "",
        "debug]: ", System.out, "",
        "info]: ", System.out, "",
        "warn]: ", System.err, "",
        "error]: ", System.err, "");

    log.info("Welcome to Strategy Game Engine " + version);
    log.info_("------------");
    log.debug("Got " + gameJar.getPath() + " as game file.");
    log.debug("Got " + agentJars.length + " agents.");

    log.tra("Initialising ThreadPool");
    ExecutorService pool = Executors.newFixedThreadPool(
        (Runtime.getRuntime().availableProcessors() > 2 ? Runtime.getRuntime().availableProcessors()
            : 2));
    log.trace_(".done");

    log.tra("Initialising Loaders");
    GameLoader gameLoader;
    try {
      gameLoader = new GameLoader(gameJar);
    } catch (IOException e) {
      log.trace_("");
      log.error("Could not load game jar");
      throw e;
    }
    log.tra_(".");
    AgentLoader[] agentLoaders = new AgentLoader[agentJars.length];
    for (int i = 0; i < agentJars.length; i++) {
      try {
        agentLoaders[i] = new AgentLoader(agentJars[i]);
      } catch (IOException e) {
        log.trace_("");
        log.error("Could not load agent jar \"" + agentJars[i].getPath() + "\"");
        throw e;
      }
      log.tra_(".");
    }
    log.trace_("done");

    log.tra("Loading game");
    Game game;
    try {
      game = gameLoader.loadGame();
    } catch (IOException e) {
      log.trace_("");
      log.error("Could not load game.");
      throw e;
    }
    log.tra_(".");

    GameBoardTranslator<String> gameBoardVisualiser;

    gameBoardVisualiser = gameLoader.loadGameBoardVisualiser();

    log.trace_(".done");

    log.tra("Loading agents");
    List<GameAgent<Game>> gameAgents = new ArrayList<>(agentLoaders.length);
    for (AgentLoader agentLoader : agentLoaders) {
      gameAgents.add(agentLoader.loadGameAgent());
      log.tra_(".");
    }
    log.trace_("done");

    log.tra("Initialising Engine");
    Engine engine = new Engine(log, pool, game, gameBoardVisualiser, gameAgents);
    log.trace_(".done");

    engine.run();

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
    } catch (InterruptedException e) {
      log.trace_("");
      log.warn("ThreadPool termination was interrupted.");
      throw e;
    }
    log.trace_("done");

    return null;
  }
}
