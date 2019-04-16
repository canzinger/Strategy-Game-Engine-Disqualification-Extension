package dev.entze.sge.game;

import java.util.Objects;

public class ActionRecord<A> {

  private final int player;
  private final A action;

  public ActionRecord(ActionRecord<A> a){
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
}
