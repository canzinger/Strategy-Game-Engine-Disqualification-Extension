package dev.entze.sge.engine.loader;

import dev.entze.sge.game.Game;
import dev.entze.sge.game.GameASCIIVisualiser;
import dev.entze.sge.util.Pair;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

public class GameLoader implements
    Callable<Pair<Constructor<Game<?, ?>>, GameASCIIVisualiser<Game<?, ?>>>> {

  private final String gameClassName;
  private final String gameASCIIVisualiserName;

  private final ClassLoader classLoader;

  public GameLoader(JarFile jarFile, ClassLoader classLoader) throws IOException {
    this.classLoader = classLoader;
    Attributes attributes = jarFile.getManifest().getMainAttributes();
    gameClassName = attributes.getValue("Game-Class");
    gameASCIIVisualiserName = attributes.getValue("GameASCIIVisualiser-Class");
  }

  @Override
  public Pair<Constructor<Game<?, ?>>, GameASCIIVisualiser<Game<?, ?>>> call()
      throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Class<Game<?, ?>> gameClass = (Class<Game<?, ?>>) classLoader.loadClass(gameClassName);
    Constructor<Game<?, ?>> gameConstructor = gameClass.getConstructor();

    Class<GameASCIIVisualiser<Game<?, ?>>> gameASCIIVisualiserClass = (Class<GameASCIIVisualiser<Game<?, ?>>>) classLoader
        .loadClass(gameASCIIVisualiserName);
    Constructor<GameASCIIVisualiser<Game<?, ?>>> gameASCIIVisualiserConstructor = gameASCIIVisualiserClass
        .getConstructor();

    return new Pair<>(gameConstructor, gameASCIIVisualiserConstructor.newInstance());
  }

}
