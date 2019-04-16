package dev.entze.sge.agent;

import dev.entze.sge.game.Game;
import java.util.concurrent.TimeUnit;

public interface GameAgent<G extends Game<A, ?>, A> {

  A computeNextAction(G game, long computationTime, TimeUnit timeUnit);

  default void setUp(int numberOfPlayers, int playerNumber) {

  }

  default void tearDown() {

  }

}
