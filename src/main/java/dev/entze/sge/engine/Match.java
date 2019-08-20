package dev.entze.sge.engine;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.agent.HumanAgent;
import dev.entze.sge.game.ActionRecord;
import dev.entze.sge.game.Game;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Match<G extends Game<? extends A, ?>, E extends GameAgent<G, ? extends A>, A> implements
    Callable<Double[]> {

  private final boolean withHumanPlayer;
  private final boolean debug;
  private final long computationTime;
  private final TimeUnit timeUnit;
  private final Logger log;
  private final ExecutorService pool;
  private Game<A, ?> game;
  private List<E> gameAgents;

  public Match(Game<A, ?> game, List<E> gameAgents, long computationTime,
      TimeUnit timeUnit, boolean debug, Logger log, ExecutorService pool) {
    this.game = game;
    this.gameAgents = gameAgents;
    if (game.getNumberOfPlayers() != gameAgents.size()) {
      throw new IllegalArgumentException("Not the correct number of players");
    }
    boolean withHumanPlayer = false;
    for (E gameAgent : gameAgents) {
      withHumanPlayer = withHumanPlayer || (gameAgent instanceof HumanAgent);
    }
    this.withHumanPlayer = withHumanPlayer;
    this.debug = debug;
    this.computationTime = computationTime;
    this.timeUnit = timeUnit;
    this.log = log;
    this.pool = pool;
  }


  @SuppressWarnings("unchecked")
  @Override
  public Double[] call() {
    for (int i = 0; i < gameAgents.size(); i++) {
      gameAgents.get(i).setUp(gameAgents.size(), i);
    }

    Double[] result = new Double[gameAgents.size()];
    Arrays.fill(result, 1D);
    int lastPlayer = (-1);
    int thisPlayer;
    boolean isHuman = false;
    while (!game.isGameOver() && Thread.currentThread().isAlive() && !Thread.currentThread()
        .isInterrupted()) {

      thisPlayer = game.getCurrentPlayer();
      if (thisPlayer >= 0) {

        G playersGame = (G) game.getGame(thisPlayer);
        isHuman = gameAgents.get(thisPlayer) instanceof HumanAgent;

        if (lastPlayer != thisPlayer) {
          log.info(
              "Player " + game.getCurrentPlayer() + " - " + gameAgents.get(thisPlayer).toString()
                  + ":");
          if (!withHumanPlayer || isHuman) {
            log.info_(playersGame.toTextRepresentation());
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
          log.printStackTrace(e);
        } catch (TimeoutException e) {
          if (isHuman || debug) {
            try {
              action = actionFuture.get();
            } catch (InterruptedException ex) {
              log.error("Interrupted.");
            } catch (ExecutionException ex) {
              log.error("Exception while executing computeNextAction().");
              log.printStackTrace(ex);
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

        if (!isHuman) {
          log.info_("> " + action.toString());
        }

        if (!game.isValidAction(action)) {
          log.warn("Illegal action given.");
          try {
            game.doAction(action);
          } catch (IllegalArgumentException e) {
            log.printStackTrace(e);
          }
          result[thisPlayer] = (-1D);
          return result;
        }

        game = game.doAction(action);
      } else {

        A action = game.determineNextAction();
        if (action == null || !game.isValidAction(action)) {
          log.error(
              "There is a programming error in the implementation of the game. Could not determine next action.");
          throw new IllegalStateException("The current game violates the implementation contract");
        }
        game = game.doAction(action);
      }

      lastPlayer = thisPlayer;

      if (game.getCurrentPlayer() >= 0 && isHuman && !(gameAgents
          .get(game.getCurrentPlayer()) instanceof HumanAgent) || lastPlayer == game
          .getCurrentPlayer()) {
        log.info_(game.getGame().toTextRepresentation());
      }
    }

    double[] utility = game.getGameUtilityValue();

    for (int i = 0; i < result.length; i++) {
      result[i] = utility[i];
    }

    log.info_("-----");
    log.info("Game over.");
    log.info_(game.toTextRepresentation());
    log.inf(game.getNumberOfActions() + " plies ");
    List<ActionRecord<A>> actionRecords = game.getActionRecords();
    lastPlayer = actionRecords.get(0).getPlayer() + 1;

    log.info_(ActionRecord.iterableToString(actionRecords, lastPlayer));

    System.out.print("Result: ");
    for (int i = 0; i < gameAgents.size(); i++) {
      System.out.print("Player " + i + ": " + result[i]);
      if (i + 1 < gameAgents.size()) {
        System.out.print(", ");
      }
    }
    System.out.println();
    for (E gameAgent : gameAgents) {
      gameAgent.tearDown();
    }

    return result;
  }

  public List<E> getGameAgents() {
    return gameAgents;
  }

}
