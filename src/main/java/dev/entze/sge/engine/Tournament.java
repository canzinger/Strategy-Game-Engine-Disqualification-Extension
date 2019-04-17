package dev.entze.sge.engine;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.engine.factory.GameFactory;
import dev.entze.sge.game.Game;
import dev.entze.sge.game.GameASCIIVisualiser;
import dev.entze.sge.util.pair.ImmutablePair;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public interface Tournament {

  List<ImmutablePair<List<GameAgent<Game<?, ?>, ?>>, Double[]>> playTournament(
      GameFactory gameFactory,
      GameASCIIVisualiser<Game<?, ?>> gameASCIIVisualiser,
      List<GameAgent<Game<?, ?>, ?>> gameAgents,
      int numberOfPlayers,
      long calculationTime, TimeUnit timeUnit, boolean debug, Logger log, ExecutorService pool);

  String visualiseTournament(
      List<ImmutablePair<List<GameAgent<Game<?, ?>, ?>>, Double[]>> tournament);

}
