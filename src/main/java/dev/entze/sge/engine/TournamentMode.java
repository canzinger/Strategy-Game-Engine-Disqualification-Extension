package dev.entze.sge.engine;

import dev.entze.sge.game.GameASCIIVisualiser;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public enum TournamentMode implements TournamentListGenerator {

  SINGLE_ELEMINATION{
    @Override
    public List<Match> getTournamentList(Constructor gameConstructor,
        GameASCIIVisualiser gameASCIIVisualiser, List gameAgents, long calculationTime,
        TimeUnit timeUnit, Logger log, ExecutorService pool) {
      return null;
    }
  },
  ROUND_ROBIN{
    @Override
    public List<Match> getTournamentList(Constructor gameConstructor,
        GameASCIIVisualiser gameASCIIVisualiser, List gameAgents, long calculationTime,
        TimeUnit timeUnit, Logger log, ExecutorService pool) {
      return null;
    }
  },
  DOUBLE_ROUND_ROBIN{
    @Override
    public List<Match> getTournamentList(Constructor gameConstructor,
        GameASCIIVisualiser gameASCIIVisualiser, List gameAgents, long calculationTime,
        TimeUnit timeUnit, Logger log, ExecutorService pool) {
      return null;
    }
  },
  FULL_ROUND_ROBIN{
    @Override
    public List<Match> getTournamentList(Constructor gameConstructor,
        GameASCIIVisualiser gameASCIIVisualiser, List gameAgents, long calculationTime,
        TimeUnit timeUnit, Logger log, ExecutorService pool) {
      return null;
    }
  }


}
