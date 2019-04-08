package dev.entze.sge.engine;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.game.Game;
import dev.entze.sge.game.GameASCIIVisualiser;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class Engine implements Runnable {


  private final Logger log;
  private final ExecutorService pool;

  private final Constructor<Game<?, ?>> gameConstructor;
  private final GameASCIIVisualiser<Game<?, ?>> gameBoardVisualiser;
  private final List<GameAgent<Game<?, ?>, ?>> gameAgents;

  private final long calculationTime;
  private final TimeUnit timeUnit;

  private final boolean interactiveMode;
  private Tournament tournamentMode;

  public Engine(Logger log, ExecutorService pool,
      Constructor<Game<?, ?>> gameConstructor,
      GameASCIIVisualiser<Game<?, ?>> gameBoardVisualiser,
      List<GameAgent<Game<?, ?>, ?>> gameAgents, long calculationTime, TimeUnit timeUnit,
      boolean interactiveMode,
      Tournament tournamentMode) {
    this.log = log;
    this.pool = pool;
    this.gameConstructor = gameConstructor;
    this.gameBoardVisualiser = gameBoardVisualiser;
    this.gameAgents = gameAgents;
    this.calculationTime = calculationTime;
    this.timeUnit = timeUnit;
    this.interactiveMode = interactiveMode;
    this.tournamentMode = tournamentMode;
  }

  @Override
  public void run() {
    log.info("Starting Engine.");
    if (interactiveMode) {
      interactiveMode();
    } else {
      tournamentMode();
    }
  }

  private void interactiveMode() {

  }

  private void tournamentMode() {
    tournamentMode.visualiseTournament(tournamentMode
        .playTournament(gameConstructor, gameBoardVisualiser, gameAgents, calculationTime, timeUnit,
            log, pool));
  }

}
