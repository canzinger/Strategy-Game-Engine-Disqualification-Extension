package dev.entze.sge.engine.loader;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.game.Game;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

public class AgentLoader implements Callable<GameAgent<Game<?, ?>, ?>> {

  private final String agentClassName;
  private final ClassLoader classLoader;

  public AgentLoader(JarFile jarFile, ClassLoader classLoader) throws IOException {
    this.classLoader = classLoader;
    Attributes attributes = jarFile.getManifest().getMainAttributes();
    this.agentClassName = attributes.getValue("Agent-Class");
  }

  @Override
  public GameAgent<Game<?, ?>, ?> call()
      throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Class<GameAgent<Game<?, ?>, ?>> gameAgentClass = (Class<GameAgent<Game<?, ?>, ?>>) classLoader
        .loadClass(agentClassName);
    Constructor<GameAgent<Game<?, ?>, ?>> gameAgentConstructor = gameAgentClass.getConstructor();
    return gameAgentConstructor.newInstance();
  }

}
