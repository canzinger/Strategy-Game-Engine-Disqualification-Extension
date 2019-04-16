package dev.entze.sge.agent;

import dev.entze.sge.game.Game;
import dev.entze.sge.util.pair.ImmutablePair;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class HumanAgent<G extends Game<A, ?>, A> implements GameAgent<G, A> {

  @Override
  public A computeNextAction(G game, long computationTime, TimeUnit timeUnit) {

    List<ImmutablePair<Integer, A>> previousActions = game.getPreviousActions();
    int lastActionOfThisPlayer = (-1);
    for (int i = previousActions.size() - 1; i >= 0 && lastActionOfThisPlayer < 0; i--) {
      if (previousActions.get(i).getA() == game.getCurrentPlayer()) {
        lastActionOfThisPlayer = i;
      }
    }

    if (lastActionOfThisPlayer < previousActions.size() - 1) {
      System.out.println("Last moves:");
      for (int i = lastActionOfThisPlayer + 1; i < previousActions.size(); i++) {
        int player = previousActions.get(i).getA();
        if (player >= 0) {
          System.out.print("(" + i + ") Player " + player + ": ");
        } else {
          System.out.print("(" + i + ") World: ");
        }
        System.out.println(previousActions.get(i).getB().toString() + '\n');
      }
    }

    System.out.println("Possible actions:");
    List<A> possibleActions = new ArrayList<>(game.getPossibleActions());

    for (int i = 0; i < possibleActions.size(); i++) {
      System.out.println("[" + i + "] " + possibleActions.get(i).toString());
    }

    int move = (-1);
    String input = null;

    while (!(0 <= move && move < possibleActions.size())) {
      System.out.print('\n' + "> ");
      if (System.console() != null) {
        input = System.console().readLine();
      } else {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
          input = bufferedReader.readLine();
        } catch (IOException e) {
          System.err.println("Could not read input.");
        }
      }

      if (input != null) {
        try {
          move = Integer.parseInt(input);
        } catch (NumberFormatException ignored) {
        }

        if (!(0 <= move && move < possibleActions.size())) {
          System.err.println("Not a valid number.");
        }
      }

    }

    return possibleActions.get(move);
  }
}
