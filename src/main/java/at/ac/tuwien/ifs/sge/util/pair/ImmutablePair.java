package at.ac.tuwien.ifs.sge.util.pair;

import java.util.Objects;

/**
 * A pair of allValues. Accessible via getA and getB
 *
 * @param <A> - the first
 * @param <B> - the second
 */
public class ImmutablePair<A, B> implements Pair<A, B> {

  private final A a;
  private final B b;

  public ImmutablePair(A a, B b) {
    this.a = a;
    this.b = b;
  }

  public ImmutablePair(Pair<? extends A, ? extends B> pair) {
    this(pair.getA(), pair.getB());
  }


  public A getA() {
    return a;
  }

  public B getB() {
    return b;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImmutablePair<?, ?> that = (ImmutablePair<?, ?>) o;
    return a.equals(that.a) &&
        b.equals(that.b);
  }

  @Override
  public int hashCode() {
    return Objects.hash(a, b);
  }
}
