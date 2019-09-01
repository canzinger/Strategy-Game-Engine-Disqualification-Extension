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

public abstract class AbstractTournament<G extends Game<? extends A, ?>, E extends GameAgent<G, ? extends A>, A> implements
    Tournament<G, E, A> {

  protected List<MatchResult<G, E>> tournamentResult;
  protected String textRepresentation;
  protected final GameFactory<G> gameFactory;
  protected final int numberOfPlayers;
  protected final String board;
  protected final List<E> gameAgents;
  protected final boolean debug;
  protected final long computationTime;
  protected final TimeUnit timeUnit;
  protected final Logger log;
  protected final ExecutorService pool;


  protected AbstractTournament(GameFactory<G> gameFactory, int numberOfPlayers, String board,
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
  }

  @Override
  public List<MatchResult<G, E>> call() {
    return Collections.unmodifiableList(this.tournamentResult);
  }

  @Override
  public String toTextRepresentation() {
    return textRepresentation;
  }

  @SuppressWarnings("unchecked")
  protected Game<A, ?> newInstanceOfGame() {
    return (Game<A, ?>) gameFactory.newInstance(board, numberOfPlayers);
  }

}
