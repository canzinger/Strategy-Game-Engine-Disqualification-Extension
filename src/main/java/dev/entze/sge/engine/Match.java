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

  private final boolean withHumanPlayer;
  private final long computationTime;
  private final TimeUnit timeUnit;
  private final Logger log;
  private final ExecutorService pool;
  private Game<A, ?> game;
  private GameASCIIVisualiser<G> gameASCIIVisualiser;
  private List<E> gameAgents;

  public Match(Game<A, ?> game, GameASCIIVisualiser<G> gameASCIIVisualiser,
      List<E> gameAgents, long computationTime,
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
    this.computationTime = computationTime;
    this.timeUnit = timeUnit;
    this.log = log;
    this.pool = pool;
  }


  @Override
  public Double[] call() {
    for (GameAgent<G, A> gameAgent : gameAgents) {
      gameAgent.setUp(gameAgents.size());
    }

    Double[] result = new Double[gameAgents.size()];
    Arrays.fill(result, 1D);
    int lastPlayer = (-1);
    int thisPlayer;
    boolean isHuman = false;
    while (!game.isGameOver()) {

      thisPlayer = game.getCurrentPlayer();
      if (thisPlayer >= 0) {

        G playersGame = (G) game.getGame(thisPlayer);
        isHuman = gameAgents.get(thisPlayer) instanceof HumanAgent;

        if (lastPlayer != thisPlayer) {
          log.info("Player " + game.getCurrentPlayer() + ": ");
          if (!withHumanPlayer || isHuman) {
            log.info_(gameASCIIVisualiser.visualise(playersGame));
          }
        }

        final int finalThisPlayer = thisPlayer;
        Future<A> actionFuture = pool.submit(() -> gameAgents.get(finalThisPlayer)
            .computeNextAction(playersGame, computationTime, timeUnit));

        A action = null;

        try {
          action = actionFuture.get(computationTime, timeUnit);
        } catch (InterruptedException e) {
          log.error("Interrupted.");
        } catch (ExecutionException e) {
          log.error("Exception while executing computeNextAction().");
        } catch (TimeoutException e) {
          if (isHuman) {
            try {
              action = actionFuture.get();
            } catch (InterruptedException ex) {
              log.error("Interrupted.");
            } catch (ExecutionException ex) {
              log.error("Exception while executing computeNextAction().");
            }
          } else {
            log.warn("Agent timeout.");
          }
        }

        if (action == null) {
          log.warn("No action given.");
          result[thisPlayer] = (-1D);
          return result;
        }

        if (!game.isValidAction(action)) {
          log.warn("Illegal action given.");
          try {
            game.doAction(action);
          } catch (IllegalArgumentException e) {
            e.printStackTrace();
          }
          result[thisPlayer] = (-1D);
          return result;
        }

        game = game.doAction(action);
      } else {

        A action = game.determineNextAction();
        if (action == null) {
          log.error(
              "There is a programming error in the implementation of game. Could not determine next action.");
          throw new IllegalStateException("The current game violates the implementation contract");
        }
        game = game.doAction(action);
      }

      if (game.getCurrentPlayer() >= 0 && isHuman && !(gameAgents
          .get(game.getCurrentPlayer()) instanceof HumanAgent)) {
        log.info_(gameASCIIVisualiser.visualise((G) game.getGame()));
      }
    }

    for (int i = 0; i < result.length; i++) {
      result[i] = game.getGameUtilityValue()[i];
    }

    log.info_("-----");
    log.info("Game over.");
    log.info_(gameASCIIVisualiser.visualise((G) game));
    log.inf("Result: ");
    for (int i = 0; i < gameAgents.size(); i++) {
      log.inf_("Player " + i + " : " + result[i]);
      if (i + 1 < gameAgents.size()) {
        log.inf_(", ");
      }
    }
    log.info_();

    for (GameAgent<G, A> gameAgent : gameAgents) {
      gameAgent.tearDown();
    }

    return result;
  }

  public List<E> getGameAgents() {
    return gameAgents;
  }

}
