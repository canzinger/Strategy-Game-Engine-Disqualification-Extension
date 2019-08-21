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
    public Tournament<Game<?, ?>, ?, ?> getTournament(GameFactory<Game<?, ?>> gameFactory,
        String board, List<GameAgent<Game<?, ?>, ?>> gameAgents, boolean debug,
        long computationTime,
        TimeUnit timeUnit, Logger log, ExecutorService pool) {
      return new RoundRobin<>(gameFactory, board, gameAgents, debug, computationTime, timeUnit, log,
          pool);
    }
  };

  abstract Tournament<Game<?, ?>, ?, ?> getTournament(GameFactory<Game<?, ?>> gameFactory,
      String board, List<GameAgent<Game<?, ?>, ?>> gameAgents, boolean debug,
      long computationTime,
      TimeUnit timeUnit, Logger log, ExecutorService pool);

}
