package dev.entze.sge.engine.loader;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.engine.Logger;
import dev.entze.sge.engine.factory.AgentFactory;
import dev.entze.sge.game.Game;
import java.util.concurrent.Callable;

public class AgentLoader implements Callable<AgentFactory> {

  private final String agentName;
  private final String agentClassName;
  private final ClassLoader classLoader;
  private final Logger log;

  public AgentLoader(String agentName, String agentClassName, ClassLoader classLoader,
      Logger log) {
    this.agentName = agentName;
    this.classLoader = classLoader;
    this.agentClassName = agentClassName;
    this.log = log;
  }

  @SuppressWarnings("unchecked")
  @Override
  public AgentFactory call()
      throws ClassNotFoundException, NoSuchMethodException {
    Class<GameAgent<Game<Object, Object>, Object>> gameAgentClass = (Class<GameAgent<Game<Object, Object>, Object>>) classLoader
        .loadClass(agentClassName);
    return new AgentFactory(agentName, gameAgentClass.getConstructor(), log);
  }

}
