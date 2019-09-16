package at.ac.tuwien.ifs.sge.game;

import java.util.List;
import java.util.Set;

/**
 * A game.
 *
 * @param <A> - Type of Action
 * @param <B> - Type of Board
 */
public interface Game<A, B> {

  /**
   * Checks whether the game is over yet. Once this state is reached it can not be left.
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
   * Checks which player's move it is and returns the id of the player. A negative number indicates
   * some indeterminacy which is resolved by the game itself.
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
   * The weight of the utility function of a given player. Per default 0 if player or the
   * currentPlayer is less than 0 and 1 iff getCurrentPlayer() equals player otherwise -1.
   *
   * @param player - the player
   * @return the weight of the utility function.
   */
  default double getPlayerUtilityWeight(int player) {
    if (player < 0 || getCurrentPlayer() < 0) {
      return 0;
    }
    if (getCurrentPlayer() == player) {
      return 1;
    }
    return (-1);
  }

  /**
   * Applies the heuristic function of each player and returns the result in an array where the
   * index corresponds to the player id.
   *
   * @return the result of the utility function for each player
   */
  default double[] getGameHeuristicValue() {
    double[] heuristicValues = new double[getNumberOfPlayers()];

    for (int p = 0; p < heuristicValues.length; p++) {
      heuristicValues[p] = getHeuristicValue(p);
    }

    return heuristicValues;
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
   * <p>
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
   * <p>
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
   * Applies the (public) utility function for the given player. The utility function is the final
   * measure which determines how "good" a player does. The player with the highest value is
   * considered the winner. On equality it is considered a tie.
   *
   * @param player - the player
   * @return the result of the utility function for the player
   */
  double getUtilityValue(int player);

  /**
   * Applies the heuristic function for the given player. This function is a more lax measure in how
   * "good" a player does, it is not used to determine the outcome of a game. Per default the same
   * as getUtilityValue().
   *
   * @param player - the player
   * @return the result of the heuristic function for the player
   */
  default double getHeuristicValue(int player) {
    return getUtilityValue(player);
  }

  /**
   * Collects all possible moves and returns them as a set. Should the game be over an empty set is
   * returned instead.
   *
   * @return a set of all possible moves
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
   * @return true - iff the action is valid and possible
   */
  default boolean isValidAction(A action) {
    return getPossibleActions().contains(action);
  }

  /**
   * Does a given action.
   *
   * @param action - the action to take
   * @return a new copy of the game with the given action applied
   * @throws IllegalArgumentException - In the case of a non-existing action or null
   * @throws IllegalStateException    - If game over
   */
  Game<A, B> doAction(A action);

  /**
   * Progresses the game if it currently is in an indeterminant state.
   *
   * @return a new copy of the game with an action applied
   * @throws IllegalArgumentException - If the game is not in an non-deterministic state
   * @throws IllegalStateException    - If game over
   */
  default Game<A, B> doAction() {
    return doAction(determineNextAction());
  }

  /**
   * If the game is in a state of indeterminacy, this method will return an action according to the
   * distribution of probabilities, or hidden information. If the game is in a definitive state null
   * is returned.
   *
   * @return a possible action, which determines the game
   */
  A determineNextAction();

  /**
   * Returns the last action and which player did the action. If no action was done yet null is
   * returned.
   *
   * @return the last action record
   */
  default ActionRecord<A> getPreviousActionRecord() {
    List<ActionRecord<A>> actionRecordList = getActionRecords();
    if (actionRecordList.isEmpty()) {
      return null;
    }
    return actionRecordList.get(getNumberOfActions() - 1);
  }

  /**
   * Returns only the last action, but not which player did the action. If no action was done yet
   * given null is returned.
   *
   * @return the last action
   */
  default A getPreviousAction() {
    ActionRecord<A> previousAction = getPreviousActionRecord();
    return previousAction == null ? null : previousAction.getAction();
  }

  /**
   * Returns the record of all previous actions and which player has done it.
   *
   * @return the record of all previous actions
   */
  List<ActionRecord<A>> getActionRecords();

  /**
   * Returns how many actions were taken.
   *
   * @return how many actions were taken
   */
  default int getNumberOfActions() {
    return getActionRecords().size();
  }

  /**
   * Checks if the game is canonical. A game is canonical if it bears all information and is not the
   * child of a non-canonical game.
   *
   * @return true if canonical
   */
  boolean isCanonical();

  /***
   * The game as seen from the current player. In games with complete and perfect information or
   * non-canonical games (games where this function was already called) this method will return a
   * copy of this game as is. In other games the unknown information will be hidden and abstracted
   * via placeholders.
   *
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

  /**
   * A representation of the current game via text. Can be multiline. Per default toString.
   *
   * @return a string representing the game
   */
  default String toTextRepresentation() {
    return this.toString();
  }

}
