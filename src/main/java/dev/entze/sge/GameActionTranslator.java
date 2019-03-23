package dev.entze.sge;

public interface GameActionTranslator<T> {

  T translate(int action);
  int translate(T action);

}
