package at.ac.tuwien.ifs.sge.engine.factory;

public interface Factory<E> {

  E newInstance(Object... initargs);

}
