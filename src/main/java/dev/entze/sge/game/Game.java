package dev.entze.sge.game;

import dev.entze.sge.util.Pair;
import java.util.List;
import java.util.Set;

public interface Game<A, B> {

  /**
   * Checks whether the game is over yet.
   *
   * @return true if and only if game over
   */
  boolean isGameOver();

  /**
   * Check how many players have to play to be a valid game.
   *
   * @return the minimum number of players
   */
  int getMinimumNumberOfPlayers();


  /**
   * Check how many players can play at most to be a valid game.
   *
   * @return the maximum number of players.
   */
  int getMaximumNumberOfPlayers();

  /**
   * Counts the number of involved players. Has to be between getMinimumNumberOfPlayers() and
   * getMaximumNumberOfPlayers(). This number cannot change throughout the game.
   *
   * @return the number of players
   */
  int getNumberOfPlayers();

  /**
   * Checks which player's move it is and returns the id of the player.
   *
   * @return the id of the player
   */
  int getCurrentPlayer();

  /**
   * Applies the (public) utility function of each player and returns the result in an array where
   * the index corresponds to the player id.
   *
   * @return the result of the utility function for each player
   */
  default double[] getGameUtilityValue() {
    double[] utilityValues = new double[getNumberOfPlayers()];
    for (int p = 0; p < utilityValues.length; p++) {
      utilityValues[p] = getUtilityValue(p);
    }
    return utilityValues;
  }

  /**
   * The weight of the utility function of a given player.
   *
   * @param player - the player
   * @return the weight of the utility function.
   */
  default double getPlayerUtilityWeight(int player) {
    if (getCurrentPlayer() == player) {
      return 1;
    }
    return (-1);
  }

  /**
   * The weight of the heuristic function of a given player. Per default the same as
   * getPlayerUtilityWeight().
   *
   * @param player - the player
   * @return the weight of the heuristic function.
   */
  default double getPlayerHeuristicWeight(int player) {
    return getPlayerUtilityWeight(player);
  }

  /**
   * Applies the (public) utility function of each player and returns the result multiplied with
   * their weight in a sum.
   *
   * Should no weight be supplied for a given player's utility function getPlayerUtilityWeight() is
   * used instead. Any number of weights can be given.
   *
   * @param weights - the optional weights
   * @return the weighted sum of the utility functions
   */
  default double getUtilityValue(double... weights) {
    double value = 0;
    for (int p = 0; p < getNumberOfPlayers(); p++) {
      if (p < weights.length) {
        value += weights[p] * getUtilityValue(p);
      } else {
        value += getPlayerUtilityWeight(p) * getUtilityValue(p);
      }
    }
    return value;
  }

  /**
   * Applies the heuristic function of each player and returns the result multiplied with their
   * weight in a sum.
   *
   * Should no weight be supplied for a given player's utility function getPlayerHeuristicWeight()
   * is used instead. Any number of weights can be given.
   *
   * @param weights - the optional weights
   * @return the weighted sum of the heuristic functions
   */
  default double getHeuristicValue(double... weights) {
    double value = 0;
    for (int p = 0; p < getNumberOfPlayers(); p++) {
      if (p < weights.length) {
        value += weights[p] * getHeuristicValue(p);
      } else {
        value += getPlayerHeuristicWeight(p) * getHeuristicValue(p);
      }
    }
    return value;
  }

  /**
   * Applies the (public) utility function for the given player.
   *
   * @param player - the player
   * @return the result of the utility function for the player
   */
  double getUtilityValue(int player);

  /**
   * Applies a heuristic function for the given player. Per default the same as getUtilityValue().
   *
   * @param player - the player
   * @return the result of the heuristic function for the player
   */
  default double getHeuristicValue(int player) {
    return getUtilityValue(player);
  }

  /**
   * Collects all possible moves and returns them as a set.
   *
   * @return a set of all possible moves.
   */
  Set<A> getPossibleActions();

  /**
   * Returns a copy of the current board. Notice that only in non-canonical games some information
   * might be hidden.
   *
   * @return the board
   */
  B getBoard();

  /**
   * Checks whether doAction(action) would not throw an exception.
   *
   * @param action - the action
   * @return true - iff the action is valid and possible.
   */
  boolean isValidAction(A action);

  /**
   * Does a given action.
   *
   * @param action - the action to take
   * @return a new copy of the game with the previous action applied.
   * @throws IllegalArgumentException - In the case of a non-existing action.
   */
  Game<A, B> doAction(A action);

  /**
   * Returns the record of all previous actions and which player has done it.
   *
   * @return the record of all previous actions
   */
  List<Pair<Integer, A>> getPreviousActions();

  /**
   * Checks whether this game or a parent is a creation of getGame.
   *
   * @return true if this or any parent was the result getGame.
   */
  boolean isCanonical();

  /***
   * The game as seen from the current player. In games with complete and perfect information or
   * non-canonical games (games where this function was already called) this method will return a
   * copy of this game as is. In other games the unknown information will be hidden and abstracted
   * via placeholders.
   * @return a copy of the game with only the information available to the player
   */
  default Game<A, B> getGame() {
    return getGame(this.getCurrentPlayer());
  }

  /**
   * The game as seen from the given player. In games with complete and perfect information or
   * non-canonical games (games where this function was already called) this method will return a
   * copy of this game as is. In other games the unknown information will be hidden and abstracted
   * via placeholders.
   *
   * @param player - the player
   * @return a copy of the game with only the information available to the player
   */
  Game<A, B> getGame(int player);

}
