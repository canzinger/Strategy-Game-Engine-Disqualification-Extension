package dev.entze.sge.engine.game.tournament;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.engine.Logger;
import dev.entze.sge.engine.factory.GameFactory;
import dev.entze.sge.engine.game.Match;
import dev.entze.sge.engine.game.MatchResult;
import dev.entze.sge.game.Game;
import dev.entze.sge.util.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RoundRobin<G extends Game<? extends A, ?>, E extends GameAgent<G, ? extends A>, A> implements
    Tournament<G, E, A> {

  private List<MatchResult<G, E>> tournamentResult;
  private String textRepresentation;
  private final GameFactory<G> gameFactory;
  private final String board;
  private final List<E> gameAgents;
  private final boolean debug;
  private final long computationTime;
  private final TimeUnit timeUnit;
  private final Logger log;
  private final ExecutorService pool;

  public RoundRobin(GameFactory<G> gameFactory, String board, List<E> gameAgents, boolean debug,
      long computationTime,
      TimeUnit timeUnit, Logger log, ExecutorService pool) {
    this.tournamentResult = null;
    this.textRepresentation = null;
    this.gameFactory = gameFactory;
    this.board = board;
    this.gameAgents = gameAgents;
    this.debug = debug;
    this.computationTime = computationTime;
    this.timeUnit = timeUnit;
    this.log = log;
    this.pool = pool;
  }

  @Override
  public String toTextRepresentation() {
    if (tournamentResult == null) {
      return "Not played yet.";
    } else if (textRepresentation != null) {
      return textRepresentation;
    }
    StringBuilder stringBuilder = new StringBuilder();

    List<String> gameAgentNames = gameAgents.stream().map(Object::toString)
        .collect(Collectors.toList());
    List<Integer> gameAgentWidths = gameAgentNames.stream().map(String::length)
        .collect(Collectors.toList());

    int maxWidth = gameAgentWidths.stream().mapToInt(Integer::intValue).max().orElse(0);

    appendSeperationLine(stringBuilder, gameAgentWidths, maxWidth);
    stringBuilder.append('|').append(' ');

    stringBuilder.append(String.join(" | ", gameAgentNames));
    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
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
        String resultString = Util.convertDoubleToMinimalString(result, 2);
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

  @SuppressWarnings("unchecked")
  @Override
  public List<MatchResult<G, E>> call() {
    if (tournamentResult != null) {
      return Collections.unmodifiableList(tournamentResult);
    }
    tournamentResult = new ArrayList<>();

    for (int x = 0; x < gameAgents.size(); x++) {
      E xAgent = gameAgents.get(x);
      for (int y = x + 1; y < gameAgents.size(); y++) {
        E yAgent = gameAgents.get(y);
        List<E> agentList = List.of(xAgent, yAgent);
        Match<G, E, A> match = new Match<>((Game<A, ?>) gameFactory.newInstance(board, 2),
            agentList, computationTime, timeUnit, debug,
            log, pool);
        MatchResult<G, E> matchResult = match.call();
        tournamentResult.add(matchResult);
      }
    }

    return Collections.unmodifiableList(tournamentResult);
  }
}
