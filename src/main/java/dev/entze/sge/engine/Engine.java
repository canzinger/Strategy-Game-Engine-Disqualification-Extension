package dev.entze.sge.engine;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.game.Game;
import dev.entze.sge.game.GameASCIIVisualiser;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class Engine implements Runnable {


  private final Logger log;
  private final ExecutorService pool;

  private final Constructor<Game<?, ?>> gameConstructor;
  private final GameASCIIVisualiser<Game<?, ?>> gameBoardVisualiser;
  private final List<GameAgent<Game<?, ?>, ?>> gameAgents;

  public Engine(Logger log, ExecutorService pool,
      Constructor<Game<?, ?>> gameConstructor,
      GameASCIIVisualiser<Game<?, ?>> gameBoardVisualiser,
      List<GameAgent<Game<?, ?>, ?>> gameAgents) {
    this.log = log;
    this.pool = pool;
    this.gameConstructor = gameConstructor;
    this.gameBoardVisualiser = gameBoardVisualiser;
    this.gameAgents = gameAgents;
  }

  @Override
  public void run() {
    log.info("Starting Engine.");
  }


}
