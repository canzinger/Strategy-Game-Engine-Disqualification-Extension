package dev.entze.sge.engine;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.game.Game;
import dev.entze.sge.game.GameASCIIVisualiser;
import dev.entze.sge.util.Pair;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public enum TournamentMode implements Tournament {

  SINGLE_ELEMINATION {
    @Override
    public List<Pair<List<GameAgent<Game<?, ?>, ?>>, Double[]>> playTournament(
        Constructor<Game<?, ?>> gameConstructor,
        GameASCIIVisualiser<Game<?, ?>> gameASCIIVisualiser,
        List<GameAgent<Game<?, ?>, ?>> gameAgents,
        long calculationTime,
        TimeUnit timeUnit, Logger log, ExecutorService pool) {

      List<GameAgent<Game<?, ?>, ?>> roster = new ArrayList<>(gameAgents);
      List<Pair<List<GameAgent<Game<?, ?>, ?>>, Double[]>> result = new ArrayList<>();

      Game<?, ?> infoGame = getGame(gameConstructor);
      int numberOfPlayers = infoGame.getNumberOfPlayers();

      Deque<Match<?, ?, ?>> gameDeque = new ArrayDeque<>(gameAgents.size() / numberOfPlayers);

      Collections.shuffle(roster);

      while (roster.size() >= numberOfPlayers) {
        for (int groupRound = 0; groupRound < gameAgents.size() / numberOfPlayers;
            groupRound += numberOfPlayers) {

          List<GameAgent<?, ?>> group = new ArrayList<>(numberOfPlayers);
          for (int i = 0; i < numberOfPlayers; i++) {
            group.add(roster.get(groupRound + i));
          }

          gameDeque
              .add(new Match(getGame(gameConstructor), gameASCIIVisualiser, group, calculationTime,
                  timeUnit, log, pool));

        }

        while (!gameDeque.isEmpty()) {
          Match<?, ?, ?> match = gameDeque.removeFirst();
          Double[] matchResult = match.call();
          result.add(new Pair
              (match.getGameAgents(), matchResult));
          List<GameAgent<Game<?, ?>, ?>> toRemove = (List<GameAgent<Game<?, ?>, ?>>) match
              .getGameAgents();
          double max = Collections.max(Arrays.asList(matchResult));
          for (int i = 0; i < matchResult.length; i++) {
            if (matchResult[i] >= max) {
              toRemove.remove(i);
            }
          }

          roster.removeAll(toRemove);
        }
      }

      return result;
    }

    @Override
    public String visualiseTournament(
        List<Pair<List<GameAgent<Game<?, ?>, ?>>, Double[]>> tournament) {

      StringBuilder stringBuilder = new StringBuilder();

      stringBuilder.append("Winner");
      List<GameAgent<Game<?, ?>, ?>> finalists = tournament.get(tournament.size() - 1).getA();
      Double[] result = tournament.get(tournament.size() - 1).getB();
      Double max = Collections.max(Arrays.asList(result));
      List<GameAgent<Game<?, ?>, ?>> winners = new ArrayList<>(1);
      for (int i = 0; i < result.length; i++) {
        if (result[i] >= max) {
          winners.add(finalists.get(i));
        }
      }

      if (winners.size() > 1) {
        stringBuilder.append('s');
      }

      stringBuilder.append(": ");

      for (GameAgent<Game<?, ?>, ?> winner : winners) {
        stringBuilder.append(winner).append(", ");
      }

      stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length() - 1);

      return stringBuilder.toString();
    }
  };


  public static Game<?, ?> getGame(Constructor<Game<?, ?>> gameConstructor) {
    try {
      return gameConstructor.newInstance();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new IllegalArgumentException("Could not use Constructor to instantiate game");
    }
  }

}
