package dev.entze.sge.engine.game.tournament;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.engine.Logger;
import dev.entze.sge.engine.factory.GameFactory;
import dev.entze.sge.engine.game.MatchResult;
import dev.entze.sge.game.Game;
import dev.entze.sge.util.pair.Pair;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class DoubleRoundRobin<G extends Game<? extends A, ?>, E extends GameAgent<G, ? extends A>, A> implements
    Tournament<G, E, A> {

  private List<MatchResult<G, E>> tournamentResult;
  private String textRepresentation;
  private Map<String, Map<String, Double>> twoResult;
  private Map<String, Pair<Double, Double>> result;

  private final GameFactory<G> gameFactory;
  private final int numberOfPlayers;
  private final String board;
  private final List<E> gameAgents;
  private final boolean debug;
  private final long computationTime;
  private final TimeUnit timeUnit;
  private final Logger log;
  private final ExecutorService pool;


  public DoubleRoundRobin(GameFactory<G> gameFactory, int numberOfPlayers, String board,
      List<E> gameAgents,
      long computationTime, TimeUnit timeUnit, boolean debug, Logger log, ExecutorService pool) {
    this.tournamentResult = null;
    this.textRepresentation = null;
    this.gameFactory = gameFactory;
    this.numberOfPlayers = numberOfPlayers;
    this.board = board;
    this.gameAgents = gameAgents;
    this.debug = debug;
    this.computationTime = computationTime;
    this.timeUnit = timeUnit;
    this.log = log;
    this.pool = pool;
    this.result = null;
    this.twoResult = null;
  }

  @Override
  public List<MatchResult<G, E>> call() throws Exception {
    return null;
  }

  @Override
  public String toTextRepresentation() {
    return null;
  }
}
