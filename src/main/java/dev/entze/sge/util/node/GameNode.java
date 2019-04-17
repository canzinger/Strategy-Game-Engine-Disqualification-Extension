package dev.entze.sge.util.node;

import dev.entze.sge.game.Game;

public interface GameNode<A> {


  Game<A, ?> getGame();

  void setGame(Game<A, ?> game);

}
