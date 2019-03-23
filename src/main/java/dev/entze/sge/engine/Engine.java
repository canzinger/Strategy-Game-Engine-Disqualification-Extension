package dev.entze.sge.engine;

import java.io.File;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "Strategy Game Engine", description = "A sequential game engine.", version = "sge "
    + Engine.version, aliases = "sge")
public class Engine implements Callable<Void> {

  @Option(names = {"-h", "--help"}, usageHelp = true, description = "print this message")
  private boolean helpRequested = false;

  @Option(names = {"-V", "--version"}, versionHelp = true, description = "print the version")
  private boolean versionRequested = false;

  @Parameters(arity = "1", paramLabel = "FILE", description = "JAR file of game")
  private File gameJar;

  @Parameters(arity = "1..*", paramLabel = "FILE", description = "JAR file(s) of agents")
  private File[] agentJars;

  public static final String version = "0.0.0";

  public static void main(String[] args) {
    CommandLine.call(new Engine(), args);
  }

  @Override
  public Void call() throws Exception {
    System.out.println("Found: " + gameJar.getAbsolutePath());
    for (File agentJar : agentJars) {
      System.out.println("Found: " + agentJar.getAbsolutePath());
    }
    return null;
  }
}
