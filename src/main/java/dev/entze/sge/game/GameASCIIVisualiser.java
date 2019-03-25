package dev.entze.sge.game;

public interface GameASCIIVisualiser<G extends Game<?, ?>> {

  String visualise(G game);

}
