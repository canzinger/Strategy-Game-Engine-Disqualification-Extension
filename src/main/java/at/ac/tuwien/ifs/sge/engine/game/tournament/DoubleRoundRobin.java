package at.ac.tuwien.ifs.sge.engine.game.tournament;

import at.ac.tuwien.ifs.sge.agent.GameAgent;
import at.ac.tuwien.ifs.sge.engine.Logger;
import at.ac.tuwien.ifs.sge.engine.factory.GameFactory;
import at.ac.tuwien.ifs.sge.engine.game.Match;
import at.ac.tuwien.ifs.sge.engine.game.MatchResult;
import at.ac.tuwien.ifs.sge.game.Game;
import at.ac.tuwien.ifs.sge.util.Util;
import at.ac.tuwien.ifs.sge.util.pair.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DoubleRoundRobin<G extends Game<? extends A, ?>, E extends GameAgent<G, ? extends A>, A> extends
    AbstractTournament<G, E, A> implements
    Tournament<G, E, A> {

  private Map<String, Map<String, Double>> twoResult;
  private Map<String, Pair<Double, Double>> result;


  public DoubleRoundRobin(GameFactory<G> gameFactory, int numberOfPlayers, String board,
      List<E> gameAgents, long computationTime, TimeUnit timeUnit, boolean debug, Logger log,
      ExecutorService pool, int maxActions) {
    super(gameFactory, numberOfPlayers, board, gameAgents, computationTime, timeUnit, debug, log,
        pool, maxActions);
    this.result = null;
    this.twoResult = null;
  }

  @Override
  public List<MatchResult<G, E>> call() {
    if (tournamentResult == null) {

      int gameAgentsSize = gameAgents.size();
      int numberOfGames = Util.nPr(gameAgentsSize, numberOfPlayers);
      tournamentResult = new ArrayList<>(numberOfGames);
      log.info("Starting Double Round Robin tournament with " + gameAgentsSize + " contestant" + (
          gameAgentsSize == 1 ? "" : "s") + ", " + numberOfGames + " game" + (numberOfGames == 1
          ? "" : "s") + ".");
      log.debug(
          "Agents: " + gameAgents.stream().map(Objects::toString)
              .collect(Collectors.joining(", ")));

      if (numberOfPlayers == 2) {
        twoTournament();
      } else {
        tournament();
      }
    }
    return super.call();
  }

  private void twoTournament() {

    for (int x = 0; x < gameAgents.size(); x++) {
      for (int y = 0; y < gameAgents.size(); y++) {
        if (x != y) {
          E xAgent = gameAgents.get(x);
          E yAgent = gameAgents.get(y);
          Match<G, E, A> match = new Match<>(newInstanceOfGame(), List.of(xAgent, yAgent),
              computationTime, timeUnit, debug, log, pool, maxActions);
          MatchResult<G, E> matchResult = match.call();
          tournamentResult.add(matchResult);
        }
      }
    }


  }

  private void tournament() {

    final int n = gameAgents.size();
    int[] indices = new int[numberOfPlayers];
    for (int i = 0; i < numberOfPlayers; i++) {
      indices[i] = (numberOfPlayers - 1) - i;
    }

    do {

      List<E> agentList = Arrays.stream(indices).mapToObj(gameAgents::get)
          .collect(Collectors.toList());
      Match<G, E, A> match = new Match<>(newInstanceOfGame(), agentList, computationTime, timeUnit,
          debug, log, pool, maxActions);
      MatchResult<G, E> matchResult = match.call();
      tournamentResult.add(matchResult);

      indices = Util.permutations(indices, n);
    } while (Util.isReverseIndexEqualToValue(indices));

  }


  @Override
  public String toTextRepresentation() {
    if (textRepresentation == null) {
      if (tournamentResult == null) {
        return "not played";
      }

      if (numberOfPlayers == 2) {
        if (twoResult == null) {
          twoResult = RoundRobin.resultAsTwoTable(tournamentResult);
        }
        textRepresentation = RoundRobin.twoTableToString(twoResult);
      } else {
        if (result == null) {
          result = Tournament.resultAsTable(tournamentResult);
        }
        textRepresentation = Tournament.tableToString(result);
      }

    }
    return super.toTextRepresentation();
  }

}
