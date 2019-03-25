package dev.entze.sge.engine.loader;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.game.Game;
import java.io.File;
import java.io.IOException;

public class AgentLoader extends JarLoader {

  public AgentLoader(File file) throws IOException {
    super(file);
  }

  public GameAgent<Game> loadGameAgent() {
    //TODO: Implement

    return null;
  }

}
