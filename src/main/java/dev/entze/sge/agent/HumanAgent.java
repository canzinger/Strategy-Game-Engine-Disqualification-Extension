package dev.entze.sge.agent;

import dev.entze.sge.game.Game;
import java.util.concurrent.TimeUnit;

public final class HumanAgent<G extends Game<A, ?>, A> implements GameAgent<G, A> {

  @Override
  public A calculateNextAction(G game, long calculationTime, TimeUnit timeUnit) {
    return null;
  }
}
