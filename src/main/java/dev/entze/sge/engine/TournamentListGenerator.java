package dev.entze.sge.engine;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.game.Game;
import dev.entze.sge.game.GameASCIIVisualiser;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public interface TournamentListGenerator<G extends Game<?, ?>, E extends GameAgent<G, ?>> {

  List<Match<G, E, ?>> getTournamentList(Constructor<G> gameConstructor,
      GameASCIIVisualiser<G> gameASCIIVisualiser, List<E> gameAgents,
      long calculationTime, TimeUnit timeUnit, Logger log, ExecutorService pool);

}
