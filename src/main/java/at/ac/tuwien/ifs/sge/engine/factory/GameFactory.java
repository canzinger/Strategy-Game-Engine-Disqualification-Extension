package at.ac.tuwien.ifs.sge.engine.factory;

import at.ac.tuwien.ifs.sge.engine.Logger;
import at.ac.tuwien.ifs.sge.game.Game;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class GameFactory<G extends Game<?, ?>> implements Factory<G> {

  private final Constructor<G> gameConstructor;
  private final Logger log;


  private final int minimumNumberOfPlayers;
  private final int maximumNumberOfPlayers;


  public GameFactory(Constructor<G> gameConstructor, int minimumNumberOfPlayers,
      int maximumNumberOfPlayers, Logger log) {
    this.gameConstructor = gameConstructor;
    this.minimumNumberOfPlayers = minimumNumberOfPlayers;
    this.maximumNumberOfPlayers = maximumNumberOfPlayers;
    this.log = log;
  }

  @Override
  public G newInstance(Object... initargs) {
    try {
      return gameConstructor.newInstance(initargs);
    } catch (InstantiationException e) {
      log._error_();
      log.error("Could not instantiate new element with constructor of game");
      log.printStackTrace(e);
    } catch (IllegalAccessException e) {
      log._error_();
      log.error("Could not access constructor of game");
      log.printStackTrace(e);
    } catch (InvocationTargetException e) {
      log._error_();
      log.error("Could not invoke constructor of game");
      log.printStackTrace(e);
    }
    throw new IllegalStateException("GameFactory is faulty");

  }

  public int getMinimumNumberOfPlayers() {
    return minimumNumberOfPlayers;
  }

  public int getMaximumNumberOfPlayers() {
    return maximumNumberOfPlayers;
  }

}
