package dev.entze.sge;

import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name= "Strategy Game Engine", description = "A sequential game engine.", version = "sge " + Engine.version, aliases = "sge")
public class Engine implements Callable<Void> {

  @Option(names = {"-h", "--help"}, usageHelp = true, description = "print this message")
  private boolean helpRequested = false;

  @Option(names = {"-V", "--version"}, versionHelp = true, description = "print the version")
  private boolean versionRequested = false;

  public static final String version = "0.0.0";

  public static void main(String[] args) {
    CommandLine.call(new Engine(), args);
  }

  @Override
  public Void call() throws Exception {
    return null;
  }
}
