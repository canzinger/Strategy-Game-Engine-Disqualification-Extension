package at.ac.tuwien.ifs.sge.agent;

import at.ac.tuwien.ifs.sge.engine.Logger;
import at.ac.tuwien.ifs.sge.game.ActionRecord;
import at.ac.tuwien.ifs.sge.game.Game;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class HumanAgent<G extends Game<A, ?>, A> implements GameAgent<G, A> {

  private final Logger log;

  public HumanAgent() {
    this(null);
  }

  public HumanAgent(Logger log) {
    this.log = log;
  }

  @Override
  public A computeNextAction(G game, long computationTime, TimeUnit timeUnit) {

    List<ActionRecord<A>> previousActions = game.getActionRecords();
    int lastActionOfThisPlayer = (-1);
    for (int i = previousActions.size() - 1; i >= 0 && lastActionOfThisPlayer < 0; i--) {
      if (previousActions.get(i).getPlayer() == game.getCurrentPlayer()) {
        lastActionOfThisPlayer = i;
      }
    }

    if (lastActionOfThisPlayer < previousActions.size() - 1) {
      System.out.print("Last moves:");
      int lastPlayer = Integer.MIN_VALUE;
      for (int i = lastActionOfThisPlayer + 1; i < previousActions.size(); i++) {
        int player = previousActions.get(i).getPlayer();
        if (player >= 0 && lastPlayer != player) {
          System.out.print("\n(" + i + ") Player " + player + ": ");
        } else if (lastPlayer == player) {
          System.out.print(", ");
        } else {
          System.out.print("\n(" + i + ") World: ");
        }

        System.out.print(previousActions.get(i).getAction().toString());
        lastPlayer = player;

      }
      System.out.println('\n');
    }

    System.out.println("Possible actions:");
    List<A> possibleActions = new ArrayList<>(game.getPossibleActions());

    boolean isInteger = false;

    try {
      Integer test = (Integer) possibleActions.get(0);
      isInteger = true;
    } catch (Exception ignored) {

    }

    if (!isInteger) {
      for (int i = 0; i < possibleActions.size(); i++) {
        System.out.println("[" + i + "] " + possibleActions.get(i).toString());
      }
    } else {
      for (A possibleAction : possibleActions) {
        System.out.println("[" + possibleAction.toString() + "]");
      }
    }

    int move = (-1);
    String input;

    boolean moveInvalid = true;

    while (moveInvalid
        && Thread.currentThread().isAlive()
        && !Thread.currentThread().isInterrupted()) {
      move = (-1);
      input = null;
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
        int matches = 0;
        try {
          move = Integer.parseInt(input);
        } catch (NumberFormatException ignored) {
          for (int i = 0; i < possibleActions.size(); i++) {
            if (possibleActions.get(i).toString().startsWith(input)) {
              matches++;
              move = i;
            }
          }
        }

        if (isInteger) {
          move = possibleActions.indexOf(move);
        }

        //sic!
        if (moveInvalid = !(0 <= move && move < possibleActions.size())) {
          System.err.println("Not a valid action.");
        } else if (matches > 1) {
          System.err.println("Specified input is ambiguous");
          moveInvalid = true;
        }

      }

    }

    return possibleActions.get(move);
  }
}
