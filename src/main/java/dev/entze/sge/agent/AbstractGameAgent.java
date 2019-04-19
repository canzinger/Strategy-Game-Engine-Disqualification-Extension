package dev.entze.sge.agent;

import dev.entze.sge.engine.Logger;
import dev.entze.sge.game.Game;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public abstract class AbstractGameAgent<G extends Game<A, ?>, A> implements GameAgent<G, A> {

  private static final long ONE_SECOND = TimeUnit.SECONDS.toNanos(1L);
  protected final Random random;
  protected final Logger log;
  protected long START_TIME;
  protected long ACTUAL_TIMEOUT;
  protected long TIMEOUT;
  protected Comparator<Game<A, ?>> gameUtilityComparator;
  protected Comparator<Game<A, ?>> gameHeuristicComparator;
  protected Comparator<Game<A, ?>> gameComparator;
  protected double[] minMaxWeights;
  protected double[] evenWeights;
  protected int playerNumber;
  private long TIMEOUT_MULTIPLIER;
  private long TIMEOUT_DIVISOR;
  private long AT_LEAST;

  protected AbstractGameAgent() {
    this(null);
  }

  protected AbstractGameAgent(Logger log) {
    this(1L, 2L, log);
  }

  protected AbstractGameAgent(long timeOutMultiplier, long timeOutDivisor, Logger log) {
    this(timeOutMultiplier, timeOutDivisor, 10L, TimeUnit.SECONDS, log);
  }

  protected AbstractGameAgent(long timeOutMultiplier, long timeOutDivisor, long atLeast,
      TimeUnit atLeastTimeUnit, Logger log) {
    this.TIMEOUT_MULTIPLIER = timeOutMultiplier;
    this.TIMEOUT_DIVISOR = timeOutDivisor;
    this.AT_LEAST = atLeastTimeUnit.toNanos(atLeast);
    this.random = new Random();
    this.log = log;
  }

  protected AbstractGameAgent(double timeOutRatio, Logger log) {
    this(timeOutRatio, 10L, TimeUnit.SECONDS, log);
  }

  protected AbstractGameAgent(double timeOutRatio, long precision, Logger log) {
    this(timeOutRatio, precision, 10L, TimeUnit.SECONDS, log);
  }

  protected AbstractGameAgent(double timeOutRatio, long atLeast, TimeUnit atLeastTimeUnit,
      Logger log) {
    this(timeOutRatio, TimeUnit.MILLISECONDS.toNanos(1L), 10L, TimeUnit.SECONDS, log);
  }

  protected AbstractGameAgent(double timeOutRatio, long precision, long atLeast,
      TimeUnit atLeastTimeUnit, Logger log) {
    long timeOutDivisor = 1;
    long timeOutMultiplier = (long) (timeOutRatio / timeOutDivisor);

    while ((double) timeOutMultiplier / (double) timeOutDivisor != timeOutRatio
        || timeOutDivisor >= precision) {
      timeOutDivisor *= 10;
      timeOutMultiplier = (long) (timeOutDivisor * timeOutRatio);
    }
    this.AT_LEAST = atLeastTimeUnit.toNanos(atLeast);
    this.TIMEOUT_MULTIPLIER = timeOutMultiplier;
    this.TIMEOUT_DIVISOR = timeOutDivisor;
    this.random = new Random();
    this.log = log;
  }

  @Override
  public void setUp(int numberOfPlayers, int playerNumber) {
    minMaxWeights = new double[numberOfPlayers];
    Arrays.fill(minMaxWeights, -1D);
    minMaxWeights[playerNumber] = 1D;

    evenWeights = new double[numberOfPlayers];
    Arrays.fill(evenWeights, -1D / (-1D + numberOfPlayers));
    evenWeights[playerNumber] = 1D;

    this.playerNumber = playerNumber;

    gameUtilityComparator = Comparator
        .comparingDouble(o -> o.getUtilityValue(minMaxWeights));
    gameHeuristicComparator = Comparator
        .comparingDouble(o -> o.getHeuristicValue(minMaxWeights));
    gameComparator = gameUtilityComparator.thenComparing(gameHeuristicComparator);
  }

  protected void setTimers(long computationTime, TimeUnit timeUnit) {
    START_TIME = System.nanoTime();
    ACTUAL_TIMEOUT = timeUnit.toNanos(computationTime);
    long computationTimeInNanos = ACTUAL_TIMEOUT - ONE_SECOND;
    if (computationTimeInNanos < 0) {
      computationTimeInNanos = ACTUAL_TIMEOUT;
    }
    TIMEOUT = (computationTimeInNanos * TIMEOUT_MULTIPLIER) / TIMEOUT_DIVISOR;
    TIMEOUT = Math.max(TIMEOUT, Math.min(computationTimeInNanos, AT_LEAST));
  }

  protected boolean shouldStopComputation() {
    return System.nanoTime() - START_TIME >= TIMEOUT || !Thread.currentThread().isAlive() || Thread
        .currentThread().isInterrupted();
  }

}
