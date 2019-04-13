package dev.entze.sge.engine.loader;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.game.Game;
import java.lang.reflect.Constructor;
import java.util.concurrent.Callable;

public class AgentLoader implements Callable<Constructor<GameAgent<Game<?, ?>, ?>>> {

  private final String agentClassName;
  private final ClassLoader classLoader;

  public AgentLoader(String agentClassName, ClassLoader classLoader) {
    this.classLoader = classLoader;
    this.agentClassName = agentClassName;
  }

  @Override
  public Constructor<GameAgent<Game<?, ?>, ?>> call()
      throws ClassNotFoundException, NoSuchMethodException {
    Class<GameAgent<Game<?, ?>, ?>> gameAgentClass = (Class<GameAgent<Game<?, ?>, ?>>) classLoader
        .loadClass(agentClassName);
    return gameAgentClass.getConstructor();
  }

}
