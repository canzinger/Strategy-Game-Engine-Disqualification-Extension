package dev.entze.sge.engine.game.tournament;

import com.google.common.math.IntMath;
import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.engine.Logger;
import dev.entze.sge.engine.factory.GameFactory;
import dev.entze.sge.engine.game.Match;
import dev.entze.sge.engine.game.MatchResult;
import dev.entze.sge.game.Game;
import dev.entze.sge.util.Util;
import dev.entze.sge.util.pair.MutablePair;
import dev.entze.sge.util.pair.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RoundRobin<G extends Game<? extends A, ?>, E extends GameAgent<G, ? extends A>, A> implements
    Tournament<G, E, A> {

  private List<MatchResult<G, E>> tournamentResult;
  private String textRepresentation;
  private Map<String, Map<String, Double>> twoResult;
  private Map<String, Pair<Double, Double>> result;

  private final GameFactory<G> gameFactory;
  private final int numberOfPlayers;
  private final String board;
  private final List<E> gameAgents;
  private final boolean debug;
  private final long computationTime;
  private final TimeUnit timeUnit;
  private final Logger log;
  private final ExecutorService pool;


  public RoundRobin(GameFactory<G> gameFactory, int numberOfPlayers, String board,
      List<E> gameAgents,
      long computationTime, TimeUnit timeUnit, boolean debug, Logger log, ExecutorService pool) {
    this.tournamentResult = null;
    this.textRepresentation = null;
    this.gameFactory = gameFactory;
    this.numberOfPlayers = numberOfPlayers;
    this.board = board;
    this.gameAgents = gameAgents;
    this.debug = debug;
    this.computationTime = computationTime;
    this.timeUnit = timeUnit;
    this.log = log;
    this.pool = pool;
    this.result = null;
    this.twoResult = null;
  }

  public static <G extends Game<? extends A, ?>, E extends GameAgent<G, ? extends A>, A> Map<String, Map<String, Double>> resultAsTwoTable(
      List<MatchResult<G, E>> tournamentResult) {
    Map<String, Map<String, Double>> twoResult = new HashMap<>();

    return twoResult;
  }

  public static <G extends Game<? extends A, ?>, E extends GameAgent<G, ? extends A>, A> Map<String, Pair<Double, Double>> resultAsTable(
      List<MatchResult<G, E>> tournamentResult) {
    Map<String, Pair<Double, Double>> result = new HashMap<>();

    return result;
  }

  @Override
  public String toTextRepresentation() {
    if (tournamentResult == null) {
      return "Not played yet.";
    } else if (textRepresentation != null) {
      return textRepresentation;
    } else if (numberOfPlayers == 2 && twoResult == null) {

    }
    StringBuilder stringBuilder = new StringBuilder();

    List<String> gameAgentNames = gameAgents.stream().map(Object::toString)
        .collect(Collectors.toList());
    List<Integer> gameAgentWidths = gameAgentNames.stream().map(String::length)
        .collect(Collectors.toList());

    int maxWidth = gameAgentWidths.stream().mapToInt(Integer::intValue).max().orElse(0);

    appendSeperationLine(stringBuilder, gameAgentWidths, maxWidth);
    stringBuilder.append('|').append(' ').append(String.join(" | ", gameAgentNames)).append(' ')
        .append('|').append('\n');
    appendSeperationLine(stringBuilder, gameAgentWidths, maxWidth);

    for (int x = 0; x < gameAgents.size(); x++) {
      for (int y = 0; y < x; y++) {
        int width = gameAgentWidths.get(y) + 2;
        final int finalX = x;
        final int finalY = y;
        MatchResult<G, E> matchResult = tournamentResult.stream().filter(
            r -> r.getGameAgents().get(0).equals(gameAgents.get(finalX))
                && r.getGameAgents().get(1).equals(gameAgents.get(finalY))).findAny()
            .orElseThrow();

        double[] results = matchResult.getResult();
        double result = results[0] - results[1];
        String resultString = Util.convertDoubleToMinimalString(result, width - 2);
        width -= resultString.length();

        Util.appendNTimes(stringBuilder, ' ', width / 2 + (width % 2)).append(resultString);
        Util.appendNTimes(stringBuilder, ' ', width / 2).append('|');
      }
      appendNSkips(stringBuilder, gameAgentWidths, gameAgents.size() - x);
      stringBuilder.append('\n');
      appendSeperationLine(stringBuilder, gameAgentWidths, maxWidth);
    }
    textRepresentation = stringBuilder.toString();
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

  private static StringBuilder appendNSkips(StringBuilder stringBuilder, List<Integer> widths,
      int n) {
    if (n == 0) {
      return stringBuilder;
    }
    stringBuilder.append('|');
    for (int i = 0; i < n; i++) {
      Util.appendNTimes(stringBuilder, ' ', widths.get(i) + 2).append('|');
    }
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
            timeUnit, debug, log, pool);
        MatchResult<G, E> matchResult = match.call();
        double[] evenResult = matchResult.getResult();
        twoResult.get(xAgent.toString()).put(yAgent.toString(), evenResult[0] - evenResult[1]);
        tournamentResult.add(matchResult);
      }
    }
    return tournamentResult;
  }

  private List<MatchResult<G, E>> tournament() {

    return tournamentResult;
  }

  @SuppressWarnings("unchecked")
  private Game<A, ?> newInstanceOfGame() {
    return (Game<A, ?>) gameFactory.newInstance(board, numberOfPlayers);
  }

}
