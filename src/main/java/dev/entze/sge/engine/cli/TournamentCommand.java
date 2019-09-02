package dev.entze.sge.engine.cli;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.engine.game.tournament.Tournament;
import dev.entze.sge.engine.game.tournament.TournamentMode;
import dev.entze.sge.game.Game;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

@Command(name = "tournament", aliases = {"t"}, description = "Let agents play in a tournament.")
public class TournamentCommand extends AbstractCommand implements Runnable {

  @Option(names = {"-c",
      "--computation-time"}, defaultValue = "60", description = "Amount of computational time given for each action.")
  long computationTime = 60;

  @Option(names = {"-u",
      "--time-unit"}, defaultValue = "SECONDS", description = "Time unit in which -c is. Valid values: ${COMPLETION-CANDIDATES}")
  TimeUnit timeUnit = TimeUnit.SECONDS;

  @ParentCommand
  private SgeCommand sge;

  @Option(names = {"-h", "--help"}, usageHelp = true, description = "Prints this message.")
  private boolean helpRequested = false;

  @Option(names = {"-q",
      "--quiet"}, description = "Found once: Log only warnings. Twice: only errors. Thrice: no output")
  private boolean[] quiet = new boolean[0];

  @Option(names = {"-v",
      "--verbose"}, description = "Found once: Log debug information. Twice: with trace information")
  private boolean[] verbose = new boolean[0];

  @Option(names = {"-p",
      "--number-of-players"}, arity = "1", paramLabel = "N", description = "Number of players. By default the minimum the tournament supports.")
  private int numberOfPlayers = (-1);

  @Option(names = {"-b",
      "--board"}, arity = "1", paramLabel = "BOARD", description = "Optional: Initial board position. Can either be a file or a string.")
  private String board = null;

  @Option(names = {
      "--debug"}, description = "Starts engine in debug mode. No timeouts and verbose is turned on once.")
  private boolean debug = false;

  @Option(names = {"-f",
      "--file"}, arity = "1..*", paramLabel = "FILE", description = "File(s) of game and agents.")
  private List<File> files = new ArrayList<>();

  @Option(names = {"-d",
      "--directory"}, arity = "1..*", paramLabel = "DIRECTORY", description = "Directory(s) of game and agents. Note that all subdirectories will be considered.")
  private List<File> directories = new ArrayList<>();

  @Option(names = {"-a",
      "--agent"}, arity = "1..*", paramLabel = "AGENT", description = "Configuration of agents.")
  private List<String> agentConfiguration = new ArrayList<>();

  @Option(names = {"-r", "-s", "--shuffle"}, description = "Shuffle configuration of agents.")
  private boolean shuffle = false;

  @Option(names = {"-m",
      "--mode"}, arity = "1", paramLabel = "MODE", description = "Tournament Mode. Valid values: ${COMPLETION-CANDIDATES}")
  private TournamentMode tournamentMode = TournamentMode.ROUND_ROBIN;

  @Parameters(index = "0", arity = "0..*", description = {
      "Not explicitly specified files or configuration of agents."})
  private List<String> arguments = new ArrayList<>();


  @Override
  public void run() {
    loadCommon();

    int min = Math
        .max(sge.gameFactory.getMinimumNumberOfPlayers(), tournamentMode.getMinimumPerRound());
    int max = Math
        .min(sge.gameFactory.getMaximumNumberOfPlayers(), tournamentMode.getMaximumPerRound());

    if (numberOfPlayers < 0) {
      numberOfPlayers = min;
    } else if (!(min <= numberOfPlayers && numberOfPlayers <= max)) {
      sge.log.error("Tournament cannot be played with this number of players.");
      throw new IllegalArgumentException("Illegal player number.");
    }

    if (agentConfiguration.size() < min) {
      if (!agentConfiguration.isEmpty()) {
        sge.log.warn("Not enough agents, filling up with others.");
      }
      sge.fillAgentList(agentConfiguration);
    }

    if (shuffle) {
      Collections.shuffle(agentConfiguration);
    }

    printAgentConfiguration();

    List<GameAgent<Game<Object, Object>, Object>> agentList = sge
        .createAgentListFromConfiguration(agentConfiguration);

    Tournament<Game<Object, Object>, GameAgent<Game<Object, Object>, Object>, Object> tournament = tournamentMode
        .getTournament(sge.gameFactory, numberOfPlayers, board, agentList, computationTime,
            timeUnit, sge.debug, sge.log, sge.pool);

    try {
      tournament.call();
    } catch (Exception e) {
      sge.log.warn("Could not finish tournament.");
      sge.log.printStackTrace(e);
    }

    System.out.println("\n".concat(tournament.toTextRepresentation()).concat("\n"));

    destroyAgents(agentList);
  }

  @Override
  public SgeCommand getSge() {
    return sge;
  }

  @Override
  public boolean[] getQuiet() {
    return quiet;
  }

  @Override
  public boolean[] getVerbose() {
    return verbose;
  }

  @Override
  public int getNumberOfPlayers() {
    return numberOfPlayers;
  }

  @Override
  public String getBoard() {
    return board;
  }

  @Override
  public boolean isDebug() {
    return debug;
  }

  @Override
  public List<File> getFiles() {
    return files;
  }

  @Override
  public List<File> getDirectories() {
    return directories;
  }

  @Override
  public List<String> getAgentConfiguration() {
    return agentConfiguration;
  }

  @Override
  public List<String> getArguments() {
    return arguments;
  }

  @Override
  protected void setNumberOfPlayers(int numberOfPlayers) {
    this.numberOfPlayers = numberOfPlayers;
  }

  @Override
  protected void setBoard(String board) {
    this.board = board;
  }
}
