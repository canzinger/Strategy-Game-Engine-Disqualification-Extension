package dev.entze.sge.game;

public interface GameBoardTranslator<T> {

  T translate(int[] board);

  int[] translate(T board);

}
