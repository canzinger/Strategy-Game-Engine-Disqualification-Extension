package dev.entze.sge.engine;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.game.Game;
import dev.entze.sge.game.GameASCIIVisualiser;
import dev.entze.sge.util.Pair;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public interface Tournament {

  List<Pair<List<GameAgent<Game<?, ?>, ?>>, Double[]>> playTournament(
      Constructor<Game<?, ?>> gameConstructor,
      GameASCIIVisualiser<Game<?, ?>> gameASCIIVisualiser,
      List<GameAgent<Game<?, ?>, ?>> gameAgents,
      long calculationTime, TimeUnit timeUnit, Logger log, ExecutorService pool);

  String visualiseTournament(List<Pair<List<GameAgent<Game<?, ?>, ?>>, Double[]>> tournament);

}
