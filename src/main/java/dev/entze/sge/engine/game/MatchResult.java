package dev.entze.sge.engine.game;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.game.Game;
import java.util.Collection;
import java.util.List;

public class MatchResult<G extends Game<?, ?>, E extends GameAgent<G, ?>> {

  private final List<E> gameAgents;
  private final long duration;
  private final double[] result;

  public MatchResult(Collection<E> gameAgents, long startTime, long endTime, double[] result) {
    this(gameAgents, endTime - startTime, result);
  }

  public MatchResult(Collection<E> gameAgents, long duration, double[] result) {
    this.gameAgents = List.copyOf(gameAgents);
    this.duration = duration;
    this.result = result;
  }

  public List<E> getGameAgents() {
    return gameAgents;
  }

  public long getDuration() {
    return duration;
  }

  public double[] getResult() {
    return result;
  }

}
