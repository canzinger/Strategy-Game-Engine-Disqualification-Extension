package at.ac.tuwien.ifs.sge.engine.factory;

import at.ac.tuwien.ifs.sge.agent.GameAgent;
import at.ac.tuwien.ifs.sge.engine.Logger;
import at.ac.tuwien.ifs.sge.game.Game;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class AgentFactory implements Factory<GameAgent<Game<Object, Object>, Object>> {

  private final String agentName;
  private final Constructor<GameAgent<Game<Object, Object>, Object>> agentConstructor;
  private final Logger log;

  public AgentFactory(String agentName,
      Constructor<GameAgent<Game<Object, Object>, Object>> agentConstructor, Logger log) {
    this.agentName = agentName;
    this.agentConstructor = agentConstructor;
    this.log = log;
  }

  @Override
  public GameAgent<Game<Object, Object>, Object> newInstance(Object... initargs) {
    try {
      return agentConstructor.newInstance(new Logger(log.getLogLevel(), "[" + agentName + " ", "",
          "trace]: ", System.out, "",
          "debug]: ", System.out, "",
          "info]: ", System.out, "",
          "warn]: ", System.err, "",
          "error]: ", System.err, ""));
    } catch (InstantiationException e) {
      log._error_();
      log.error("Could not instantiate new element with constructor of agent " + agentName);
      log.printStackTrace(e);
    } catch (IllegalAccessException e) {
      log._error_();
      log.error("Could not access constructor of agent " + agentName);
      log.printStackTrace(e);
    } catch (InvocationTargetException e) {
      log._error_();
      log.error("Could not invoke constructor of agent " + agentName);
      log.printStackTrace(e);
    }
    throw new IllegalStateException("AgentFactory for agent " + agentName + " is faulty");
  }

  public String getAgentName() {
    return agentName;
  }
}
