package dev.entze.sge.engine;

import dev.entze.sge.game.GameASCIIVisualiser;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public enum GamePlayOffMode implements GamePlayOff {

  SINGLE_ELEMINATION {
    @Override
    public int[] playOff(long calculationTime, TimeUnit timeUnit, Logger log, ExecutorService pool,
        Random random, Constructor gameConstructor, GameASCIIVisualiser gameASCIIVisualiser,
        List list) {
      return new int[0];
    }
  },
  DOUBLE_ELEMINATION {
    @Override
    public int[] playOff(long calculationTime, TimeUnit timeUnit, Logger log, ExecutorService pool,
        Random random, Constructor gameConstructor, GameASCIIVisualiser gameASCIIVisualiser,
        List list) {
      return new int[0];
    }
  },

  ROUND_ROBIN {
    @Override
    public int[] playOff(long calculationTime, TimeUnit timeUnit, Logger log, ExecutorService pool,
        Random random, Constructor gameConstructor, GameASCIIVisualiser gameASCIIVisualiser,
        List list) {
      return new int[0];
    }
  },

  DOUBLE_ROUND_ROBIN {
    @Override
    public int[] playOff(long calculationTime, TimeUnit timeUnit, Logger log, ExecutorService pool,
        Random random, Constructor gameConstructor, GameASCIIVisualiser gameASCIIVisualiser,
        List list) {
      return new int[0];
    }
  },

  FULL_ROUND_ROBIN {
    @Override
    public int[] playOff(long calculationTime, TimeUnit timeUnit, Logger log, ExecutorService pool,
        Random random, Constructor gameConstructor, GameASCIIVisualiser gameASCIIVisualiser,
        List list) {
      return new int[0];
    }
  },

}
