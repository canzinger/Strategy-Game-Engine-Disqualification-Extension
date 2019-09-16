package at.ac.tuwien.ifs.sge.engine.loader;

import at.ac.tuwien.ifs.sge.engine.Logger;
import at.ac.tuwien.ifs.sge.engine.factory.GameFactory;
import at.ac.tuwien.ifs.sge.game.Game;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;

public class GameLoader<G extends Game<?, ?>> implements
    Callable<GameFactory<G>> {

  private final String gameClassName;

  private final ClassLoader classLoader;

  private final Logger log;

  public GameLoader(String gameClassName, ClassLoader classLoader, Logger log) {
    this.classLoader = classLoader;
    this.gameClassName = gameClassName;
    this.log = log;
  }

  @SuppressWarnings("unchecked")
  @Override
  public GameFactory<G> call()
      throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Class<Game<Object, Object>> gameClass = (Class<Game<Object, Object>>) classLoader
        .loadClass(gameClassName);
    Constructor<Game<Object, Object>> gameConstructor = gameClass
        .getConstructor(String.class, int.class);
    Constructor<Game<Object, Object>> gameConstructorWithoutPlayerNumber = gameClass
        .getConstructor();
    Game<?, ?> testGame = gameConstructorWithoutPlayerNumber.newInstance();

    return new GameFactory(gameConstructor, testGame.getMinimumNumberOfPlayers(),
        testGame.getMaximumNumberOfPlayers(), log);
  }

}
