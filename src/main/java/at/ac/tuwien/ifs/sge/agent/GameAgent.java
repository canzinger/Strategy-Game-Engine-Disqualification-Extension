package at.ac.tuwien.ifs.sge.agent;

import at.ac.tuwien.ifs.sge.game.Game;
import java.util.concurrent.TimeUnit;

public interface GameAgent<G extends Game<A, ?>, A> {

  A computeNextAction(G game, long computationTime, TimeUnit timeUnit);

  /**
   * Called before each match to allow the agent to be set up.
   *
   * @param numberOfPlayers number of players participating in this match
   * @param playerNumber beginning from 0 which player is the agent
   */
  default void setUp(int numberOfPlayers, int playerNumber) {

  }

  /**
   * Called after each match to allow to clean up temporary resources.
   */
  default void tearDown() {

  }

  default void ponderStart() {
  }

  default void ponderStop() {

  }

  /**
   * Called before the engine exits so that the agent can clean up resources (like database
   * connections).
   */
  default void destroy() {

  }

}
