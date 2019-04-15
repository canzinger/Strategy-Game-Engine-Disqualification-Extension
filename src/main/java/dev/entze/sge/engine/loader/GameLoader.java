package dev.entze.sge.engine.loader;

import dev.entze.sge.engine.Logger;
import dev.entze.sge.engine.factory.GameFactory;
import dev.entze.sge.game.Game;
import dev.entze.sge.game.GameASCIIVisualiser;
import dev.entze.sge.util.Pair;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;

public class GameLoader implements
    Callable<Pair<GameFactory, GameASCIIVisualiser<Game<?, ?>>>> {

  private final String gameClassName;
  private final String gameASCIIVisualiserClassName;

  private final ClassLoader classLoader;

  private final Logger log;

  public GameLoader(String gameClassName, String gameASCIIVisualiserClassName,
      ClassLoader classLoader, Logger log) {
    this.classLoader = classLoader;
    this.gameClassName = gameClassName;
    this.gameASCIIVisualiserClassName = gameASCIIVisualiserClassName;
    this.log = log;
  }

  @Override
  public Pair<GameFactory, GameASCIIVisualiser<Game<?, ?>>> call()
      throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Class<Game<?, ?>> gameClass = (Class<Game<?, ?>>) classLoader.loadClass(gameClassName);
    Constructor<Game<?, ?>> gameConstructor = gameClass.getConstructor(int.class);
    Constructor<Game<?, ?>> gameConstructorWithoutPlayerNumber = gameClass.getConstructor();
    Game<?, ?> testGame = gameConstructorWithoutPlayerNumber.newInstance();

    Class<GameASCIIVisualiser<Game<?, ?>>> gameASCIIVisualiserClass = (Class<GameASCIIVisualiser<Game<?, ?>>>) classLoader
        .loadClass(gameASCIIVisualiserClassName);
    Constructor<GameASCIIVisualiser<Game<?, ?>>> gameASCIIVisualiserConstructor = gameASCIIVisualiserClass
        .getConstructor();

    return new Pair<>(new GameFactory(gameConstructor, testGame.getMinimumNumberOfPlayers(),
        testGame.getMaximumNumberOfPlayers(), log), gameASCIIVisualiserConstructor.newInstance());
  }

}
