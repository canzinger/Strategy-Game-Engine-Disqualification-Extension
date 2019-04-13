package dev.entze.sge.engine.factory;

public interface Factory<E> {

  E newInstance(Object... initargs);

}
