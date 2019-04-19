package dev.entze.sge.util.graph;

import java.util.concurrent.Callable;
import org.jgrapht.Graph;

public class GraphReader<E, V> implements Callable<Graph<E, V>>{

  private static final String graphInfoPattern = "";

  private final String graphString;
  private Graph<V, E> graph;

  public GraphReader(String graphString) {
    this.graphString = graphString;
  }

  @Override
  public Graph<E, V> call() throws Exception {

    return null;
  }
}
