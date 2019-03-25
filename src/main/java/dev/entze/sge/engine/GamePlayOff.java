package dev.entze.sge.engine;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.game.Game;
import dev.entze.sge.game.GameASCIIVisualiser;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface GamePlayOff<G extends Game<A, ?>, A> {


  default double[] play(long calculationTime, TimeUnit timeUnit, Logger log, ExecutorService pool,
      G game,
      GameASCIIVisualiser<G> gameASCIIVisualiser,
      GameAgent<G, A>... gameAgents) {

    if (game.getNumberOfPlayers() != gameAgents.length) {
      throw new IllegalArgumentException("Not the correct number of players");
    }

    for (GameAgent<G, A> gameAgent : gameAgents) {
      gameAgent.setUp();
    }

    double[] result = new double[gameAgents.length];
    Arrays.fill(result, 1);
    int lastPlayer = (-1);
    int thisPlayer;

    while (!game.isGameOver()) {

      thisPlayer = game.getCurrentPlayer();

      G playersGame = (G) game.getGame(thisPlayer);

      if (lastPlayer != thisPlayer) {
        log.info("Player " + game.getCurrentPlayer() + ": ");
        gameASCIIVisualiser.visualise(playersGame);
      }

      final int finalThisPlayer = thisPlayer;
      Future<A> actionFuture = pool.submit(() -> gameAgents[finalThisPlayer]
          .calculateNextAction(playersGame, calculationTime, timeUnit));

      A action = null;

      try {
        action = actionFuture.get(calculationTime, timeUnit);
      } catch (InterruptedException e) {
        log.error("Interrupted.");
      } catch (ExecutionException e) {
        log.error("Exception while executing calculateNextAction().");
      } catch (TimeoutException e) {
        log.warn("Timeout.");
        result[thisPlayer] = (-1);
        return result;
      }

      if (action == null) {
        log.warn("No action given.");
        result[thisPlayer] = (-1);
        return result;
      }

      game.doAction(action);
    }

    for (GameAgent<G, A> gameAgent : gameAgents) {
      gameAgent.tearDown();
    }

    return result;
  }


  int[] playOff(long calculationTime, TimeUnit timeUnit, Logger log, ExecutorService pool,
      Random random,
      Constructor<G> gameConstructor, GameASCIIVisualiser<G> gameASCIIVisualiser,
      List<GameAgent<G, A>> gameAgentList);

  default int[] playOff(long calculationTime, TimeUnit timeUnit, Logger log, ExecutorService pool,
      Constructor<G> gameConstructor, GameASCIIVisualiser<G> gameASCIIVisualiser,
      List<GameAgent<G, A>> gameAgentList) {
    return playOff(calculationTime, timeUnit, log, pool, new Random(), gameConstructor,
        gameASCIIVisualiser, gameAgentList);
  }

}

