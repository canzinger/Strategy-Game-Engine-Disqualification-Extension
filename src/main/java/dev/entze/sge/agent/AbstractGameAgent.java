package dev.entze.sge.agent;

import dev.entze.sge.game.Game;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public abstract class AbstractGameAgent<G extends Game<A, ?>, A> implements GameAgent<G, A> {

  private static final long ONE_SECOND = TimeUnit.SECONDS.toNanos(1L);

  private long TIMEOUT_MULTIPLIER;
  private long TIMEOUT_DIVISOR;

  private long START_TIME;
  private long TIMEOUT;
  private long AT_LEAST;

  protected final Random random;

  protected double[] minMaxWeights;
  protected double[] evenWeights;

  protected int playerNumber;

  protected AbstractGameAgent() {
    this(1L, 2L);
  }

  protected AbstractGameAgent(long timeOutMultiplier, long timeOutDivisor) {
    this(timeOutMultiplier, timeOutDivisor, 10L, TimeUnit.SECONDS);
  }

  protected AbstractGameAgent(long timeOutMultiplier, long timeOutDivisor, long atLeast,
      TimeUnit atLeastTimeUnit) {
    this.TIMEOUT_MULTIPLIER = timeOutMultiplier;
    this.TIMEOUT_DIVISOR = timeOutDivisor;
    this.AT_LEAST = atLeastTimeUnit.toNanos(atLeast);
    this.random = new Random();
  }

  protected AbstractGameAgent(double timeOutRatio) {
    this(timeOutRatio, 10L, TimeUnit.SECONDS);
  }

  protected AbstractGameAgent(double timeOutRatio, long precision) {
    this(timeOutRatio, precision, 10L, TimeUnit.SECONDS);
  }

  protected AbstractGameAgent(double timeOutRatio, long atLeast, TimeUnit atLeastTimeUnit) {
    this(timeOutRatio, TimeUnit.MILLISECONDS.toNanos(1L), 10L, TimeUnit.SECONDS);
  }

  protected AbstractGameAgent(double timeOutRatio, long precision, long atLeast,
      TimeUnit atLeastTimeUnit) {
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
  }

  @Override
  public A computeNextAction(G game, long computationTime, TimeUnit timeUnit) {
    START_TIME = System.nanoTime();
    long computationTimeInNanos = timeUnit.toNanos(computationTime) - ONE_SECOND;
    if (computationTimeInNanos < 0) {
      computationTimeInNanos = timeUnit.toNanos(computationTime);
    }
    TIMEOUT = (computationTimeInNanos * TIMEOUT_MULTIPLIER) / TIMEOUT_DIVISOR;
    TIMEOUT = Math.max(TIMEOUT, Math.min(computationTimeInNanos, AT_LEAST));

    return null;
  }

  protected boolean shouldStopComputation() {
    return System.nanoTime() - START_TIME >= TIMEOUT || !Thread.currentThread().isAlive() || Thread
        .currentThread().isInterrupted();
  }


}
