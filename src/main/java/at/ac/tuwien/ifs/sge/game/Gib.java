package at.ac.tuwien.ifs.sge.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Gib implements Game<String, String> {


  private final boolean canonical;
  private final Set<String> targets;
  private final Set<String> allowedStrings;
  private final int numberOfPlayers;
  private int currentPlayer;
  private List<ActionRecord<String>> actionRecords;
  private String board;


  public Gib(String initialBoard, int numberOfPlayers) {
    this(Arrays.asList(initialBoard.split(";")[0].split(",")),
        Arrays.asList(initialBoard.split(";")[1].split(",")), numberOfPlayers);
  }

  public Gib(Collection<String> targets, Collection<String> allowedStrings) {
    this(targets, allowedStrings, 2);
  }

  public Gib(Collection<String> targets, Collection<String> allowedStrings, int numberOfPlayers) {
    this(0, true, Collections.emptyList(), "", targets, allowedStrings, numberOfPlayers);
  }

  public Gib(int currentPlayer, boolean canonical,
      List<ActionRecord<String>> actionRecords, String board, Collection<String> targets,
      Collection<String> allowedStrings, int numberOfPlayers) {
    this.currentPlayer = currentPlayer;
    this.canonical = canonical;
    this.actionRecords = new ArrayList<>(actionRecords);
    this.board = board;
    this.targets = Collections.unmodifiableSet(new TreeSet<>(targets));
    this.allowedStrings = Collections.unmodifiableSet(new TreeSet<>(allowedStrings));
    this.numberOfPlayers = numberOfPlayers;
  }

  @Override
  public boolean isGameOver() {
    return targets.stream().anyMatch(t -> t.equals(board)) || targets.stream()
        .noneMatch(t -> t.startsWith(board));
  }

  @Override
  public int getMinimumNumberOfPlayers() {
    return 1;
  }

  @Override
  public int getMaximumNumberOfPlayers() {
    return Integer.MAX_VALUE;
  }

  @Override
  public int getNumberOfPlayers() {
    return numberOfPlayers;
  }

  @Override
  public int getCurrentPlayer() {
    return currentPlayer;
  }

  @Override
  public double getUtilityValue(int player) {
    return targets.stream().anyMatch(t -> t.equals(board)) ? (
        ((currentPlayer + (numberOfPlayers - 1)) % numberOfPlayers) == player ? 1D : 0D)
        : 1D / numberOfPlayers;
  }

  @Override
  public Set<String> getPossibleActions() {
    if (isGameOver()) {
      return Collections.emptySet();
    }
    return allowedStrings;
  }

  @Override
  public String getBoard() {
    return board;
  }

  @Override
  public boolean isValidAction(String action) {
    return !isGameOver() && allowedStrings.contains(action);
  }

  @Override
  public Game<String, String> doAction(String action) {
    if (isGameOver()) {
      throw new IllegalStateException("Cannot do an action when game is over");
    }
    if (!isValidAction(action)) {
      throw new IllegalArgumentException(action + " is not a valid string");
    }
    Gib next = new Gib((currentPlayer + 1) % numberOfPlayers, canonical, actionRecords,
        board + action, targets, allowedStrings, numberOfPlayers);

    next.actionRecords.add(new ActionRecord<>(currentPlayer, action));

    return next;
  }

  @Override
  public String determineNextAction() {
    return null;
  }

  @Override
  public List<ActionRecord<String>> getActionRecords() {
    return Collections.unmodifiableList(actionRecords);
  }

  @Override
  public boolean isCanonical() {
    return canonical;
  }

  @Override
  public Game<String, String> getGame(int player) {
    return new Gib(currentPlayer, false, actionRecords, board, targets, allowedStrings,
        numberOfPlayers);
  }


  @Override
  public String toString() {
    return board;
  }
}
