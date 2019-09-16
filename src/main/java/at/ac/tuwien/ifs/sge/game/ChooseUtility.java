package at.ac.tuwien.ifs.sge.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ChooseUtility implements Game<Double, Double[]> {

  private final int numberOfPlayers;
  private int currentPlayerId;
  private final boolean canonical;
  private double[] board;
  private final List<ActionRecord<Double>> actionRecords;

  public ChooseUtility() {
    this(1);
  }

  public ChooseUtility(String initialBoard) {
    this();
  }

  public ChooseUtility(int numberOfPlayers) {
    this.numberOfPlayers = numberOfPlayers;
    currentPlayerId = 0;
    canonical = true;
    board = new double[numberOfPlayers];
    actionRecords = Collections.emptyList();
  }

  public ChooseUtility(String initialBoard, int numberOfPlayers) {
    this(numberOfPlayers);
  }

  public ChooseUtility(ChooseUtility chooseUtility) {
    this.numberOfPlayers = chooseUtility.numberOfPlayers;
    this.currentPlayerId = chooseUtility.currentPlayerId;
    this.canonical = chooseUtility.canonical;
    this.board = chooseUtility.board.clone();
    this.actionRecords = Collections.emptyList();
  }

  public ChooseUtility(int numberOfPlayers, int currentPlayerId, boolean canonical,
      double[] board, List<ActionRecord<Double>> actionRecords) {
    this.numberOfPlayers = numberOfPlayers;
    this.currentPlayerId = currentPlayerId;
    this.canonical = canonical;
    this.board = board.clone();
    this.actionRecords = new ArrayList<>(actionRecords);
  }

  @Override
  public boolean isGameOver() {
    return currentPlayerId >= numberOfPlayers;
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
    return this.numberOfPlayers;
  }

  @Override
  public int getCurrentPlayer() {
    return this.currentPlayerId;
  }

  @Override
  public double getUtilityValue(int player) {
    if (!(0 <= player && player < numberOfPlayers)) {
      return 0D;
    }
    return board[player];
  }

  @Override
  public Set<Double> getPossibleActions() {
    return IntStream.rangeClosed(0, numberOfPlayers)
        .mapToDouble(i -> ((double) i) / ((double) numberOfPlayers)).boxed().collect(
            Collectors.toSet());
  }

  @Override
  public Double[] getBoard() {
    return (Double[]) Arrays.stream(board).boxed().toArray();
  }

  @Override
  public Game<Double, Double[]> doAction(Double action) {
    if (isGameOver()) {
      throw new IllegalArgumentException("Game over.");
    }

    ChooseUtility next = new ChooseUtility(this);
    next.board[currentPlayerId] = action;
    next.currentPlayerId++;
    next.actionRecords.add(new ActionRecord<>(currentPlayerId, action));

    return next;
  }

  @Override
  public Double determineNextAction() {
    return null;
  }

  @Override
  public List<ActionRecord<Double>> getActionRecords() {
    return Collections.unmodifiableList(actionRecords);
  }

  @Override
  public boolean isCanonical() {
    return this.canonical;
  }

  @Override
  public Game<Double, Double[]> getGame(int player) {
    return new ChooseUtility(this.numberOfPlayers, this.currentPlayerId, false, this.board,
        this.actionRecords);
  }
}
