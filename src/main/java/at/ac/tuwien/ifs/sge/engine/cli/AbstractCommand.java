package at.ac.tuwien.ifs.sge.engine.cli;

import at.ac.tuwien.ifs.sge.util.pair.ImmutablePair;
import at.ac.tuwien.ifs.sge.util.pair.Pair;
import at.ac.tuwien.ifs.sge.agent.GameAgent;
import at.ac.tuwien.ifs.sge.game.Game;
import java.io.File;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

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

  protected void destroyAgents(Collection<GameAgent<Game<Object, Object>, Object>> agents) {
    Deque<Pair<String, Future<Void>>> destroys = new ArrayDeque<>(agents.size());

    for (GameAgent<Game<Object, Object>, Object> gameAgent : agents) {
      destroys.add(new ImmutablePair<>(gameAgent.toString(), getSge().pool.submit(() -> {
        gameAgent.destroy();
        return null;
      })));
    }

    final int size = destroys.size();

    getSge().log.traProcess("Destroying agents", 0, size);

    while (!destroys.isEmpty() && Thread.currentThread().isAlive() && !Thread.currentThread()
        .isInterrupted()) {
      Pair<String, Future<Void>> tearDown = destroys.pop();
      getSge().log.tra_("\r");
      getSge().log.traProcess("Destroying agents", size - destroys.size(), size);
      try {
        tearDown.getB().get();
      } catch (InterruptedException e) {
        getSge().log.trace_(", failed.");
        getSge().log.debug("Interrupted while destroying agent ".concat(tearDown.getA()));
        getSge().log.printStackTrace(e);
      } catch (ExecutionException e) {
        getSge().log.trace_(", failed.");
        getSge().log.debug("Exception while destroying agent ".concat(tearDown.getA()));
        getSge().log.printStackTrace(e);
      }
    }

    if (!destroys.isEmpty()) {
      getSge().log.trace_(", failed.");
      getSge().log.warn("Following agents where not verified to be destroyed: "
          .concat(destroys.stream().map(Pair::getA).collect(Collectors.joining(", "))));
    } else {
      getSge().log.trace_(", done.");
    }
  }

}
