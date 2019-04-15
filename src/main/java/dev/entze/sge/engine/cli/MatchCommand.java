package dev.entze.sge.engine.cli;

import dev.entze.sge.agent.GameAgent;
import dev.entze.sge.engine.Match;
import dev.entze.sge.game.Game;
import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

@Command(name = "match", aliases = {
    "m"}, description = "Let agents play against each other in a single match.")
public class MatchCommand implements Runnable {

  @ParentCommand
  private SgeCommand sge;

  @Option(names = {"-h", "--help"}, usageHelp = true, description = "print this message")
  private boolean helpRequested = false;

  @Option(names = {"-q",
      "--quiet"}, description = "Found once: Log only warnings. Twice: only errors. Thrice: no output")
  private boolean[] quiet = new boolean[0];

  @Option(names = {"-v",
      "--verbose"}, description = "Found once: Log debug information. Twice: with trace information")
  private boolean[] verbose = new boolean[0];

  @Option(names = {"-c",
      "--computation-time"}, defaultValue = "60", description = "Amount of computational time given for each action")
  long computationTime = 60;

  @Option(names = {"-u",
      "--time-unit"}, defaultValue = "SECONDS", description = "Time unit in which -c is. Valid values: ${COMPLETION-CANDIDATES}")
  TimeUnit timeUnit = TimeUnit.SECONDS;

  @Option(names = {"-p",
      "--number-of-players"}, arity = "1", description = "Number of players. By default the minimum required to play")
  private int players = (-1);

  @Parameters(arity = "0..*", description = "Optional: Specifies the configuration of the agents")
  private List<String> playerConfiguration;

  @Parameters(arity = "1..*", description = "Jar file(s) for game and agents")
  private List<File> files;

  @Override
  public void run() {
    if (verbose.length != 0 || quiet.length != 0) {
      sge.log.setLogLevel(quiet.length - verbose.length);
    }

    files.removeIf(file -> !file.getPath().endsWith(".jar"));

    sge.log.tra("Files: ");

    for (File file : files) {
      sge.log.tra_(file.getPath() + " ");
    }

    sge.log.trace_();

    playerConfiguration.removeIf(s -> s.endsWith(".jar"));

    sge.loadFiles(files);
    sge.log.debug("Successfully loaded all files.");

    if (players < 0) {
      players = sge.gameFactory.getMinimumNumberOfPlayers();
    }

    List<String> playerConfigurationLowercase = playerConfiguration.stream()
        .map(String::toLowerCase)
        .collect(Collectors.toList());

    for (int lastPotentiallyUnused = 0;
        lastPotentiallyUnused < sge.agentFactories.size()
            && playerConfigurationLowercase.size() < players;
        lastPotentiallyUnused++) {
      String agentName = sge.agentFactories.get(lastPotentiallyUnused).getAgentName().toLowerCase();
      if (!playerConfiguration.contains(agentName)) {
        playerConfigurationLowercase.add(agentName);
        playerConfiguration.add(sge.agentFactories.get(lastPotentiallyUnused).getAgentName());
      }
    }

    while (playerConfiguration.size() < players) {
      playerConfiguration.add("Human");
    }

    sge.log.deb("Configuration: ");
    for (String s : playerConfiguration) {
      sge.log.deb_(s + " ");
    }
    sge.log.debug_();

    List<GameAgent<Game<?, ?>, ?>> agentList = sge
        .createAgentListFromConfiguration(players, playerConfiguration);

    Match<Game<?, ?>, GameAgent<Game<?, ?>, ?>, ?> match = new Match(
        sge.gameFactory.newInstance(players), sge.gameASCIIVisualiser, agentList, computationTime,
        timeUnit, sge.log, sge.pool);

    match.call();


  }
}
