package dev.entze.sge.game;

import java.util.List;

public interface Game {

  /**
   * Checks whether the game is over yet.
   *
   * @return true if and only if game over
   */
  boolean isGameOver();

  /**
   * Counts the number of involved players. This number cannot change throughout the game.
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
   * @return the weight of the utlity function.
   */
  default double getPlayerUtilityWeight(int player) {
    if (getCurrentPlayer() == player) {
      return 1;
    }
    return (-1);
  }

  /**
   * Applies the (public) utility function of each player and returns the result multiplied with
   * their weight in a sum.
   *
   * Should no weight be supplied for a given player's utility function getPlayerUtilityWeight is
   * used instead. Any number of weights can be given.
   *
   * @param weights - the optional weights
   * @return the weighted sum of the utilty functions
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
   * Applies the (public) utility function for the given player.
   *
   * @param player - the player
   * @return the result of the utility function for the player
   */
  double getUtilityValue(int player);

  /**
   * Collects all possible moves and returns them in a list. The list can be translated via a
   * GameActionTranslator
   *
   * @return a list of all possible moves.
   */
  List<Integer> getPossibleActions();

  /**
   * Returns a copy of the current board. Notice that only in non-canonical games some information
   * might be hidden.
   *
   * @return the board
   */
  int[] getBoard();

  /**
   * Does a given action. Usually the value was acquired from the action table. The given value has
   * to be greater than or equal to 0. Negative values are rejected with an
   * IllegalArgumentException.
   *
   * @param action - the number of the action to take
   * @return a new copy of the game with the previous action applied.
   * @throws IllegalArgumentException - In the case of a non-existing action.
   */
  Game doAction(int action);

  /**
   * Returns the record of all previous moves. The first element in the array indicates the player
   * and the second element the action. Use the action table to translate to readable moves.
   *
   * @return the record of all previous moves
   */
  List<Integer[]> getPreviousActions();

  /**
   * Checks whether this game or a parent is a creation of getGame.
   *
   * @return true if this or any parent was the result getGame.
   */
  boolean isCanonical();

  /**
   * The game as seen from the given player. In games with complete and perfect information or
   * non-canonical games (games where this function was already called) this method will return a
   * copy of this game as is. In other games the unknown information will be hidden and abstracted
   * via placeholders.
   *
   * @param player - the player
   * @return a copy of the game with only the information available to the player
   */
  Game getGame(int player);

}
