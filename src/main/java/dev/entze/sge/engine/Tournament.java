package dev.entze.sge.engine;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.game.Game;
import dev.entze.sge.game.GameASCIIVisualiser;
import dev.entze.sge.util.Pair;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class Tournament<G extends Game<?, ?>, E extends GameAgent<G, ?>> implements
    Callable<List<Pair<List<E>, Double[]>>> {


  private final Constructor<G> gameConstructor;
  private GameASCIIVisualiser<G> gameASCIIVisualiser;
  private List<E> gameAgents;
  private final TournamentListGenerator<G, E> tournamentMode;
  private final long calculationTime;
  private final TimeUnit timeUnit;
  private final Logger log;
  private final ExecutorService pool;

  public Tournament(Constructor<G> gameConstructor,
      GameASCIIVisualiser<G> gameASCIIVisualiser, List<E> gameAgents, TournamentMode tournamentMode,
      long calculationTime,
      TimeUnit timeUnit, Logger log, ExecutorService pool) {
    this.gameConstructor = gameConstructor;
    this.gameASCIIVisualiser = gameASCIIVisualiser;
    this.gameAgents = gameAgents;
    this.tournamentMode = tournamentMode;
    this.calculationTime = calculationTime;
    this.timeUnit = timeUnit;
    this.log = log;
    this.pool = pool;
  }


  @Override
  public List<Pair<List<E>, Double[]>> call() throws Exception {
    List<Match<G, E, ?>> tournamentList = tournamentMode
        .getTournamentList(gameConstructor, gameASCIIVisualiser, gameAgents, calculationTime,
            timeUnit, log, pool);
    List<Pair<List<E>, Double[]>> result = new ArrayList<>(tournamentList.size());

    for (Match<G, E, ?> match : tournamentList) {
      result.add(new Pair<>(match.getGameAgents(), match.call()));
    }

    return result;
  }

}
