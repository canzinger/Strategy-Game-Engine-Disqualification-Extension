package dev.entze.sge.engine.loader;

import java.io.File;
import java.io.IOException;
import java.util.jar.Attributes;

public class AgentLoader extends JarLoader {

  Attributes attributes;

  public AgentLoader(File file) throws IOException {
    super(file);
    attributes = jarFile.getManifest().getMainAttributes();
  }

  /*public GameAgent<Game> loadGameAgent() {
    //TODO: Implement

    return null;
  }
  */

}
