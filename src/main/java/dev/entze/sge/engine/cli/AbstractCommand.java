package dev.entze.sge.engine.cli;

import java.io.File;
import java.util.List;

public abstract class AbstractCommand {

  public abstract SgeCommand getSge();

  public abstract boolean[] getQuiet();

  public abstract boolean[] getVerbose();

  public abstract int getNumberOfPlayers();

  public abstract String getBoard();

  public abstract boolean isDebug();

  public abstract List<File> getFiles();

  public abstract List<File> getDirectories();

  public abstract List<String> getAgentConfiguration();

  public abstract List<String> getArguments();

  protected abstract void setNumberOfPlayers(int numberOfPlayers);

  protected abstract void setBoard(String board);

  protected void loadDebug() {
    getSge().debug = getSge().debug || isDebug();
  }

  protected void loadLogLevel() {
    if (getVerbose().length != 0 || getQuiet().length != 0) {
      getSge().log
          .setLogLevel(getQuiet().length - (getVerbose().length + (getSge().debug ? 1 : 0)));
    }
  }

  protected void loadArguments() {
    getSge()
        .determineArguments(getArguments(), getFiles(), getDirectories(), getAgentConfiguration());
    getSge().processDirectories(getFiles(), getDirectories());
  }

  protected void loadFiles() {
    getSge().log.tra("Files: ");

    for (File file : getFiles()) {
      getSge().log.tra_(file.getPath() + " ");
    }

    getSge().log.trace_();

    getSge().loadFiles(getFiles());
    getSge().log.debug("Successfully loaded all files.");
  }


  protected void loadFillAgentList(int p) {
    getSge().fillAgentList(getAgentConfiguration(), p);
  }

  protected void loadBoard() {
    setBoard(getSge().interpretBoardString(getBoard()));
    if (getBoard() == null) {
      getSge().log.debug("No initial board given.");
    } else {
      getSge().log
          .debug("Initial board: " + getBoard().split("\n")[0] + (getBoard().contains("\n") ? "..."
              : ""));
    }
  }

  protected void printAgentConfiguration() {
    getSge().log.deb("Configuration: ");
    for (String s : getAgentConfiguration()) {
      getSge().log.deb_(s + " ");
    }
    getSge().log.debug_();
  }

  protected void loadCommon() {
    loadDebug();
    loadLogLevel();
    loadArguments();
    loadFiles();
    loadBoard();
  }

}
