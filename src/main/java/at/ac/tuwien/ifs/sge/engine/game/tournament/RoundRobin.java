package at.ac.tuwien.ifs.sge.engine.game.tournament;

import at.ac.tuwien.ifs.sge.agent.GameAgent;
import at.ac.tuwien.ifs.sge.engine.Logger;
import at.ac.tuwien.ifs.sge.engine.factory.GameFactory;
import at.ac.tuwien.ifs.sge.engine.game.Match;
import at.ac.tuwien.ifs.sge.engine.game.MatchResult;
import at.ac.tuwien.ifs.sge.game.Game;
import at.ac.tuwien.ifs.sge.util.Util;
import at.ac.tuwien.ifs.sge.util.pair.ImmutablePair;
import at.ac.tuwien.ifs.sge.util.pair.MutablePair;
import at.ac.tuwien.ifs.sge.util.pair.Pair;
import com.google.common.math.IntMath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RoundRobin<G extends Game<? extends A, ?>, E extends GameAgent<G, ? extends A>, A> extends
    AbstractTournament<G, E, A> implements
    Tournament<G, E, A> {

  private Map<String, Map<String, Double>> twoResult;
  private Map<String, Pair<Double, Double>> result;

  public RoundRobin(GameFactory<G> gameFactory, int numberOfPlayers, String board,
      List<E> gameAgents,
      long computationTime, TimeUnit timeUnit, boolean debug, Logger log, ExecutorService pool, int maxActions) {
    super(gameFactory, numberOfPlayers, board, gameAgents, computationTime, timeUnit, debug, log,
        pool, maxActions);
    this.result = null;
    this.twoResult = null;
  }

  public static <G extends Game<? extends A, ?>, E extends GameAgent<G, ? extends A>, A> Map<String, Map<String, Double>> resultAsTwoTable(
      List<MatchResult<G, E>> tournamentResult) {
    Map<String, Map<String, Double>> twoResult = new HashMap<>();

    for (MatchResult<G, E> matchResult : tournamentResult) {
      final List<E> agents = matchResult.getGameAgents();
      final double[] results = matchResult.getResult();
      final double result = results[0] - results[1];
      final String xAgent = agents.get(0).toString();
      final String yAgent = agents.get(1).toString();
      twoResult.compute(xAgent, (k, v) -> {
        if (v == null) {
          v = new HashMap<>();
        }
        v.put(yAgent, result);
        return v;
      });
    }

    return twoResult;
  }


  public static String twoTableToString(Map<String, Map<String, Double>> twoTable) {
    StringBuilder stringBuilder = new StringBuilder();

    List<String> gameAgentNames = twoTable.entrySet().stream().sorted(
        Comparator.comparingInt(o -> o.getValue().size())).map(Entry::getKey)
        .collect(Collectors.toUnmodifiableList());
    List<Integer> gameAgentWidths = gameAgentNames.stream().map(String::length)
        .collect(Collectors.toList());
    int maxWidth = gameAgentWidths.stream().mapToInt(Integer::intValue).max().orElse(0);

    appendSeperationLine(stringBuilder, gameAgentWidths, maxWidth).append('|');
    Util.appendNTimes(stringBuilder, ' ', maxWidth + 2);
    stringBuilder.append('|').append(' ').append(String.join(" | ", gameAgentNames)).append(' ')
        .append('|').append('\n');
    appendSeperationLine(stringBuilder, gameAgentWidths, maxWidth);

    for (int y = 0; y < gameAgentNames.size(); y++) {
      String yAgentName = gameAgentNames.get(y);
      stringBuilder.append('|');
      Util.appendWithBlankBuffer(stringBuilder, yAgentName, maxWidth + 2).append('|');
      for (int x = 0; x < gameAgentNames.size(); x++) {
        String xAgentName = gameAgentNames.get(x);
        if (twoTable.containsKey(yAgentName) && twoTable.get(yAgentName).containsKey(xAgentName)) {
          stringBuilder.append(' ');
          Util.appendWithBlankBuffer(stringBuilder, twoTable.get(yAgentName).get(xAgentName),
              gameAgentWidths.get(x)).append(' ');
        } else {
          Util.appendNTimes(stringBuilder, ' ', gameAgentWidths.get(x) + 2);
        }
        stringBuilder.append('|');
      }
      stringBuilder.append('\n');
    }
    appendSeperationLine(stringBuilder, gameAgentWidths, maxWidth)
        .deleteCharAt(stringBuilder.length() - 1);
    return stringBuilder.toString();
  }

  @Override
  public String toTextRepresentation() {
    if (tournamentResult == null) {
      return "not played";
    } else if (textRepresentation != null) {
      return textRepresentation;
    } else if (numberOfPlayers == 2) {
      if (twoResult == null) {
        twoResult = resultAsTwoTable(tournamentResult);
      }
      textRepresentation = twoTableToString(twoResult);
    } else {
      if (result == null) {
        result = Tournament.resultAsTable(tournamentResult);
      }
      textRepresentation = Tournament.tableToString(result);
    }

    return textRepresentation;
  }

  private static StringBuilder appendSeperationLine(StringBuilder stringBuilder,
      List<Integer> widths, int maxWidth) {

    stringBuilder.append('|');
    Util.appendNTimes(stringBuilder, '-', maxWidth + 2).append('+');

    for (Integer width : widths) {
      Util.appendNTimes(stringBuilder, '-', width + 2).append('+');
    }

    stringBuilder.deleteCharAt(stringBuilder.length() - 1).append('|').append('\n');
    return stringBuilder;
  }

  @Override
  public List<MatchResult<G, E>> call() {
    if (tournamentResult != null) {
      return Collections.unmodifiableList(tournamentResult);
    }
    tournamentResult = new ArrayList<>();

    int gameAgentsSize = gameAgents.size();
    int numberOfGames = IntMath.binomial(gameAgentsSize, numberOfPlayers);
    log.info("Starting Round Robin tournament with " + gameAgentsSize + " contestants, "
        + numberOfGames + " game" + (numberOfGames == 1 ? "" : "s") + ".");
    log.debug(
        "Agents: " + gameAgents.stream().map(Objects::toString).collect(Collectors.joining(", ")));

    if (numberOfPlayers == 2) {
      twoResult = gameAgents.stream()
          .collect(Collectors.toMap(Objects::toString, a -> new HashMap<>()));
      twoTournament();
    } else {
      result = gameAgents.stream()
          .collect(Collectors.toMap(Objects::toString, a -> new MutablePair<>(0D, 0D)));
      tournament();
    }

    return Collections.unmodifiableList(tournamentResult);
  }

  private List<MatchResult<G, E>> twoTournament() {
    for (int x = 0; x < gameAgents.size(); x++) {
      E xAgent = gameAgents.get(x);
      for (int y = x + 1; y < gameAgents.size(); y++) {
        E yAgent = gameAgents.get(y);
        List<E> agentList = List.of(xAgent, yAgent);
        Match<G, E, A> match = new Match<>(newInstanceOfGame(), agentList, computationTime,
            timeUnit, debug, log, pool, maxActions);
        MatchResult<G, E> matchResult = match.call();
        double[] evenResult = matchResult.getResult();
        twoResult.get(xAgent.toString()).put(yAgent.toString(), evenResult[0] - evenResult[1]);
        tournamentResult.add(matchResult);
      }
    }
    return tournamentResult;
  }

  private List<MatchResult<G, E>> tournament() {
    int[] indices = new int[numberOfPlayers];

    for (int i = 0; i < indices.length; i++) {
      indices[i] = i;
    }

    do {
      List<E> agentList = Arrays.stream(indices).mapToObj(gameAgents::get)
          .collect(Collectors.toUnmodifiableList());
      Match<G, E, A> match = new Match<>(newInstanceOfGame(), agentList, computationTime, timeUnit,
          debug, log, pool, maxActions);
      MatchResult<G, E> matchResult = match.call();
      double[] utilities = matchResult.getResult();
      double[] scores = Util.scoresOutOfUtilities(utilities);
      for (int i = 0; i < agentList.size(); i++) {
        String agentName = agentList.get(i).toString();
        final double score = scores[i];
        final double utility = utilities[i];
        result.compute(agentName, (k, v) -> v == null ? new ImmutablePair<>(score, utility)
            : new ImmutablePair<>(v.getA() + score, v.getB() + utility));
      }
      indices = Util.combinations(indices);
    } while (indices[numberOfPlayers - 1] < gameAgents.size());

    return tournamentResult;
  }


}
