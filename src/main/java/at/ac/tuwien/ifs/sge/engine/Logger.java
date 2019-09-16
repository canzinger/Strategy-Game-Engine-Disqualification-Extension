package at.ac.tuwien.ifs.sge.engine;

import at.ac.tuwien.ifs.sge.util.Util;
import java.io.PrintStream;

public class Logger {

  private final String pre;
  private final String post;
  private final String tracePre;
  private final PrintStream traceStream;
  private final String tracePost;
  private final String debugPre;
  private final PrintStream debugStream;
  private final String debugPost;
  private final String infoPre;
  private final PrintStream infoStream;
  private final String infoPost;
  private final String warnPre;
  private final PrintStream warnStream;
  private final String warnPost;
  private final String errorPre;
  private final PrintStream errorStream;
  private final String errorPost;
  private int logLevel;


  public Logger(int logLevel, String pre, String post, String tracePre,
      PrintStream traceStream, String tracePost, String debugPre, PrintStream debugStream,
      String debugPost, String infoPre, PrintStream infoStream, String infoPost,
      String warnPre, PrintStream warnStream, String warnPost, String errorPre,
      PrintStream errorStream, String errorPost) {
    this.logLevel = logLevel;
    this.pre = pre;
    this.post = post;
    this.tracePre = tracePre;
    this.traceStream = traceStream;
    this.tracePost = tracePost;
    this.debugPre = debugPre;
    this.debugStream = debugStream;
    this.debugPost = debugPost;
    this.infoPre = infoPre;
    this.infoStream = infoStream;
    this.infoPost = infoPost;
    this.warnPre = warnPre;
    this.warnStream = warnStream;
    this.warnPost = warnPost;
    this.errorPre = errorPre;
    this.errorStream = errorStream;
    this.errorPost = errorPost;
  }

  public int getLogLevel() {
    return logLevel;
  }

  public void setLogLevel(int logLevel) {
    this.logLevel = logLevel;
  }

  private void print(PrintStream stream, int level, String... strings) {
    if (level >= logLevel) {
      String string = "";
      for (String s : strings) {
        string = string.concat(s);
      }
      stream.print(string);
    }
  }

  private void println(PrintStream stream, int level, String... strings) {
    if (level >= logLevel) {
      String string = "";
      for (String s : strings) {
        string = string.concat(s);
      }
      stream.println(string);
    }
  }

  public void printStackTrace(Exception e) {
    if (isDebug()) {
      e.printStackTrace();
    }
  }

  //-------------------------------

  public void traEnum(String string, int enumerator) {
    traf("%s: %d", string, enumerator);
  }

  public void traProcess(String string, int i, int max) {
    traf("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  public boolean isTrace() {
    return -2 >= logLevel;
  }

  public void tra_() {
    print(traceStream, -2);
  }

  public void tra_(String string) {
    print(traceStream, -2, string);
  }

  public void traf_(String format, Object... args) {
    tra_(String.format(format, args));
  }

  public void tra() {
    print(traceStream, -2, pre, tracePre, tracePost, post);
  }

  public void tra(String string) {
    print(traceStream, -2, pre, tracePre, string, tracePost, post);
  }

  public void traf(String format, Object... args) {
    tra(String.format(format, args));
  }

  public void trace_() {
    println(traceStream, -2);
  }

  public void trace_(String string) {
    println(traceStream, -2, string);
  }

  public void tracef_(String format, Object... args) {
    trace_(String.format(format, args));
  }

  public void trace() {
    println(traceStream, -2, pre, tracePre, tracePost, post);
  }

  public void trace(String string) {
    println(traceStream, -2, pre, tracePre, string, tracePost, post);
  }

  public void tracef(String format, Object... args) {
    trace(String.format(format, args));
  }

  //-------------------------------

  public void debEnum(String string, int enumerator) {
    debf("%s: %d", string, enumerator);
  }

  public void debProcess(String string, int i, int max) {
    debf("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  public boolean isDebug() {
    return -1 >= logLevel;
  }

  public void deb_() {
    print(debugStream, -1);
  }

  public void deb_(String string) {
    print(debugStream, -1, string);
  }

  public void debf_(String format, Object... args) {
    deb_(String.format(format, args));
  }

  public void deb() {
    print(debugStream, -1, pre, debugPre, debugPost, post);
  }

  public void deb(String string) {
    print(debugStream, -1, pre, debugPre, string, debugPost, post);
  }

  public void debf(String format, Object... args) {
    deb(String.format(format, args));
  }

  public void debug_() {
    println(debugStream, -1);
  }

  public void debug_(String string) {
    println(debugStream, -1, string);
  }

  public void debugf_(String format, Object... args) {
    debug_(String.format(format, args));
  }

  public void debug() {
    println(debugStream, -1, pre, debugPre, debugPost, post);
  }

  public void debug(String string) {
    println(debugStream, -1, pre, debugPre, string, debugPost, post);
  }

  public void debugf(String format, Object... args) {
    debug(String.format(format, args));
  }

  //-------------------------------

  public void infEnum(String string, int enumerator) {
    inff("%s: %d", string, enumerator);
  }

  public void infProcess(String string, int i, int max) {
    inff("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  public boolean isInfo() {
    return 0 >= logLevel;
  }

  public void inf_() {
    print(infoStream, 0);
  }

  public void inf_(String string) {
    print(infoStream, 0, string);
  }

  public void inff_(String format, Object... args) {
    inf_(String.format(format, args));
  }

  public void inf() {
    print(infoStream, 0, pre, infoPre, infoPost, post);
  }

  public void inf(String string) {
    print(infoStream, 0, pre, infoPre, string, infoPost, post);
  }

  public void inff(String format, Object... args) {
    inf(String.format(format, args));
  }

  public void info_() {
    println(infoStream, 0);
  }

  public void info_(String string) {
    println(infoStream, 0, string);
  }

  public void infof_(String format, Object... args) {
    info_(String.format(format, args));
  }

  public void info() {
    println(infoStream, 0, pre, infoPre, infoPost, post);
  }

  public void info(String string) {
    println(infoStream, 0, pre, infoPre, string, infoPost, post);
  }

  public void infof(String format, Object... args) {
    info(String.format(format, args));
  }

  //-------------------------------

  public void warEnum(String string, int enumerator) {
    warf("%s: %d", string, enumerator);
  }

  public void warProcess(String string, int i, int max) {
    warf("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  public boolean isWarn() {
    return 1 >= logLevel;
  }

  public void war_() {
    print(warnStream, 1);
  }

  public void war_(String string) {
    print(warnStream, 1, string);
  }

  public void warf_(String format, Object... args) {
    war_(String.format(format, args));
  }

  public void war() {
    print(warnStream, 1, pre, warnPre, warnPost, post);
  }

  public void war(String string) {
    print(warnStream, 1, pre, warnPre, string, warnPost, post);
  }

  public void warf(String format, Object... args) {
    war(String.format(format, args));
  }

  public void warn_() {
    println(warnStream, 1);
  }

  public void warn_(String string) {
    println(warnStream, 1, string);
  }

  public void warnf_(String format, Object... args) {
    warn_(String.format(format, args));
  }

  public void warn() {
    println(warnStream, 1, pre, warnPre, warnPost, post);
  }

  public void warn(String string) {
    println(warnStream, 1, pre, warnPre, string, warnPost, post);
  }

  public void warnf(String format, Object... args) {
    warn(String.format(format, args));
  }

  //-------------------------------

  public void errEnum(String string, int enumerator) {
    errf("%s: %d", string, enumerator);
  }

  public void errProcess(String string, int i, int max) {
    errf("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  public boolean isError() {
    return 2 >= logLevel;
  }

  public void err_() {
    print(errorStream, 2);
  }

  public void err_(String string) {
    print(errorStream, 2, string);
  }

  public void errf_(String format, Object... args) {
    err_(String.format(format, args));
  }

  public void err() {
    print(errorStream, 2, pre, errorPre, errorPost, post);
  }

  public void err(String string) {
    print(errorStream, 2, pre, errorPre, string, errorPost, post);
  }

  public void errf(String format, Object... args) {
    err(String.format(format, args));
  }

  public void error_() {
    println(errorStream, 2);
  }

  public void error_(String string) {
    println(errorStream, 2, string);
  }

  public void errorf_(String format, Object... args) {
    error_(String.format(format, args));
  }

  public void error() {
    println(errorStream, 2, pre, errorPre, errorPost, post);
  }

  public void error(String string) {
    println(errorStream, 2, pre, errorPre, string, errorPost, post);
  }

  public void errorf(String format, Object... args) {
    error(String.format(format, args));
  }


}
