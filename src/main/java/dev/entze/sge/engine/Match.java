package dev.entze.sge.engine;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.agent.HumanAgent;
import dev.entze.sge.game.Game;
import dev.entze.sge.game.GameASCIIVisualiser;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Match<G extends Game<A, ?>, E extends GameAgent<G, A>, A> implements
    Callable<Double[]> {

  private Game<A, ?> game;
  private GameASCIIVisualiser<G> gameASCIIVisualiser;
  private List<E> gameAgents;
  private final boolean withHumanPlayer;
  private final long calculationTime;
  private final TimeUnit timeUnit;
  private final Logger log;
  private final ExecutorService pool;

  public Match(Game<A, ?> game, GameASCIIVisualiser<G> gameASCIIVisualiser,
      List<E> gameAgents, long calculationTime,
      TimeUnit timeUnit, Logger log, ExecutorService pool) {
    this.game = game;
    this.gameASCIIVisualiser = gameASCIIVisualiser;
    this.gameAgents = gameAgents;
    if (game.getNumberOfPlayers() != gameAgents.size()) {
      throw new IllegalArgumentException("Not the correct number of players");
    }
    boolean withHumanPlayer = false;
    for (E gameAgent : gameAgents) {
      withHumanPlayer = withHumanPlayer || (gameAgent instanceof HumanAgent);
    }
    this.withHumanPlayer = withHumanPlayer;
    this.calculationTime = calculationTime;
    this.timeUnit = timeUnit;
    this.log = log;
    this.pool = pool;
  }


  @Override
  public Double[] call() {
    for (GameAgent<G, A> gameAgent : gameAgents) {
      gameAgent.setUp();
    }

    Double[] result = new Double[gameAgents.size()];
    Arrays.fill(result, 1D);
    int lastPlayer = (-1);
    int thisPlayer;

    while (!game.isGameOver()) {

      thisPlayer = game.getCurrentPlayer();

      G playersGame = (G) game.getGame(thisPlayer);

      if (lastPlayer != thisPlayer) {
        log.info("Player " + game.getCurrentPlayer() + ": ");
        if (!withHumanPlayer || (gameAgents.get(thisPlayer) instanceof HumanAgent)) {
          log.info_(gameASCIIVisualiser.visualise(playersGame));
        }
      }

      final int finalThisPlayer = thisPlayer;
      Future<A> actionFuture = pool.submit(() -> gameAgents.get(finalThisPlayer)
          .calculateNextAction(playersGame, calculationTime, timeUnit));

      A action = null;

      try {
        action = actionFuture.get(calculationTime, timeUnit);
      } catch (InterruptedException e) {
        log.error("Interrupted.");
      } catch (ExecutionException e) {
        log.error("Exception while executing calculateNextAction().");
      } catch (TimeoutException e) {
        log.warn("Agent timeout.");
      }

      if (action == null) {
        log.warn("No action given.");
        result[thisPlayer] = (-1D);
        return result;
      }

      game.doAction(action);
    }

    for (GameAgent<G, A> gameAgent : gameAgents) {
      gameAgent.tearDown();
    }

    return result;
  }

  public List<E> getGameAgents() {
    return gameAgents;
  }

}
