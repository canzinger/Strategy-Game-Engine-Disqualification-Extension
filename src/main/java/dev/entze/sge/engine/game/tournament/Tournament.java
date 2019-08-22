package dev.entze.sge.engine.game.tournament;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.engine.game.MatchResult;
import dev.entze.sge.game.Game;
import dev.entze.sge.util.Util;
import dev.entze.sge.util.pair.ImmutablePair;
import dev.entze.sge.util.pair.Pair;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public interface Tournament<G extends Game<? extends A, ?>, E extends GameAgent<G, ? extends A>, A> extends
    Callable<List<MatchResult<G, E>>> {

  default String toTextRepresentation() {
    try {
      return tableToString(resultAsTable(call()));
    } catch (Exception ignored) {

    }
    return null;
  }


  static String tableToString(Map<String, Pair<Double, Double>> table) {
    StringBuilder stringBuilder = new StringBuilder();

    Comparator<Pair<Double, Double>> comparatorA = Comparator.comparingDouble(Pair::getA);
    Comparator<Pair<Double, Double>> comparatorB = Comparator.comparingDouble(Pair::getB);
    Comparator<Entry<String, Pair<Double, Double>>> comparator = Comparator
        .comparing(Entry::getValue, comparatorA.reversed().thenComparing(comparatorB.reversed()));
    List<String> agentNames = table.entrySet().stream().sorted(comparator).map(Entry::getKey)
        .collect(Collectors.toUnmodifiableList());
    List<Integer> agentNamesWidth = agentNames.stream().map(String::length)
        .collect(Collectors.toUnmodifiableList());

    stringBuilder.append('|').append(' ').append(String.join(" | ", agentNames)).append(' ')
        .append('|').append('\n');

    stringBuilder.append('|');
    for (Integer size : agentNamesWidth) {
      Util.appendNTimes(stringBuilder, '-', size + 2).append('+');
    }
    stringBuilder.deleteCharAt(stringBuilder.length() - 1).append('|').append('\n');

    //TODO: score utility

    return stringBuilder.toString();
  }


  static <G extends Game<? extends A, ?>, E extends GameAgent<G, ? extends A>, A> Map<String, Pair<Double, Double>> resultAsTable(
      List<MatchResult<G, E>> tournamentResult) {
    Map<String, Pair<Double, Double>> table = new HashMap<>();

    for (MatchResult<G, E> matchResult : tournamentResult) {
      List<E> gameAgents = matchResult.getGameAgents();
      final double[] utilities = matchResult.getResult();
      final double[] scores = Util.scoreOutOfUtility(utilities);

      for (int i = 0; i < gameAgents.size(); i++) {
        final String gameAgentName = gameAgents.get(i).toString();
        final double utility = utilities[i];
        final double score = scores[i];
        table.compute(gameAgentName, (k, v) -> v == null ? new ImmutablePair<>(score, utility)
            : new ImmutablePair<>(v.getA() + score, v.getB() + utility));
      }

    }

    return table;
  }
}
