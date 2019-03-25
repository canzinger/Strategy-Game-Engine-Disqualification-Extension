package dev.entze.sge.engine;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.game.Game;
import dev.entze.sge.game.GameBoardTranslator;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class Engine implements Runnable {

  private final Logger log;
  private final ExecutorService pool;

  private final Game game;
  private final GameBoardTranslator<String> gameBoardVisualiser;
  private final List<GameAgent<Game>> gameAgents;

  public Engine(Logger log, ExecutorService pool, Game game,
      GameBoardTranslator<String> gameBoardVisualiser,
      List<GameAgent<Game>> gameAgents) {
    this.log = log;
    this.pool = pool;
    this.game = game;
    this.gameBoardVisualiser = gameBoardVisualiser;
    this.gameAgents = gameAgents;
  }


  @Override
  public void run() {
    log.info("Starting Engine.");
  }

}
