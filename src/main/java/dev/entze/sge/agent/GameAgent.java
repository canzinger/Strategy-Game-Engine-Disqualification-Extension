package dev.entze.sge.agent;

import dev.entze.sge.game.Game;
import java.util.concurrent.TimeUnit;

public interface GameAgent<G extends Game<? extends A, ?>, A> {

  A calculateNextAction(G game, long calculationTime, TimeUnit timeUnit);

  default void setUp() {
  }

  default void tearDown() {

  }

}
