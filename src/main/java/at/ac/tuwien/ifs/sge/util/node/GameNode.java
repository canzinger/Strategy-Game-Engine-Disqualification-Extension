package at.ac.tuwien.ifs.sge.util.node;

import at.ac.tuwien.ifs.sge.game.Game;

public interface GameNode<A> {


  Game<A, ?> getGame();

  void setGame(Game<A, ?> game);

}
