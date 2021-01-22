package at.ac.tuwien.ifs.sge.engine.game.tournament;

import at.ac.tuwien.ifs.sge.agent.GameAgent;
import at.ac.tuwien.ifs.sge.engine.Logger;
import at.ac.tuwien.ifs.sge.engine.factory.GameFactory;
import at.ac.tuwien.ifs.sge.game.Game;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public enum TournamentMode {

  ROUND_ROBIN {
    public Tournament<Game<Object, Object>, GameAgent<Game<Object, Object>, Object>, Object> getTournament(
        GameFactory<Game<Object, Object>> gameFactory, int numberOfPlayers,
        String board, List<GameAgent<Game<Object, Object>, Object>> gameAgents,
        long computationTime,
        TimeUnit timeUnit, boolean debug, Logger log, ExecutorService pool, int maxActions) {
      return new RoundRobin<>(gameFactory, numberOfPlayers, board, gameAgents, computationTime,
          timeUnit, debug, log,
          pool, maxActions);
    }

    @Override
    public int getMinimumPerRound() {
      return 2;
    }

    @Override
    public int getMaximumPerRound() {
      return Integer.MAX_VALUE;
    }


  },
  DOUBLE_ROUND_ROBIN {
    @Override
    public Tournament<Game<Object, Object>, GameAgent<Game<Object, Object>, Object>, Object> getTournament(
        GameFactory<Game<Object, Object>> gameFactory, int numberOfPlayers, String board,
        List<GameAgent<Game<Object, Object>, Object>> gameAgents, long computationTime,
        TimeUnit timeUnit, boolean debug, Logger log, ExecutorService pool, int maxActions) {
      return new DoubleRoundRobin<>(gameFactory, numberOfPlayers, board, gameAgents,
          computationTime, timeUnit, debug, log, pool, maxActions);
    }

    @Override
    public int getMinimumPerRound() {
      return 2;
    }

    @Override
    public int getMaximumPerRound() {
      return Integer.MAX_VALUE;
    }
  };

  public abstract Tournament<Game<Object, Object>, GameAgent<Game<Object, Object>, Object>, Object> getTournament(
      GameFactory<Game<Object, Object>> gameFactory, int numberOfPlayers,
      String board, List<GameAgent<Game<Object, Object>, Object>> gameAgents,
      long computationTime,
      TimeUnit timeUnit, boolean debug, Logger log, ExecutorService pool, int maxActions);

  public abstract int getMinimumPerRound();

  public abstract int getMaximumPerRound();
}
