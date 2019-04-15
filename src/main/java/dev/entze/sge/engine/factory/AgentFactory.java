package dev.entze.sge.engine.factory;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.engine.Logger;
import dev.entze.sge.game.Game;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class AgentFactory implements Factory<GameAgent<Game<?, ?>, ?>> {

  private final String agentName;
  private final Constructor<GameAgent<Game<?, ?>, ?>> agentConstructor;
  private final Logger log;

  public AgentFactory(String agentName,
      Constructor<GameAgent<Game<?, ?>, ?>> agentConstructor, Logger log) {
    this.agentName = agentName;
    this.agentConstructor = agentConstructor;
    this.log = log;
  }

  @Override
  public GameAgent<Game<?, ?>, ?> newInstance(Object... initargs) {
    try {
      return agentConstructor.newInstance();
    } catch (InstantiationException e) {
      log.error_();
      log.error("Could not instantiate new element with constructor of agent " + agentName);
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      log.error_();
      log.error("Could not access constructor of agent " + agentName);
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      log.error_();
      log.error("Could not invoke constructor of agent " + agentName);
      e.printStackTrace();
    }
    throw new IllegalStateException("AgentFactory for agent " + agentName + " is faulty.");
  }

  public String getAgentName() {
    return agentName;
  }
}
