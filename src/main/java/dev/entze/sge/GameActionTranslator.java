package dev.entze.sge;

public interface GameActionTranslator<E> {

  E translate(int action);

  int translate(E action);

}
