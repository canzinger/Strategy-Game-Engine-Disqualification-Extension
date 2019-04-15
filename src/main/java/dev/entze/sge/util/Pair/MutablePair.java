package dev.entze.sge.util.Pair;

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
}
