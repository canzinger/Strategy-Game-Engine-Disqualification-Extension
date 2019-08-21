package dev.entze.sge.engine.game.tournament;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.engine.Logger;
import dev.entze.sge.engine.factory.GameFactory;
import dev.entze.sge.game.Game;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public enum TournamentMode {

  ROUND_ROBIN {
    public Tournament<Game<Object, Object>, GameAgent<Game<Object, Object>, Object>, Object> getTournament(
        GameFactory<Game<Object, Object>> gameFactory,
        String board, List<GameAgent<Game<Object, Object>, Object>> gameAgents,
        long computationTime,
        TimeUnit timeUnit, boolean debug, Logger log, ExecutorService pool) {
      return new RoundRobin<>(gameFactory, board, gameAgents, computationTime, timeUnit, debug, log,
          pool);
    }
  };

  public abstract Tournament<Game<Object, Object>, GameAgent<Game<Object, Object>, Object>, Object> getTournament(
      GameFactory<Game<Object, Object>> gameFactory,
      String board, List<GameAgent<Game<Object, Object>, Object>> gameAgents,
      long computationTime,
      TimeUnit timeUnit, boolean debug, Logger log, ExecutorService pool);

}
