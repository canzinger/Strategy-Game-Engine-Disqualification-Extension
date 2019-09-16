package at.ac.tuwien.ifs.sge.engine.loader;

import at.ac.tuwien.ifs.sge.agent.GameAgent;
import at.ac.tuwien.ifs.sge.engine.Logger;
import at.ac.tuwien.ifs.sge.engine.factory.AgentFactory;
import at.ac.tuwien.ifs.sge.game.Game;
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
    return new AgentFactory(agentName, gameAgentClass.getConstructor(Logger.class), log);
  }

}
