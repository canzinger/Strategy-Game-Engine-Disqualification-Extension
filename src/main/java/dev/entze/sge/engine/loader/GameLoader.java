package dev.entze.sge.engine.loader;

import dev.entze.sge.game.Game;
import java.io.File;
import java.io.IOException;
import java.util.jar.Attributes;

public class GameLoader extends JarLoader {

  Attributes attributes;

  public GameLoader(File file) throws IOException {
    super(file);
    attributes = jarFile.getManifest().getMainAttributes();
  }

  public Game loadGame() throws IOException {
    //TODO: Implement

    return null;
  }

  /*
  public GameBoardTranslator<String> loadGameBoardVisualiser() {
    return null;
  }
  */

}
