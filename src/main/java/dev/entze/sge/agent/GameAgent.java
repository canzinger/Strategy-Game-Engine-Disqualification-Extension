package dev.entze.sge.agent;

import dev.entze.sge.game.Game;
import java.util.concurrent.TimeUnit;

public interface GameAgent<G extends Game> {

  int calculateNextAction(G game, long calculationTime, TimeUnit timeUnit);

  default void initialiseAgentForNewGame() {
  }

}
