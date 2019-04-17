package dev.entze.sge.engine.loader;

import dev.entze.sge.engine.Logger;
import dev.entze.sge.engine.factory.GameFactory;
import dev.entze.sge.game.Game;
import dev.entze.sge.game.GameASCIIVisualiser;
import dev.entze.sge.util.pair.ImmutablePair;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;

public class GameLoader implements
    Callable<ImmutablePair<GameFactory, GameASCIIVisualiser<Game<Object, Object>>>> {

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

  @SuppressWarnings("unchecked")
  @Override
  public ImmutablePair<GameFactory, GameASCIIVisualiser<Game<Object, Object>>> call()
      throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Class<Game<Object, Object>> gameClass = (Class<Game<Object, Object>>) classLoader
        .loadClass(gameClassName);
    Constructor<Game<Object, Object>> gameConstructor = gameClass
        .getConstructor(String.class, int.class);
    Constructor<Game<Object, Object>> gameConstructorWithoutPlayerNumber = gameClass
        .getConstructor();
    Game<?, ?> testGame = gameConstructorWithoutPlayerNumber.newInstance();

    Class<GameASCIIVisualiser<Game<Object, Object>>> gameASCIIVisualiserClass = (Class<GameASCIIVisualiser<Game<Object, Object>>>) classLoader
        .loadClass(gameASCIIVisualiserClassName);
    Constructor<GameASCIIVisualiser<Game<Object, Object>>> gameASCIIVisualiserConstructor = gameASCIIVisualiserClass
        .getConstructor();

    return new ImmutablePair<>(
        new GameFactory(gameConstructor, testGame.getMinimumNumberOfPlayers(),
            testGame.getMaximumNumberOfPlayers(), log),
        gameASCIIVisualiserConstructor.newInstance());
  }

}
