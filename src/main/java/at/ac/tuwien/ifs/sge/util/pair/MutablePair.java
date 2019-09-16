package at.ac.tuwien.ifs.sge.util.pair;

import java.util.Objects;

public class MutablePair<A, B> implements Pair<A, B> {

  private A a;
  private B b;

  public MutablePair() {
    this(null, null);
  }

  public MutablePair(A a, B b) {
    this.a = a;
    this.b = b;
  }

  public MutablePair(Pair<? extends A, ? extends B> pair) {
    this(pair.getA(), pair.getB());
  }


  @Override
  public A getA() {
    return a;
  }

  public MutablePair<A, B> setA(A a) {
    this.a = a;
    return this;
  }

  @Override
  public B getB() {
    return b;
  }

  public MutablePair<A, B> setB(B b) {
    this.b = b;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MutablePair<?, ?> that = (MutablePair<?, ?>) o;
    return Objects.equals(a, that.a) &&
        Objects.equals(b, that.b);
  }

  @Override
  public int hashCode() {
    return Objects.hash(a, b);
  }
}
