package dev.entze.sge.engine.game.tournament;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.engine.Logger;
import dev.entze.sge.engine.factory.GameFactory;
import dev.entze.sge.engine.game.MatchResult;
import dev.entze.sge.game.Game;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class RoundRobin<G extends Game<? extends A, ?>, E extends GameAgent<G, ? extends A>, A> implements
    Tournament<G, E, A> {

  private List<MatchResult<G, E>> tournamentResult;
  private final GameFactory<G> gameFactory;
  private final List<E> gameAgents;
  private final boolean debug;
  private final long computationTime;
  private final TimeUnit timeUnit;
  private final Logger log;
  private final ExecutorService pool;

  public RoundRobin(GameFactory<G> gameFactory, List<E> gameAgents, boolean debug,
      long computationTime,
      TimeUnit timeUnit, Logger log, ExecutorService pool) {
    this.gameFactory = gameFactory;
    this.gameAgents = gameAgents;
    this.debug = debug;
    this.computationTime = computationTime;
    this.timeUnit = timeUnit;
    this.log = log;
    this.pool = pool;
  }

  @Override
  public String toTextRepresentation() {
    return null;
  }

  @Override
  public List<MatchResult<G, E>> call() {
    if (tournamentResult != null) {
      return Collections.unmodifiableList(tournamentResult);
    }

    return Collections.unmodifiableList(tournamentResult);
  }
}
