package dev.entze.sge.engine.loader;

import dev.entze.sge.game.Game;
import dev.entze.sge.game.GameASCIIVisualiser;
import dev.entze.sge.util.Pair;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;

public class GameLoader implements
    Callable<Pair<Constructor<Game<?, ?>>, GameASCIIVisualiser<Game<?, ?>>>> {

  private final String gameClassName;
  private final String gameASCIIVisualiserClassName;

  private final ClassLoader classLoader;

  public GameLoader(String gameClassName, String gameASCIIVisualiserClassName, ClassLoader classLoader) {
    this.classLoader = classLoader;
    this.gameClassName = gameClassName;
    this.gameASCIIVisualiserClassName = gameASCIIVisualiserClassName;
  }

  @Override
  public Pair<Constructor<Game<?, ?>>, GameASCIIVisualiser<Game<?, ?>>> call()
      throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Class<Game<?, ?>> gameClass = (Class<Game<?, ?>>) classLoader.loadClass(gameClassName);
    Constructor<Game<?, ?>> gameConstructor = gameClass.getConstructor();

    Class<GameASCIIVisualiser<Game<?, ?>>> gameASCIIVisualiserClass = (Class<GameASCIIVisualiser<Game<?, ?>>>) classLoader
        .loadClass(gameASCIIVisualiserClassName);
    Constructor<GameASCIIVisualiser<Game<?, ?>>> gameASCIIVisualiserConstructor = gameASCIIVisualiserClass
        .getConstructor();

    return new Pair<>(gameConstructor, gameASCIIVisualiserConstructor.newInstance());
  }

}
