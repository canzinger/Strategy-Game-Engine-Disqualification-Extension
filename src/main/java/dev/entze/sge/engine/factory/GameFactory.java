package dev.entze.sge.engine.factory;

import dev.entze.sge.engine.Logger;
import dev.entze.sge.game.Game;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class GameFactory implements Factory<Game<?, ?>> {

  private final Constructor<Game<?, ?>> gameConstructor;
  private final Logger log;

  private final int minimumNumberOfPlayers;
  private final int maximumNumberOfPlayers;

  private Game<?, ?> testGame = null;

  public GameFactory(Constructor<Game<?, ?>> gameConstructor, int minimumNumberOfPlayers,
      int maximumNumberOfPlayers, Logger log) {
    this.gameConstructor = gameConstructor;
    this.minimumNumberOfPlayers = minimumNumberOfPlayers;
    this.maximumNumberOfPlayers = maximumNumberOfPlayers;
    this.log = log;
  }

  @Override
  public Game<?, ?> newInstance(Object... initargs) {
    try {
      return gameConstructor.newInstance(initargs);
    } catch (InstantiationException e) {
      log.error_();
      log.error("Could not instantiate new element with constructor of game");
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      log.error_();
      log.error("Could not access constructor of game");
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      log.error_();
      log.error("Could not invoke constructor of game");
      e.printStackTrace();
    }
    throw new IllegalStateException("GameFactory is faulty.");

  }

  public int getMinimumNumberOfPlayers() {
    return minimumNumberOfPlayers;
  }

  public int getMaximumNumberOfPlayers() {
    return maximumNumberOfPlayers;
  }

  private Game<?, ?> getTestGame() {
    if (testGame == null) {
      testGame = newInstance();
    }
    return testGame;
  }

}
