package dev.entze.sge.engine.game;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.agent.HumanAgent;
import dev.entze.sge.engine.Logger;
import dev.entze.sge.game.ActionRecord;
import dev.entze.sge.game.Game;
import dev.entze.sge.util.pair.ImmutablePair;
import dev.entze.sge.util.pair.Pair;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class Match<G extends Game<? extends A, ?>, E extends GameAgent<G, ? extends A>, A> implements
    Callable<MatchResult<G, E>> {

  private final boolean withHumanPlayer;
  private final boolean debug;
  private final long computationTime;
  private final TimeUnit timeUnit;
  private final Logger log;
  private final ExecutorService pool;
  private MatchResult<G, E> matchResult;
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
    this.matchResult = null;
  }


  @SuppressWarnings("unchecked")
  @Override
  public MatchResult<G, E> call() {
    long startTime = System.nanoTime();
    if (matchResult != null) {
      return matchResult;
    }
    {
      Deque<Pair<String, Future<Void>>> setUps = new ArrayDeque<>(gameAgents.size());
      for (int i = 0; i < gameAgents.size(); i++) {
        final E gameAgent = gameAgents.get(i);
        int finalI = i;
        setUps
            .add(new ImmutablePair<>(gameAgent.toString(), pool.submit(() -> {
              gameAgent.setUp(gameAgents.size(), finalI);
              return null;
            })));
      }

      final int setUpsSize = setUps.size();
      log.traProcess("Setting up agents", 0, setUpsSize);
      while (!setUps.isEmpty() && Thread.currentThread().isAlive() && !Thread.currentThread()
          .isInterrupted()) {
        Pair<String, Future<Void>> setUp = setUps.pop();
        log.tra_("\r");
        log.traProcess("Setting up agents", setUpsSize - setUps.size(), setUpsSize);
        try {
          setUp.getB().get();
        } catch (InterruptedException e) {
          log.trace_(", failed.");
          log.debug("Interrupted while setting up agent ".concat(setUp.getA()));
          log.printStackTrace(e);
        } catch (ExecutionException e) {
          log.debug("Exception while setting up agent ".concat(setUp.getA()));
          log.printStackTrace(e);
        }
      }

      if (!setUps.isEmpty()) {
        log.trace_(", failed.");
        log.warn("Following agents where not verified to be set up: "
            .concat(setUps.stream().map(Pair::getA).collect(Collectors.joining(", "))));
      } else {
        log.trace_(", done.");
      }
    }

    double[] result = new double[gameAgents.size()];
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
          matchResult = new MatchResult<>(gameAgents, startTime, System.nanoTime(), result);
          log.debf("%d plies: ", game.getNumberOfActions());
          log.debug_(ActionRecord.iterableToString(game.getActionRecords()));
          return matchResult;
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
          matchResult = new MatchResult<>(gameAgents, startTime, System.nanoTime(), result);
          log.debf("%d plies: ", game.getNumberOfActions());
          log.debug_(ActionRecord.iterableToString(game.getActionRecords()));
          return matchResult;
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
    long endTime = System.nanoTime();

    double[] utility = game.getGameUtilityValue();

    for (int i = 0; i < result.length; i++) {
      result[i] = utility[i];
    }

    log.info_("-----");
    log.info("Game over.");
    log.info_(game.toTextRepresentation());
    log.inf(game.getNumberOfActions() + " plies ");
    List<ActionRecord<A>> actionRecords = game.getActionRecords();

    log.info_(ActionRecord.iterableToString(actionRecords));

    {
      Deque<Pair<String, Future<Void>>> tearDowns = new ArrayDeque<>(gameAgents.size());
      for (E gameAgent : gameAgents) {
        tearDowns
            .add(new ImmutablePair<>(gameAgent.toString(), pool.submit(() -> {
              gameAgent.tearDown();
              return null;
            })));
      }

      final int tearDownsSize = tearDowns.size();
      log.traProcess("Tearing down agents", 0, tearDownsSize);
      while (!tearDowns.isEmpty() && Thread.currentThread().isAlive() && !Thread.currentThread()
          .isInterrupted()) {
        Pair<String, Future<Void>> tearDown = tearDowns.pop();
        log.tra_("\r");
        log.traProcess("Tearing down agents", tearDownsSize - tearDowns.size(), tearDownsSize);
        try {
          tearDown.getB().get();
        } catch (InterruptedException e) {
          log.trace_(", failed.");
          log.debug("Interrupted while tearing down agent ".concat(tearDown.getA()));
          log.printStackTrace(e);
        } catch (ExecutionException e) {
          log.debug("Exception while tearing down agent ".concat(tearDown.getA()));
          log.printStackTrace(e);
        }
      }

      if (!tearDowns.isEmpty()) {
        log.trace_(", failed.");
        log.warn("Following agents where not verified to be torn down: "
            .concat(tearDowns.stream().map(Pair::getA).collect(Collectors.joining(", "))));
      } else {
        log.trace_(", done.");
      }
    }

    matchResult = new MatchResult<>(gameAgents, startTime, endTime, result);
    return matchResult;
  }

  public List<E> getGameAgents() {
    return gameAgents;
  }

}
