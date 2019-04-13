package dev.entze.sge.engine.cli;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
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
      "--quiet"}, description = "Found once: Log only warnings. Twice: only errors. Thrice: no output.")
  private boolean[] quiet = new boolean[0];

  @Option(names = {"-v",
      "--verbose"}, description = "Found once: Log debug information. Twice: with trace information.")
  private boolean[] verbose = new boolean[0];

  @Option(names = {"-c",
      "--computation-time"}, defaultValue = "60", description = "Amount of computational time given for each action.")
  long computationTime = 60;

  @Option(names = {"-u",
      "--time-unit"}, defaultValue = "SECONDS", description = "Time unit in which -c is. Valid values: ${COMPLETION-CANDIDATES}")
  TimeUnit timeUnit = TimeUnit.SECONDS;

  @Option(names = {"-n",
      "--number-of-players"}, arity = "1", description = "Number of players. By default the minimum required to play.")
  private int players;

  @Parameters(arity = "0..*", description = "In which order which person plays")
  private List<String> playerConfiguration;

  @Parameters(arity = "1..*", description = "File(s) for game and agents")
  private List<File> files;

  @Override
  public void run() {
    if (verbose.length != 0 || quiet.length != 0) {
      sge.log.setLogLevel(quiet.length - verbose.length);
    }

    sge.loadFiles(files);

  }
}
