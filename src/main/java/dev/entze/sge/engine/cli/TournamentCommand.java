package dev.entze.sge.engine.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(name = "tournament", aliases = {"t"}, description = "")
public class TournamentCommand {

  @ParentCommand
  private SgeCommand sge;

  @Option(names = {"-h", "--help"}, usageHelp = true, description = "print this message")
  private boolean helpRequested = false;

  @Option(names = {"-q",
      "--quiet"}, description = "Found once: Log only warnings. Twice: only errors. Thrice: no output")
  private boolean[] quiet = new boolean[0];

}
