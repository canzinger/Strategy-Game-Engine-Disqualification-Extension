package dev.entze.sge.game;

import java.util.List;
import java.util.Objects;

public class ActionRecord<A> {

  private final int player;
  private final A action;

  public ActionRecord(ActionRecord<A> a) {
    this(a.player, a.action);
  }

  public ActionRecord(int player, A action) {
    this.player = player;
    this.action = action;
  }

  public int getPlayer() {
    return player;
  }

  public A getAction() {
    return action;
  }

  @Override
  public String toString() {
    return "<" + player + ", " + action + '>';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActionRecord<?> that = (ActionRecord<?>) o;
    return player == that.player &&
        action.equals(that.action);
  }

  @Override
  public int hashCode() {
    return Objects.hash(player, action);
  }


  public static <A> String iterableToString(Iterable<ActionRecord<A>> actionRecords,
      int lastPlayer) {
    StringBuilder builder = new StringBuilder();

    boolean flushPlayer = false;

    for (ActionRecord<A> actionRecord : actionRecords) {
      if (actionRecord.getPlayer() != lastPlayer) {
        if (flushPlayer) {
          builder.append("> ");
        }
        builder.append('<');
        if (actionRecord.getPlayer() >= 0) {
          builder.append(actionRecord.getPlayer()).append(',');
        }
        flushPlayer = true;

      }

      builder.append(" [").append(actionRecord.getAction().toString()).append(']');
      lastPlayer = actionRecord.getPlayer();

    }

    builder.append('>');

    return builder.toString();
  }

  public static <A> String iterableToString(List<ActionRecord<A>> actionRecords) {
    return iterableToString(actionRecords, actionRecords.get(actionRecords.size() - 1).getPlayer());
  }

}
