package dev.entze.sge.game;

public interface GameActionTranslator<T> {

  T translate(int action);

  int translate(T action);

}
