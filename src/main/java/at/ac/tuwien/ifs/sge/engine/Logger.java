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

  private String preByLevel(int level) {
    switch (level) {
      case -2:
        return this.tracePre;
      case -1:
        return this.debugPre;
      case 0:
        return this.infoPre;
      case 1:
        return this.warnPre;
      case 2:
        return this.errorPre;
      default:
        return "";
    }
  }

  private String postByLevel(int level) {
    switch (level) {
      case -2:
        return this.tracePost;
      case -1:
        return this.debugPost;
      case 0:
        return this.infoPost;
      case 1:
        return this.warnPost;
      case 2:
        return this.errorPost;
      default:
        return "";
    }
  }

  //region print

  private void print(PrintStream stream, int level, boolean pre, boolean post) {
    if (level >= logLevel) {
      if (pre) {
        stream.print(this.pre + preByLevel(level));
      }
      if (post) {
        stream.print(this.post + postByLevel(level));
      }
    }
  }

  private void print(PrintStream stream, int level, boolean pre, boolean post, boolean b) {
    if (level >= logLevel) {
      if (pre) {
        stream.print(this.pre + preByLevel(level));
      }
      stream.print(b);
      if (post) {
        stream.print(this.post + postByLevel(level));
      }
    }
  }

  private void print(PrintStream stream, int level, boolean pre, boolean post, char[] s) {
    if (level >= logLevel) {
      if (pre) {
        stream.print(this.pre + preByLevel(level));
      }
      stream.print(s);
      if (post) {
        stream.print(this.post + postByLevel(level));
      }
    }
  }

  private void print(PrintStream stream, int level, boolean pre, boolean post, double d) {
    if (level >= logLevel) {
      if (pre) {
        stream.print(this.pre + preByLevel(level));
      }
      stream.print(d);
      if (post) {
        stream.print(this.post + postByLevel(level));
      }
    }
  }

  private void print(PrintStream stream, int level, boolean pre, boolean post, float f) {
    if (level >= logLevel) {
      if (pre) {
        stream.print(this.pre + preByLevel(level));
      }
      stream.print(f);
      if (post) {
        stream.print(this.post + postByLevel(level));
      }
    }
  }

  private void print(PrintStream stream, int level, boolean pre, boolean post, int i) {
    if (level >= logLevel) {
      if (pre) {
        stream.print(this.pre + preByLevel(level));
      }
      stream.print(i);
      if (post) {
        stream.print(this.post + postByLevel(level));
      }
    }
  }

  private void print(PrintStream stream, int level, boolean pre, boolean post, long l) {
    if (level >= logLevel) {
      if (pre) {
        stream.print(this.pre + preByLevel(level));
      }
      stream.print(l);
      if (post) {
        stream.print(this.post + postByLevel(level));
      }
    }
  }

  private void print(PrintStream stream, int level, boolean pre, boolean post, Object obj) {
    if (level >= logLevel) {
      if (pre) {
        stream.print(this.pre + preByLevel(level));
      }
      stream.print(obj);
      if (post) {
        stream.print(this.post + postByLevel(level));
      }
    }
  }

  private void print(PrintStream stream, int level, boolean pre, boolean post, String s) {
    if (level >= logLevel) {
      if (pre) {
        stream.print(this.pre + preByLevel(level));
      }
      stream.print(s);
      if (post) {
        stream.print(this.post + postByLevel(level));
      }
    }
  }

  //endregion

  //region println

  private void println(PrintStream stream, int level, boolean pre, boolean post) {
    if (level >= logLevel) {
      if (!pre && !post) {
        stream.println();
      } else if (pre && !post) {
        stream.println(this.pre + preByLevel(level));
      } else {
        if (pre) {
          stream.print(this.pre + preByLevel(level));
        }
        stream.println(this.post + postByLevel(level));
      }
    }
  }

  private void println(PrintStream stream, int level, boolean pre, boolean post, boolean b) {
    if (level >= logLevel) {
      if (pre) {
        stream.print(this.pre + preByLevel(level));
      }
      if (!post) {
        stream.println(b);
      } else {
        stream.print(b);
        stream.println(this.post + postByLevel(level));
      }
    }
  }

  private void println(PrintStream stream, int level, boolean pre, boolean post, char[] s) {
    if (level >= logLevel) {
      if (pre) {
        stream.print(this.pre + preByLevel(level));
      }
      if (!post) {
        stream.println(s);
      } else {
        stream.print(s);
        stream.println(this.post + postByLevel(level));
      }
    }
  }

  private void println(PrintStream stream, int level, boolean pre, boolean post, double d) {
    if (level >= logLevel) {
      if (pre) {
        stream.print(this.pre + preByLevel(level));
      }
      if (!post) {
        stream.println(d);
      } else {
        stream.print(d);
        stream.println(this.post + postByLevel(level));
      }
    }
  }

  private void println(PrintStream stream, int level, boolean pre, boolean post, float f) {
    if (level >= logLevel) {
      if (pre) {
        stream.print(this.pre + preByLevel(level));
      }
      if (!post) {
        stream.println(f);
      } else {
        stream.print(f);
        stream.println(this.post + postByLevel(level));
      }
    }
  }

  private void println(PrintStream stream, int level, boolean pre, boolean post, int i) {
    if (level >= logLevel) {
      if (pre) {
        stream.print(this.pre + preByLevel(level));
      }
      if (!post) {
        stream.println(i);
      } else {
        stream.print(i);
        stream.println(this.post + postByLevel(level));
      }
    }
  }

  private void println(PrintStream stream, int level, boolean pre, boolean post, long l) {
    if (level >= logLevel) {
      if (pre) {
        stream.print(this.pre + preByLevel(level));
      }
      if (!post) {
        stream.println(l);
      } else {
        stream.print(l);
        stream.println(this.post + postByLevel(level));
      }
    }
  }

  private void println(PrintStream stream, int level, boolean pre, boolean post, Object obj) {
    if (level >= logLevel) {
      if (pre) {
        stream.print(this.pre + preByLevel(level));
      }
      if (!post) {
        stream.println(obj);
      } else {
        stream.print(obj);
        stream.println(this.post + postByLevel(level));
      }
    }
  }

  private void println(PrintStream stream, int level, boolean pre, boolean post, String s) {
    if (level >= logLevel) {
      if (pre) {
        stream.print(this.pre + preByLevel(level));
      }
      if (!post) {
        stream.println(s);
      } else {
        stream.print(s);
        stream.println(this.post + postByLevel(level));
      }
    }
  }

  //endregion

  //region format

  private void format(PrintStream stream, int level, boolean pre, boolean post, String format,
      Object... args) {
    if (level >= logLevel) {
      if (pre) {
        stream.print(this.pre + preByLevel(level));
      }
      stream.format(format, args);
      if (post) {
        stream.print(this.post + postByLevel(level));
      }
    }
  }

  private void formatln(PrintStream stream, int level, boolean pre, boolean post, String format,
      Object... args) {
    if (level >= logLevel) {
      if (pre) {
        stream.print(this.pre + preByLevel(level));
      }
      stream.format(format, args);
      if (post) {
        stream.println(this.post + postByLevel(level));
      } else {
        stream.println();
      }
    }
  }

  //endregion

  public void printStackTrace(Exception e) {
    if (isDebug()) {
      e.printStackTrace();
    }
  }

  //region isLevel

  /**
   * Returns true iff the log level is at least trace (-2).
   *
   * @return true iff the log level is at least trace (-2).
   */
  public boolean isTrace() {
    return -2 >= logLevel;
  }


  /**
   * Returns true iff the log level is at least debug (-1).
   *
   * @return true iff the log level is at least debug (-1).
   */
  public boolean isDebug() {
    return -1 >= logLevel;
  }


  /**
   * Returns true iff the log level is at least info (0).
   *
   * @return true iff the log level is at least info (0).
   */
  public boolean isInfo() {
    return 0 >= logLevel;
  }


  /**
   * Returns true iff the log level is at least warn (1).
   *
   * @return true iff the log level is at least warn (1).
   */
  public boolean isWarn() {
    return 1 >= logLevel;
  }


  /**
   * Returns true iff the log level is at least error (2).
   *
   * @return true iff the log level is at least error (2).
   */
  public boolean isError() {
    return 2 >= logLevel;
  }
  //endregion

  //region Enum

  /**
   * Prints "$string: $enumerator" without pre and post and newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void _traEnum_(String string, int enumerator) {
    _traf_("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" without pre and post but with newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void _traceEnum_(String string, int enumerator) {
    _tracef_("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" without pre but with post but without newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void _traEnum(String string, int enumerator) {
    _traf("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" without pre but with post and newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void _traceEnum(String string, int enumerator) {
    _tracef("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" with pre but without post and newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void traEnum_(String string, int enumerator) {
    traf_("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" with pre but without post but with newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void traceEnum_(String string, int enumerator) {
    tracef_("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" with pre and post but without newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void traEnum(String string, int enumerator) {
    traf("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" with pre and post and newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void traceEnum(String string, int enumerator) {
    tracef("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" without pre and post and newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void _debEnum_(String string, int enumerator) {
    _debf_("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" without pre and post but with newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void _debugEnum_(String string, int enumerator) {
    _debugf_("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" without pre but with post but without newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void _debEnum(String string, int enumerator) {
    _debf("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" without pre but with post and newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void _debugEnum(String string, int enumerator) {
    _debugf("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" with pre but without post and newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void debEnum_(String string, int enumerator) {
    debf_("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" with pre but without post but with newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void debugEnum_(String string, int enumerator) {
    debugf_("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" with pre and post but without newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void debEnum(String string, int enumerator) {
    debf("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" with pre and post and newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void debugEnum(String string, int enumerator) {
    debugf("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" without pre and post and newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void _infEnum_(String string, int enumerator) {
    _inff_("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" without pre and post but with newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void _infoEnum_(String string, int enumerator) {
    _infof_("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" without pre but with post but without newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void _infEnum(String string, int enumerator) {
    _inff("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" without pre but with post and newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void _infoEnum(String string, int enumerator) {
    _infof("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" with pre but without post and newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void infEnum_(String string, int enumerator) {
    inff_("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" with pre but without post but with newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void infoEnum_(String string, int enumerator) {
    infof_("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" with pre and post but without newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void infEnum(String string, int enumerator) {
    inff("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" with pre and post and newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void infoEnum(String string, int enumerator) {
    infof("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" without pre and post and newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void _warEnum_(String string, int enumerator) {
    _warf_("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" without pre and post but with newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void _warnEnum_(String string, int enumerator) {
    _warnf_("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" without pre but with post but without newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void _warEnum(String string, int enumerator) {
    _warf("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" without pre but with post and newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void _warnEnum(String string, int enumerator) {
    _warnf("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" with pre but without post and newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void warEnum_(String string, int enumerator) {
    warf_("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" with pre but without post but with newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void warnEnum_(String string, int enumerator) {
    warnf_("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" with pre and post but without newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void warEnum(String string, int enumerator) {
    warf("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" with pre and post and newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void warnEnum(String string, int enumerator) {
    warnf("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" without pre and post and newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void _errEnum_(String string, int enumerator) {
    _errf_("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" without pre and post but with newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void _errorEnum_(String string, int enumerator) {
    _errorf_("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" without pre but with post but without newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void _errEnum(String string, int enumerator) {
    _errf("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" without pre but with post and newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void _errorEnum(String string, int enumerator) {
    _errorf("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" with pre but without post and newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void errEnum_(String string, int enumerator) {
    errf_("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" with pre but without post but with newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void errorEnum_(String string, int enumerator) {
    errorf_("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" with pre and post but without newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void errEnum(String string, int enumerator) {
    errf("%s: %d", string, enumerator);
  }

  /**
   * Prints "$string: $enumerator" with pre and post and newline.
   *
   * @param string - the string
   * @param enumerator - the enumerator
   */
  public void errorEnum(String string, int enumerator) {
    errorf("%s: %d", string, enumerator);
  }

  //endregion

  //region Process

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically without pre and post
   * and newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void _traProcess_(String string, int i, int max) {
    _traf_("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically without pre and post
   * but with newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void _traceProcess_(String string, int i, int max) {
    _tracef_("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically without pre but with
   * post but without newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void _traProcess(String string, int i, int max) {
    _traf("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically without pre but with
   * post and newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void _traceProcess(String string, int i, int max) {
    _tracef("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically with pre but without
   * post and newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void traProcess_(String string, int i, int max) {
    traf_("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically with pre but without
   * post but with newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void traceProcess_(String string, int i, int max) {
    tracef_("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically with pre and post but
   * without newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void traProcess(String string, int i, int max) {
    traf("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically with pre and post and
   * newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void traceProcess(String string, int i, int max) {
    tracef("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically without pre and post
   * and newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void _debProcess_(String string, int i, int max) {
    _debf_("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically without pre and post
   * but with newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void _debugProcess_(String string, int i, int max) {
    _debugf_("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically without pre but with
   * post but without newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void _debProcess(String string, int i, int max) {
    _debf("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically without pre but with
   * post and newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void _debugProcess(String string, int i, int max) {
    _debugf("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically with pre but without
   * post and newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void debProcess_(String string, int i, int max) {
    debf_("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically with pre but without
   * post but with newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void debugProcess_(String string, int i, int max) {
    debugf_("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically with pre and post but
   * without newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void debProcess(String string, int i, int max) {
    debf("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically with pre and post and
   * newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void debugProcess(String string, int i, int max) {
    debugf("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically without pre and post
   * and newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void _infProcess_(String string, int i, int max) {
    _inff_("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically without pre and post
   * but with newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void _infoProcess_(String string, int i, int max) {
    _infof_("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically without pre but with
   * post but without newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void _infProcess(String string, int i, int max) {
    _inff("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically without pre but with
   * post and newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void _infoProcess(String string, int i, int max) {
    _infof("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically with pre but without
   * post and newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void infProcess_(String string, int i, int max) {
    inff_("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically with pre but without
   * post but with newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void infoProcess_(String string, int i, int max) {
    infof_("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically with pre and post but
   * without newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void infProcess(String string, int i, int max) {
    inff("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically with pre and post and
   * newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void infoProcess(String string, int i, int max) {
    infof("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically without pre and post
   * and newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void _warProcess_(String string, int i, int max) {
    _warf_("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically without pre and post
   * but with newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void _warnProcess_(String string, int i, int max) {
    _warnf_("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically without pre but with
   * post but without newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void _warProcess(String string, int i, int max) {
    _warf("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically without pre but with
   * post and newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void _warnProcess(String string, int i, int max) {
    _warnf("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically with pre but without
   * post and newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void warProcess_(String string, int i, int max) {
    warf_("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically with pre but without
   * post but with newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void warnProcess_(String string, int i, int max) {
    warnf_("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically with pre and post but
   * without newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void warProcess(String string, int i, int max) {
    warf("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically with pre and post and
   * newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void warnProcess(String string, int i, int max) {
    warnf("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically without pre and post
   * and newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void _errProcess_(String string, int i, int max) {
    _errf_("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically without pre and post
   * but with newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void _errorProcess_(String string, int i, int max) {
    _errorf_("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically without pre but with
   * post but without newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void _errProcess(String string, int i, int max) {
    _errf("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically without pre but with
   * post and newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void _errorProcess(String string, int i, int max) {
    _errorf("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically with pre but without
   * post and newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void errProcess_(String string, int i, int max) {
    errf_("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically with pre but without
   * post but with newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void errorProcess_(String string, int i, int max) {
    errorf_("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically with pre and post but
   * without newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void errProcess(String string, int i, int max) {
    errf("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  /**
   * Prints "$string: %percent% ($i/$max)" where percent is i/max numerically with pre and post and
   * newline.
   *
   * @param string - the string
   * @param i - progress
   * @param max - maximum
   */
  public void errorProcess(String string, int i, int max) {
    errorf("%s: %.0f%% (%d/%d)", string, Util.percentage(i, max), i, max);
  }

  //endregion

  //region Atoms

  //region Trace

  /**
   * Prints no string without pre and post and newline.
   */
  public void _tra_() {
    print(traceStream, -2, false, false);
  }

  /**
   * Prints no string without pre and post but with newline.
   */
  public void _trace_() {
    println(traceStream, -2, false, false);
  }

  /**
   * Prints no string without pre but with post but without newline.
   */
  public void _tra() {
    print(traceStream, -2, false, false);
  }

  /**
   * Prints no string without pre but with post and newline.
   */
  public void _trace() {
    println(traceStream, -2, false, false);
  }

  /**
   * Prints no string with pre but without post and newline.
   */
  public void tra_() {
    print(traceStream, -2, true, true);
  }

  /**
   * Prints no string with pre but without post but with newline.
   */
  public void trace_() {
    println(traceStream, -2, true, true);
  }

  /**
   * Prints no string with pre and post but without newline.
   */
  public void tra() {
    print(traceStream, -2, true, true);
  }

  /**
   * Prints no string with pre and post and newline.
   */
  public void trace() {
    println(traceStream, -2, true, true);
  }

  /**
   * Prints the boolean without pre and post and newline.
   *
   * @param b - the boolean
   */
  public void _tra_(boolean b) {
    print(traceStream, -2, false, false, b);
  }

  /**
   * Prints the boolean without pre and post but with newline.
   *
   * @param b - the boolean
   */
  public void _trace_(boolean b) {
    println(traceStream, -2, false, false, b);
  }

  /**
   * Prints the boolean without pre but with post but without newline.
   *
   * @param b - the boolean
   */
  public void _tra(boolean b) {
    print(traceStream, -2, false, false, b);
  }

  /**
   * Prints the boolean without pre but with post and newline.
   *
   * @param b - the boolean
   */
  public void _trace(boolean b) {
    println(traceStream, -2, false, false, b);
  }

  /**
   * Prints the boolean with pre but without post and newline.
   *
   * @param b - the boolean
   */
  public void tra_(boolean b) {
    print(traceStream, -2, true, true, b);
  }

  /**
   * Prints the boolean with pre but without post but with newline.
   *
   * @param b - the boolean
   */
  public void trace_(boolean b) {
    println(traceStream, -2, true, true, b);
  }

  /**
   * Prints the boolean with pre and post but without newline.
   *
   * @param b - the boolean
   */
  public void tra(boolean b) {
    print(traceStream, -2, true, true, b);
  }

  /**
   * Prints the boolean with pre and post and newline.
   *
   * @param b - the boolean
   */
  public void trace(boolean b) {
    println(traceStream, -2, true, true, b);
  }

  /**
   * Prints the char[] without pre and post and newline.
   *
   * @param s - the char[]
   */
  public void _tra_(char[] s) {
    print(traceStream, -2, false, false, s);
  }

  /**
   * Prints the char[] without pre and post but with newline.
   *
   * @param s - the char[]
   */
  public void _trace_(char[] s) {
    println(traceStream, -2, false, false, s);
  }

  /**
   * Prints the char[] without pre but with post but without newline.
   *
   * @param s - the char[]
   */
  public void _tra(char[] s) {
    print(traceStream, -2, false, false, s);
  }

  /**
   * Prints the char[] without pre but with post and newline.
   *
   * @param s - the char[]
   */
  public void _trace(char[] s) {
    println(traceStream, -2, false, false, s);
  }

  /**
   * Prints the char[] with pre but without post and newline.
   *
   * @param s - the char[]
   */
  public void tra_(char[] s) {
    print(traceStream, -2, true, true, s);
  }

  /**
   * Prints the char[] with pre but without post but with newline.
   *
   * @param s - the char[]
   */
  public void trace_(char[] s) {
    println(traceStream, -2, true, true, s);
  }

  /**
   * Prints the char[] with pre and post but without newline.
   *
   * @param s - the char[]
   */
  public void tra(char[] s) {
    print(traceStream, -2, true, true, s);
  }

  /**
   * Prints the char[] with pre and post and newline.
   *
   * @param s - the char[]
   */
  public void trace(char[] s) {
    println(traceStream, -2, true, true, s);
  }

  /**
   * Prints the double without pre and post and newline.
   *
   * @param d - the double
   */
  public void _tra_(double d) {
    print(traceStream, -2, false, false, d);
  }

  /**
   * Prints the double without pre and post but with newline.
   *
   * @param d - the double
   */
  public void _trace_(double d) {
    println(traceStream, -2, false, false, d);
  }

  /**
   * Prints the double without pre but with post but without newline.
   *
   * @param d - the double
   */
  public void _tra(double d) {
    print(traceStream, -2, false, false, d);
  }

  /**
   * Prints the double without pre but with post and newline.
   *
   * @param d - the double
   */
  public void _trace(double d) {
    println(traceStream, -2, false, false, d);
  }

  /**
   * Prints the double with pre but without post and newline.
   *
   * @param d - the double
   */
  public void tra_(double d) {
    print(traceStream, -2, true, true, d);
  }

  /**
   * Prints the double with pre but without post but with newline.
   *
   * @param d - the double
   */
  public void trace_(double d) {
    println(traceStream, -2, true, true, d);
  }

  /**
   * Prints the double with pre and post but without newline.
   *
   * @param d - the double
   */
  public void tra(double d) {
    print(traceStream, -2, true, true, d);
  }

  /**
   * Prints the double with pre and post and newline.
   *
   * @param d - the double
   */
  public void trace(double d) {
    println(traceStream, -2, true, true, d);
  }

  /**
   * Prints the float without pre and post and newline.
   *
   * @param f - the float
   */
  public void _tra_(float f) {
    print(traceStream, -2, false, false, f);
  }

  /**
   * Prints the float without pre and post but with newline.
   *
   * @param f - the float
   */
  public void _trace_(float f) {
    println(traceStream, -2, false, false, f);
  }

  /**
   * Prints the float without pre but with post but without newline.
   *
   * @param f - the float
   */
  public void _tra(float f) {
    print(traceStream, -2, false, false, f);
  }

  /**
   * Prints the float without pre but with post and newline.
   *
   * @param f - the float
   */
  public void _trace(float f) {
    println(traceStream, -2, false, false, f);
  }

  /**
   * Prints the float with pre but without post and newline.
   *
   * @param f - the float
   */
  public void tra_(float f) {
    print(traceStream, -2, true, true, f);
  }

  /**
   * Prints the float with pre but without post but with newline.
   *
   * @param f - the float
   */
  public void trace_(float f) {
    println(traceStream, -2, true, true, f);
  }

  /**
   * Prints the float with pre and post but without newline.
   *
   * @param f - the float
   */
  public void tra(float f) {
    print(traceStream, -2, true, true, f);
  }

  /**
   * Prints the float with pre and post and newline.
   *
   * @param f - the float
   */
  public void trace(float f) {
    println(traceStream, -2, true, true, f);
  }

  /**
   * Prints the int without pre and post and newline.
   *
   * @param i - the int
   */
  public void _tra_(int i) {
    print(traceStream, -2, false, false, i);
  }

  /**
   * Prints the int without pre and post but with newline.
   *
   * @param i - the int
   */
  public void _trace_(int i) {
    println(traceStream, -2, false, false, i);
  }

  /**
   * Prints the int without pre but with post but without newline.
   *
   * @param i - the int
   */
  public void _tra(int i) {
    print(traceStream, -2, false, false, i);
  }

  /**
   * Prints the int without pre but with post and newline.
   *
   * @param i - the int
   */
  public void _trace(int i) {
    println(traceStream, -2, false, false, i);
  }

  /**
   * Prints the int with pre but without post and newline.
   *
   * @param i - the int
   */
  public void tra_(int i) {
    print(traceStream, -2, true, true, i);
  }

  /**
   * Prints the int with pre but without post but with newline.
   *
   * @param i - the int
   */
  public void trace_(int i) {
    println(traceStream, -2, true, true, i);
  }

  /**
   * Prints the int with pre and post but without newline.
   *
   * @param i - the int
   */
  public void tra(int i) {
    print(traceStream, -2, true, true, i);
  }

  /**
   * Prints the int with pre and post and newline.
   *
   * @param i - the int
   */
  public void trace(int i) {
    println(traceStream, -2, true, true, i);
  }

  /**
   * Prints the long without pre and post and newline.
   *
   * @param l - the long
   */
  public void _tra_(long l) {
    print(traceStream, -2, false, false, l);
  }

  /**
   * Prints the long without pre and post but with newline.
   *
   * @param l - the long
   */
  public void _trace_(long l) {
    println(traceStream, -2, false, false, l);
  }

  /**
   * Prints the long without pre but with post but without newline.
   *
   * @param l - the long
   */
  public void _tra(long l) {
    print(traceStream, -2, false, false, l);
  }

  /**
   * Prints the long without pre but with post and newline.
   *
   * @param l - the long
   */
  public void _trace(long l) {
    println(traceStream, -2, false, false, l);
  }

  /**
   * Prints the long with pre but without post and newline.
   *
   * @param l - the long
   */
  public void tra_(long l) {
    print(traceStream, -2, true, true, l);
  }

  /**
   * Prints the long with pre but without post but with newline.
   *
   * @param l - the long
   */
  public void trace_(long l) {
    println(traceStream, -2, true, true, l);
  }

  /**
   * Prints the long with pre and post but without newline.
   *
   * @param l - the long
   */
  public void tra(long l) {
    print(traceStream, -2, true, true, l);
  }

  /**
   * Prints the long with pre and post and newline.
   *
   * @param l - the long
   */
  public void trace(long l) {
    println(traceStream, -2, true, true, l);
  }

  /**
   * Prints the object without pre and post and newline.
   *
   * @param obj - the Object
   */
  public void _tra_(Object obj) {
    print(traceStream, -2, false, false, obj);
  }

  /**
   * Prints the object without pre and post but with newline.
   *
   * @param obj - the Object
   */
  public void _trace_(Object obj) {
    println(traceStream, -2, false, false, obj);
  }

  /**
   * Prints the object without pre but with post but without newline.
   *
   * @param obj - the Object
   */
  public void _tra(Object obj) {
    print(traceStream, -2, false, false, obj);
  }

  /**
   * Prints the object without pre but with post and newline.
   *
   * @param obj - the Object
   */
  public void _trace(Object obj) {
    println(traceStream, -2, false, false, obj);
  }

  /**
   * Prints the object with pre but without post and newline.
   *
   * @param obj - the Object
   */
  public void tra_(Object obj) {
    print(traceStream, -2, true, true, obj);
  }

  /**
   * Prints the object with pre but without post but with newline.
   *
   * @param obj - the Object
   */
  public void trace_(Object obj) {
    println(traceStream, -2, true, true, obj);
  }

  /**
   * Prints the object with pre and post but without newline.
   *
   * @param obj - the Object
   */
  public void tra(Object obj) {
    print(traceStream, -2, true, true, obj);
  }

  /**
   * Prints the object with pre and post and newline.
   *
   * @param obj - the Object
   */
  public void trace(Object obj) {
    println(traceStream, -2, true, true, obj);
  }

  /**
   * Prints the string without pre and post and newline.
   *
   * @param s - the String
   */
  public void _tra_(String s) {
    print(traceStream, -2, false, false, s);
  }

  /**
   * Prints the string without pre and post but with newline.
   *
   * @param s - the String
   */
  public void _trace_(String s) {
    println(traceStream, -2, false, false, s);
  }

  /**
   * Prints the string without pre but with post but without newline.
   *
   * @param s - the String
   */
  public void _tra(String s) {
    print(traceStream, -2, false, false, s);
  }

  /**
   * Prints the string without pre but with post and newline.
   *
   * @param s - the String
   */
  public void _trace(String s) {
    println(traceStream, -2, false, false, s);
  }

  /**
   * Prints the string with pre but without post and newline.
   *
   * @param s - the String
   */
  public void tra_(String s) {
    print(traceStream, -2, true, true, s);
  }

  /**
   * Prints the string with pre but without post but with newline.
   *
   * @param s - the String
   */
  public void trace_(String s) {
    println(traceStream, -2, true, true, s);
  }

  /**
   * Prints the string with pre and post but without newline.
   *
   * @param s - the String
   */
  public void tra(String s) {
    print(traceStream, -2, true, true, s);
  }

  /**
   * Prints the string with pre and post and newline.
   *
   * @param s - the String
   */
  public void trace(String s) {
    println(traceStream, -2, true, true, s);
  }

  /**
   * Prints the format without pre and post and newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void _traf_(String format, Object... args) {
    format(traceStream, -2, false, false, format, args);
  }

  /**
   * Prints the format without pre and post but with newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void _tracef_(String format, Object... args) {
    formatln(traceStream, -2, false, false, format, args);
  }

  /**
   * Prints the format without pre but with post but without newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void _traf(String format, Object... args) {
    format(traceStream, -2, false, false, format, args);
  }

  /**
   * Prints the format without pre but with post and newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void _tracef(String format, Object... args) {
    formatln(traceStream, -2, false, false, format, args);
  }

  /**
   * Prints the format with pre but without post and newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void traf_(String format, Object... args) {
    format(traceStream, -2, true, true, format, args);
  }

  /**
   * Prints the format with pre but without post but with newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void tracef_(String format, Object... args) {
    formatln(traceStream, -2, true, true, format, args);
  }

  /**
   * Prints the format with pre and post but without newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void traf(String format, Object... args) {
    format(traceStream, -2, true, true, format, args);
  }

  /**
   * Prints the format with pre and post and newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void tracef(String format, Object... args) {
    formatln(traceStream, -2, true, true, format, args);
  }

  //endregion

  //region Debug

  /**
   * Prints no string without pre and post and newline.
   */
  public void _deb_() {
    print(debugStream, -1, false, false);
  }

  /**
   * Prints no string without pre and post but with newline.
   */
  public void _debug_() {
    println(debugStream, -1, false, false);
  }

  /**
   * Prints no string without pre but with post but without newline.
   */
  public void _deb() {
    print(debugStream, -1, false, false);
  }

  /**
   * Prints no string without pre but with post and newline.
   */
  public void _debug() {
    println(debugStream, -1, false, false);
  }

  /**
   * Prints no string with pre but without post and newline.
   */
  public void deb_() {
    print(debugStream, -1, true, true);
  }

  /**
   * Prints no string with pre but without post but with newline.
   */
  public void debug_() {
    println(debugStream, -1, true, true);
  }

  /**
   * Prints no string with pre and post but without newline.
   */
  public void deb() {
    print(debugStream, -1, true, true);
  }

  /**
   * Prints no string with pre and post and newline.
   */
  public void debug() {
    println(debugStream, -1, true, true);
  }

  /**
   * Prints the boolean without pre and post and newline.
   *
   * @param b - the boolean
   */
  public void _deb_(boolean b) {
    print(debugStream, -1, false, false, b);
  }

  /**
   * Prints the boolean without pre and post but with newline.
   *
   * @param b - the boolean
   */
  public void _debug_(boolean b) {
    println(debugStream, -1, false, false, b);
  }

  /**
   * Prints the boolean without pre but with post but without newline.
   *
   * @param b - the boolean
   */
  public void _deb(boolean b) {
    print(debugStream, -1, false, false, b);
  }

  /**
   * Prints the boolean without pre but with post and newline.
   *
   * @param b - the boolean
   */
  public void _debug(boolean b) {
    println(debugStream, -1, false, false, b);
  }

  /**
   * Prints the boolean with pre but without post and newline.
   *
   * @param b - the boolean
   */
  public void deb_(boolean b) {
    print(debugStream, -1, true, true, b);
  }

  /**
   * Prints the boolean with pre but without post but with newline.
   *
   * @param b - the boolean
   */
  public void debug_(boolean b) {
    println(debugStream, -1, true, true, b);
  }

  /**
   * Prints the boolean with pre and post but without newline.
   *
   * @param b - the boolean
   */
  public void deb(boolean b) {
    print(debugStream, -1, true, true, b);
  }

  /**
   * Prints the boolean with pre and post and newline.
   *
   * @param b - the boolean
   */
  public void debug(boolean b) {
    println(debugStream, -1, true, true, b);
  }

  /**
   * Prints the char[] without pre and post and newline.
   *
   * @param s - the char[]
   */
  public void _deb_(char[] s) {
    print(debugStream, -1, false, false, s);
  }

  /**
   * Prints the char[] without pre and post but with newline.
   *
   * @param s - the char[]
   */
  public void _debug_(char[] s) {
    println(debugStream, -1, false, false, s);
  }

  /**
   * Prints the char[] without pre but with post but without newline.
   *
   * @param s - the char[]
   */
  public void _deb(char[] s) {
    print(debugStream, -1, false, false, s);
  }

  /**
   * Prints the char[] without pre but with post and newline.
   *
   * @param s - the char[]
   */
  public void _debug(char[] s) {
    println(debugStream, -1, false, false, s);
  }

  /**
   * Prints the char[] with pre but without post and newline.
   *
   * @param s - the char[]
   */
  public void deb_(char[] s) {
    print(debugStream, -1, true, true, s);
  }

  /**
   * Prints the char[] with pre but without post but with newline.
   *
   * @param s - the char[]
   */
  public void debug_(char[] s) {
    println(debugStream, -1, true, true, s);
  }

  /**
   * Prints the char[] with pre and post but without newline.
   *
   * @param s - the char[]
   */
  public void deb(char[] s) {
    print(debugStream, -1, true, true, s);
  }

  /**
   * Prints the char[] with pre and post and newline.
   *
   * @param s - the char[]
   */
  public void debug(char[] s) {
    println(debugStream, -1, true, true, s);
  }

  /**
   * Prints the double without pre and post and newline.
   *
   * @param d - the double
   */
  public void _deb_(double d) {
    print(debugStream, -1, false, false, d);
  }

  /**
   * Prints the double without pre and post but with newline.
   *
   * @param d - the double
   */
  public void _debug_(double d) {
    println(debugStream, -1, false, false, d);
  }

  /**
   * Prints the double without pre but with post but without newline.
   *
   * @param d - the double
   */
  public void _deb(double d) {
    print(debugStream, -1, false, false, d);
  }

  /**
   * Prints the double without pre but with post and newline.
   *
   * @param d - the double
   */
  public void _debug(double d) {
    println(debugStream, -1, false, false, d);
  }

  /**
   * Prints the double with pre but without post and newline.
   *
   * @param d - the double
   */
  public void deb_(double d) {
    print(debugStream, -1, true, true, d);
  }

  /**
   * Prints the double with pre but without post but with newline.
   *
   * @param d - the double
   */
  public void debug_(double d) {
    println(debugStream, -1, true, true, d);
  }

  /**
   * Prints the double with pre and post but without newline.
   *
   * @param d - the double
   */
  public void deb(double d) {
    print(debugStream, -1, true, true, d);
  }

  /**
   * Prints the double with pre and post and newline.
   *
   * @param d - the double
   */
  public void debug(double d) {
    println(debugStream, -1, true, true, d);
  }

  /**
   * Prints the float without pre and post and newline.
   *
   * @param f - the float
   */
  public void _deb_(float f) {
    print(debugStream, -1, false, false, f);
  }

  /**
   * Prints the float without pre and post but with newline.
   *
   * @param f - the float
   */
  public void _debug_(float f) {
    println(debugStream, -1, false, false, f);
  }

  /**
   * Prints the float without pre but with post but without newline.
   *
   * @param f - the float
   */
  public void _deb(float f) {
    print(debugStream, -1, false, false, f);
  }

  /**
   * Prints the float without pre but with post and newline.
   *
   * @param f - the float
   */
  public void _debug(float f) {
    println(debugStream, -1, false, false, f);
  }

  /**
   * Prints the float with pre but without post and newline.
   *
   * @param f - the float
   */
  public void deb_(float f) {
    print(debugStream, -1, true, true, f);
  }

  /**
   * Prints the float with pre but without post but with newline.
   *
   * @param f - the float
   */
  public void debug_(float f) {
    println(debugStream, -1, true, true, f);
  }

  /**
   * Prints the float with pre and post but without newline.
   *
   * @param f - the float
   */
  public void deb(float f) {
    print(debugStream, -1, true, true, f);
  }

  /**
   * Prints the float with pre and post and newline.
   *
   * @param f - the float
   */
  public void debug(float f) {
    println(debugStream, -1, true, true, f);
  }

  /**
   * Prints the int without pre and post and newline.
   *
   * @param i - the int
   */
  public void _deb_(int i) {
    print(debugStream, -1, false, false, i);
  }

  /**
   * Prints the int without pre and post but with newline.
   *
   * @param i - the int
   */
  public void _debug_(int i) {
    println(debugStream, -1, false, false, i);
  }

  /**
   * Prints the int without pre but with post but without newline.
   *
   * @param i - the int
   */
  public void _deb(int i) {
    print(debugStream, -1, false, false, i);
  }

  /**
   * Prints the int without pre but with post and newline.
   *
   * @param i - the int
   */
  public void _debug(int i) {
    println(debugStream, -1, false, false, i);
  }

  /**
   * Prints the int with pre but without post and newline.
   *
   * @param i - the int
   */
  public void deb_(int i) {
    print(debugStream, -1, true, true, i);
  }

  /**
   * Prints the int with pre but without post but with newline.
   *
   * @param i - the int
   */
  public void debug_(int i) {
    println(debugStream, -1, true, true, i);
  }

  /**
   * Prints the int with pre and post but without newline.
   *
   * @param i - the int
   */
  public void deb(int i) {
    print(debugStream, -1, true, true, i);
  }

  /**
   * Prints the int with pre and post and newline.
   *
   * @param i - the int
   */
  public void debug(int i) {
    println(debugStream, -1, true, true, i);
  }

  /**
   * Prints the long without pre and post and newline.
   *
   * @param l - the long
   */
  public void _deb_(long l) {
    print(debugStream, -1, false, false, l);
  }

  /**
   * Prints the long without pre and post but with newline.
   *
   * @param l - the long
   */
  public void _debug_(long l) {
    println(debugStream, -1, false, false, l);
  }

  /**
   * Prints the long without pre but with post but without newline.
   *
   * @param l - the long
   */
  public void _deb(long l) {
    print(debugStream, -1, false, false, l);
  }

  /**
   * Prints the long without pre but with post and newline.
   *
   * @param l - the long
   */
  public void _debug(long l) {
    println(debugStream, -1, false, false, l);
  }

  /**
   * Prints the long with pre but without post and newline.
   *
   * @param l - the long
   */
  public void deb_(long l) {
    print(debugStream, -1, true, true, l);
  }

  /**
   * Prints the long with pre but without post but with newline.
   *
   * @param l - the long
   */
  public void debug_(long l) {
    println(debugStream, -1, true, true, l);
  }

  /**
   * Prints the long with pre and post but without newline.
   *
   * @param l - the long
   */
  public void deb(long l) {
    print(debugStream, -1, true, true, l);
  }

  /**
   * Prints the long with pre and post and newline.
   *
   * @param l - the long
   */
  public void debug(long l) {
    println(debugStream, -1, true, true, l);
  }

  /**
   * Prints the object without pre and post and newline.
   *
   * @param obj - the Object
   */
  public void _deb_(Object obj) {
    print(debugStream, -1, false, false, obj);
  }

  /**
   * Prints the object without pre and post but with newline.
   *
   * @param obj - the Object
   */
  public void _debug_(Object obj) {
    println(debugStream, -1, false, false, obj);
  }

  /**
   * Prints the object without pre but with post but without newline.
   *
   * @param obj - the Object
   */
  public void _deb(Object obj) {
    print(debugStream, -1, false, false, obj);
  }

  /**
   * Prints the object without pre but with post and newline.
   *
   * @param obj - the Object
   */
  public void _debug(Object obj) {
    println(debugStream, -1, false, false, obj);
  }

  /**
   * Prints the object with pre but without post and newline.
   *
   * @param obj - the Object
   */
  public void deb_(Object obj) {
    print(debugStream, -1, true, true, obj);
  }

  /**
   * Prints the object with pre but without post but with newline.
   *
   * @param obj - the Object
   */
  public void debug_(Object obj) {
    println(debugStream, -1, true, true, obj);
  }

  /**
   * Prints the object with pre and post but without newline.
   *
   * @param obj - the Object
   */
  public void deb(Object obj) {
    print(debugStream, -1, true, true, obj);
  }

  /**
   * Prints the object with pre and post and newline.
   *
   * @param obj - the Object
   */
  public void debug(Object obj) {
    println(debugStream, -1, true, true, obj);
  }

  /**
   * Prints the string without pre and post and newline.
   *
   * @param s - the String
   */
  public void _deb_(String s) {
    print(debugStream, -1, false, false, s);
  }

  /**
   * Prints the string without pre and post but with newline.
   *
   * @param s - the String
   */
  public void _debug_(String s) {
    println(debugStream, -1, false, false, s);
  }

  /**
   * Prints the string without pre but with post but without newline.
   *
   * @param s - the String
   */
  public void _deb(String s) {
    print(debugStream, -1, false, false, s);
  }

  /**
   * Prints the string without pre but with post and newline.
   *
   * @param s - the String
   */
  public void _debug(String s) {
    println(debugStream, -1, false, false, s);
  }

  /**
   * Prints the string with pre but without post and newline.
   *
   * @param s - the String
   */
  public void deb_(String s) {
    print(debugStream, -1, true, true, s);
  }

  /**
   * Prints the string with pre but without post but with newline.
   *
   * @param s - the String
   */
  public void debug_(String s) {
    println(debugStream, -1, true, true, s);
  }

  /**
   * Prints the string with pre and post but without newline.
   *
   * @param s - the String
   */
  public void deb(String s) {
    print(debugStream, -1, true, true, s);
  }

  /**
   * Prints the string with pre and post and newline.
   *
   * @param s - the String
   */
  public void debug(String s) {
    println(debugStream, -1, true, true, s);
  }

  /**
   * Prints the format without pre and post and newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void _debf_(String format, Object... args) {
    format(debugStream, -1, false, false, format, args);
  }

  /**
   * Prints the format without pre and post but with newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void _debugf_(String format, Object... args) {
    formatln(debugStream, -1, false, false, format, args);
  }

  /**
   * Prints the format without pre but with post but without newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void _debf(String format, Object... args) {
    format(debugStream, -1, false, false, format, args);
  }

  /**
   * Prints the format without pre but with post and newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void _debugf(String format, Object... args) {
    formatln(debugStream, -1, false, false, format, args);
  }

  /**
   * Prints the format with pre but without post and newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void debf_(String format, Object... args) {
    format(debugStream, -1, true, true, format, args);
  }

  /**
   * Prints the format with pre but without post but with newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void debugf_(String format, Object... args) {
    formatln(debugStream, -1, true, true, format, args);
  }

  /**
   * Prints the format with pre and post but without newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void debf(String format, Object... args) {
    format(debugStream, -1, true, true, format, args);
  }

  /**
   * Prints the format with pre and post and newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void debugf(String format, Object... args) {
    formatln(debugStream, -1, true, true, format, args);
  }

  //endregion

  //region Info

  /**
   * Prints no string without pre and post and newline.
   */
  public void _inf_() {
    print(infoStream, 0, false, false);
  }

  /**
   * Prints no string without pre and post but with newline.
   */
  public void _info_() {
    println(infoStream, 0, false, false);
  }

  /**
   * Prints no string without pre but with post but without newline.
   */
  public void _inf() {
    print(infoStream, 0, false, false);
  }

  /**
   * Prints no string without pre but with post and newline.
   */
  public void _info() {
    println(infoStream, 0, false, false);
  }

  /**
   * Prints no string with pre but without post and newline.
   */
  public void inf_() {
    print(infoStream, 0, true, true);
  }

  /**
   * Prints no string with pre but without post but with newline.
   */
  public void info_() {
    println(infoStream, 0, true, true);
  }

  /**
   * Prints no string with pre and post but without newline.
   */
  public void inf() {
    print(infoStream, 0, true, true);
  }

  /**
   * Prints no string with pre and post and newline.
   */
  public void info() {
    println(infoStream, 0, true, true);
  }

  /**
   * Prints the boolean without pre and post and newline.
   *
   * @param b - the boolean
   */
  public void _inf_(boolean b) {
    print(infoStream, 0, false, false, b);
  }

  /**
   * Prints the boolean without pre and post but with newline.
   *
   * @param b - the boolean
   */
  public void _info_(boolean b) {
    println(infoStream, 0, false, false, b);
  }

  /**
   * Prints the boolean without pre but with post but without newline.
   *
   * @param b - the boolean
   */
  public void _inf(boolean b) {
    print(infoStream, 0, false, false, b);
  }

  /**
   * Prints the boolean without pre but with post and newline.
   *
   * @param b - the boolean
   */
  public void _info(boolean b) {
    println(infoStream, 0, false, false, b);
  }

  /**
   * Prints the boolean with pre but without post and newline.
   *
   * @param b - the boolean
   */
  public void inf_(boolean b) {
    print(infoStream, 0, true, true, b);
  }

  /**
   * Prints the boolean with pre but without post but with newline.
   *
   * @param b - the boolean
   */
  public void info_(boolean b) {
    println(infoStream, 0, true, true, b);
  }

  /**
   * Prints the boolean with pre and post but without newline.
   *
   * @param b - the boolean
   */
  public void inf(boolean b) {
    print(infoStream, 0, true, true, b);
  }

  /**
   * Prints the boolean with pre and post and newline.
   *
   * @param b - the boolean
   */
  public void info(boolean b) {
    println(infoStream, 0, true, true, b);
  }

  /**
   * Prints the char[] without pre and post and newline.
   *
   * @param s - the char[]
   */
  public void _inf_(char[] s) {
    print(infoStream, 0, false, false, s);
  }

  /**
   * Prints the char[] without pre and post but with newline.
   *
   * @param s - the char[]
   */
  public void _info_(char[] s) {
    println(infoStream, 0, false, false, s);
  }

  /**
   * Prints the char[] without pre but with post but without newline.
   *
   * @param s - the char[]
   */
  public void _inf(char[] s) {
    print(infoStream, 0, false, false, s);
  }

  /**
   * Prints the char[] without pre but with post and newline.
   *
   * @param s - the char[]
   */
  public void _info(char[] s) {
    println(infoStream, 0, false, false, s);
  }

  /**
   * Prints the char[] with pre but without post and newline.
   *
   * @param s - the char[]
   */
  public void inf_(char[] s) {
    print(infoStream, 0, true, true, s);
  }

  /**
   * Prints the char[] with pre but without post but with newline.
   *
   * @param s - the char[]
   */
  public void info_(char[] s) {
    println(infoStream, 0, true, true, s);
  }

  /**
   * Prints the char[] with pre and post but without newline.
   *
   * @param s - the char[]
   */
  public void inf(char[] s) {
    print(infoStream, 0, true, true, s);
  }

  /**
   * Prints the char[] with pre and post and newline.
   *
   * @param s - the char[]
   */
  public void info(char[] s) {
    println(infoStream, 0, true, true, s);
  }

  /**
   * Prints the double without pre and post and newline.
   *
   * @param d - the double
   */
  public void _inf_(double d) {
    print(infoStream, 0, false, false, d);
  }

  /**
   * Prints the double without pre and post but with newline.
   *
   * @param d - the double
   */
  public void _info_(double d) {
    println(infoStream, 0, false, false, d);
  }

  /**
   * Prints the double without pre but with post but without newline.
   *
   * @param d - the double
   */
  public void _inf(double d) {
    print(infoStream, 0, false, false, d);
  }

  /**
   * Prints the double without pre but with post and newline.
   *
   * @param d - the double
   */
  public void _info(double d) {
    println(infoStream, 0, false, false, d);
  }

  /**
   * Prints the double with pre but without post and newline.
   *
   * @param d - the double
   */
  public void inf_(double d) {
    print(infoStream, 0, true, true, d);
  }

  /**
   * Prints the double with pre but without post but with newline.
   *
   * @param d - the double
   */
  public void info_(double d) {
    println(infoStream, 0, true, true, d);
  }

  /**
   * Prints the double with pre and post but without newline.
   *
   * @param d - the double
   */
  public void inf(double d) {
    print(infoStream, 0, true, true, d);
  }

  /**
   * Prints the double with pre and post and newline.
   *
   * @param d - the double
   */
  public void info(double d) {
    println(infoStream, 0, true, true, d);
  }

  /**
   * Prints the float without pre and post and newline.
   *
   * @param f - the float
   */
  public void _inf_(float f) {
    print(infoStream, 0, false, false, f);
  }

  /**
   * Prints the float without pre and post but with newline.
   *
   * @param f - the float
   */
  public void _info_(float f) {
    println(infoStream, 0, false, false, f);
  }

  /**
   * Prints the float without pre but with post but without newline.
   *
   * @param f - the float
   */
  public void _inf(float f) {
    print(infoStream, 0, false, false, f);
  }

  /**
   * Prints the float without pre but with post and newline.
   *
   * @param f - the float
   */
  public void _info(float f) {
    println(infoStream, 0, false, false, f);
  }

  /**
   * Prints the float with pre but without post and newline.
   *
   * @param f - the float
   */
  public void inf_(float f) {
    print(infoStream, 0, true, true, f);
  }

  /**
   * Prints the float with pre but without post but with newline.
   *
   * @param f - the float
   */
  public void info_(float f) {
    println(infoStream, 0, true, true, f);
  }

  /**
   * Prints the float with pre and post but without newline.
   *
   * @param f - the float
   */
  public void inf(float f) {
    print(infoStream, 0, true, true, f);
  }

  /**
   * Prints the float with pre and post and newline.
   *
   * @param f - the float
   */
  public void info(float f) {
    println(infoStream, 0, true, true, f);
  }

  /**
   * Prints the int without pre and post and newline.
   *
   * @param i - the int
   */
  public void _inf_(int i) {
    print(infoStream, 0, false, false, i);
  }

  /**
   * Prints the int without pre and post but with newline.
   *
   * @param i - the int
   */
  public void _info_(int i) {
    println(infoStream, 0, false, false, i);
  }

  /**
   * Prints the int without pre but with post but without newline.
   *
   * @param i - the int
   */
  public void _inf(int i) {
    print(infoStream, 0, false, false, i);
  }

  /**
   * Prints the int without pre but with post and newline.
   *
   * @param i - the int
   */
  public void _info(int i) {
    println(infoStream, 0, false, false, i);
  }

  /**
   * Prints the int with pre but without post and newline.
   *
   * @param i - the int
   */
  public void inf_(int i) {
    print(infoStream, 0, true, true, i);
  }

  /**
   * Prints the int with pre but without post but with newline.
   *
   * @param i - the int
   */
  public void info_(int i) {
    println(infoStream, 0, true, true, i);
  }

  /**
   * Prints the int with pre and post but without newline.
   *
   * @param i - the int
   */
  public void inf(int i) {
    print(infoStream, 0, true, true, i);
  }

  /**
   * Prints the int with pre and post and newline.
   *
   * @param i - the int
   */
  public void info(int i) {
    println(infoStream, 0, true, true, i);
  }

  /**
   * Prints the long without pre and post and newline.
   *
   * @param l - the long
   */
  public void _inf_(long l) {
    print(infoStream, 0, false, false, l);
  }

  /**
   * Prints the long without pre and post but with newline.
   *
   * @param l - the long
   */
  public void _info_(long l) {
    println(infoStream, 0, false, false, l);
  }

  /**
   * Prints the long without pre but with post but without newline.
   *
   * @param l - the long
   */
  public void _inf(long l) {
    print(infoStream, 0, false, false, l);
  }

  /**
   * Prints the long without pre but with post and newline.
   *
   * @param l - the long
   */
  public void _info(long l) {
    println(infoStream, 0, false, false, l);
  }

  /**
   * Prints the long with pre but without post and newline.
   *
   * @param l - the long
   */
  public void inf_(long l) {
    print(infoStream, 0, true, true, l);
  }

  /**
   * Prints the long with pre but without post but with newline.
   *
   * @param l - the long
   */
  public void info_(long l) {
    println(infoStream, 0, true, true, l);
  }

  /**
   * Prints the long with pre and post but without newline.
   *
   * @param l - the long
   */
  public void inf(long l) {
    print(infoStream, 0, true, true, l);
  }

  /**
   * Prints the long with pre and post and newline.
   *
   * @param l - the long
   */
  public void info(long l) {
    println(infoStream, 0, true, true, l);
  }

  /**
   * Prints the object without pre and post and newline.
   *
   * @param obj - the Object
   */
  public void _inf_(Object obj) {
    print(infoStream, 0, false, false, obj);
  }

  /**
   * Prints the object without pre and post but with newline.
   *
   * @param obj - the Object
   */
  public void _info_(Object obj) {
    println(infoStream, 0, false, false, obj);
  }

  /**
   * Prints the object without pre but with post but without newline.
   *
   * @param obj - the Object
   */
  public void _inf(Object obj) {
    print(infoStream, 0, false, false, obj);
  }

  /**
   * Prints the object without pre but with post and newline.
   *
   * @param obj - the Object
   */
  public void _info(Object obj) {
    println(infoStream, 0, false, false, obj);
  }

  /**
   * Prints the object with pre but without post and newline.
   *
   * @param obj - the Object
   */
  public void inf_(Object obj) {
    print(infoStream, 0, true, true, obj);
  }

  /**
   * Prints the object with pre but without post but with newline.
   *
   * @param obj - the Object
   */
  public void info_(Object obj) {
    println(infoStream, 0, true, true, obj);
  }

  /**
   * Prints the object with pre and post but without newline.
   *
   * @param obj - the Object
   */
  public void inf(Object obj) {
    print(infoStream, 0, true, true, obj);
  }

  /**
   * Prints the object with pre and post and newline.
   *
   * @param obj - the Object
   */
  public void info(Object obj) {
    println(infoStream, 0, true, true, obj);
  }

  /**
   * Prints the string without pre and post and newline.
   *
   * @param s - the String
   */
  public void _inf_(String s) {
    print(infoStream, 0, false, false, s);
  }

  /**
   * Prints the string without pre and post but with newline.
   *
   * @param s - the String
   */
  public void _info_(String s) {
    println(infoStream, 0, false, false, s);
  }

  /**
   * Prints the string without pre but with post but without newline.
   *
   * @param s - the String
   */
  public void _inf(String s) {
    print(infoStream, 0, false, false, s);
  }

  /**
   * Prints the string without pre but with post and newline.
   *
   * @param s - the String
   */
  public void _info(String s) {
    println(infoStream, 0, false, false, s);
  }

  /**
   * Prints the string with pre but without post and newline.
   *
   * @param s - the String
   */
  public void inf_(String s) {
    print(infoStream, 0, true, true, s);
  }

  /**
   * Prints the string with pre but without post but with newline.
   *
   * @param s - the String
   */
  public void info_(String s) {
    println(infoStream, 0, true, true, s);
  }

  /**
   * Prints the string with pre and post but without newline.
   *
   * @param s - the String
   */
  public void inf(String s) {
    print(infoStream, 0, true, true, s);
  }

  /**
   * Prints the string with pre and post and newline.
   *
   * @param s - the String
   */
  public void info(String s) {
    println(infoStream, 0, true, true, s);
  }

  /**
   * Prints the format without pre and post and newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void _inff_(String format, Object... args) {
    format(infoStream, 0, false, false, format, args);
  }

  /**
   * Prints the format without pre and post but with newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void _infof_(String format, Object... args) {
    formatln(infoStream, 0, false, false, format, args);
  }

  /**
   * Prints the format without pre but with post but without newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void _inff(String format, Object... args) {
    format(infoStream, 0, false, false, format, args);
  }

  /**
   * Prints the format without pre but with post and newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void _infof(String format, Object... args) {
    formatln(infoStream, 0, false, false, format, args);
  }

  /**
   * Prints the format with pre but without post and newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void inff_(String format, Object... args) {
    format(infoStream, 0, true, true, format, args);
  }

  /**
   * Prints the format with pre but without post but with newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void infof_(String format, Object... args) {
    formatln(infoStream, 0, true, true, format, args);
  }

  /**
   * Prints the format with pre and post but without newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void inff(String format, Object... args) {
    format(infoStream, 0, true, true, format, args);
  }

  /**
   * Prints the format with pre and post and newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void infof(String format, Object... args) {
    formatln(infoStream, 0, true, true, format, args);
  }

  //endregion

  //region Warn

  /**
   * Prints no string without pre and post and newline.
   */
  public void _war_() {
    print(warnStream, 1, false, false);
  }

  /**
   * Prints no string without pre and post but with newline.
   */
  public void _warn_() {
    println(warnStream, 1, false, false);
  }

  /**
   * Prints no string without pre but with post but without newline.
   */
  public void _war() {
    print(warnStream, 1, false, false);
  }

  /**
   * Prints no string without pre but with post and newline.
   */
  public void _warn() {
    println(warnStream, 1, false, false);
  }

  /**
   * Prints no string with pre but without post and newline.
   */
  public void war_() {
    print(warnStream, 1, true, true);
  }

  /**
   * Prints no string with pre but without post but with newline.
   */
  public void warn_() {
    println(warnStream, 1, true, true);
  }

  /**
   * Prints no string with pre and post but without newline.
   */
  public void war() {
    print(warnStream, 1, true, true);
  }

  /**
   * Prints no string with pre and post and newline.
   */
  public void warn() {
    println(warnStream, 1, true, true);
  }

  /**
   * Prints the boolean without pre and post and newline.
   *
   * @param b - the boolean
   */
  public void _war_(boolean b) {
    print(warnStream, 1, false, false, b);
  }

  /**
   * Prints the boolean without pre and post but with newline.
   *
   * @param b - the boolean
   */
  public void _warn_(boolean b) {
    println(warnStream, 1, false, false, b);
  }

  /**
   * Prints the boolean without pre but with post but without newline.
   *
   * @param b - the boolean
   */
  public void _war(boolean b) {
    print(warnStream, 1, false, false, b);
  }

  /**
   * Prints the boolean without pre but with post and newline.
   *
   * @param b - the boolean
   */
  public void _warn(boolean b) {
    println(warnStream, 1, false, false, b);
  }

  /**
   * Prints the boolean with pre but without post and newline.
   *
   * @param b - the boolean
   */
  public void war_(boolean b) {
    print(warnStream, 1, true, true, b);
  }

  /**
   * Prints the boolean with pre but without post but with newline.
   *
   * @param b - the boolean
   */
  public void warn_(boolean b) {
    println(warnStream, 1, true, true, b);
  }

  /**
   * Prints the boolean with pre and post but without newline.
   *
   * @param b - the boolean
   */
  public void war(boolean b) {
    print(warnStream, 1, true, true, b);
  }

  /**
   * Prints the boolean with pre and post and newline.
   *
   * @param b - the boolean
   */
  public void warn(boolean b) {
    println(warnStream, 1, true, true, b);
  }

  /**
   * Prints the char[] without pre and post and newline.
   *
   * @param s - the char[]
   */
  public void _war_(char[] s) {
    print(warnStream, 1, false, false, s);
  }

  /**
   * Prints the char[] without pre and post but with newline.
   *
   * @param s - the char[]
   */
  public void _warn_(char[] s) {
    println(warnStream, 1, false, false, s);
  }

  /**
   * Prints the char[] without pre but with post but without newline.
   *
   * @param s - the char[]
   */
  public void _war(char[] s) {
    print(warnStream, 1, false, false, s);
  }

  /**
   * Prints the char[] without pre but with post and newline.
   *
   * @param s - the char[]
   */
  public void _warn(char[] s) {
    println(warnStream, 1, false, false, s);
  }

  /**
   * Prints the char[] with pre but without post and newline.
   *
   * @param s - the char[]
   */
  public void war_(char[] s) {
    print(warnStream, 1, true, true, s);
  }

  /**
   * Prints the char[] with pre but without post but with newline.
   *
   * @param s - the char[]
   */
  public void warn_(char[] s) {
    println(warnStream, 1, true, true, s);
  }

  /**
   * Prints the char[] with pre and post but without newline.
   *
   * @param s - the char[]
   */
  public void war(char[] s) {
    print(warnStream, 1, true, true, s);
  }

  /**
   * Prints the char[] with pre and post and newline.
   *
   * @param s - the char[]
   */
  public void warn(char[] s) {
    println(warnStream, 1, true, true, s);
  }

  /**
   * Prints the double without pre and post and newline.
   *
   * @param d - the double
   */
  public void _war_(double d) {
    print(warnStream, 1, false, false, d);
  }

  /**
   * Prints the double without pre and post but with newline.
   *
   * @param d - the double
   */
  public void _warn_(double d) {
    println(warnStream, 1, false, false, d);
  }

  /**
   * Prints the double without pre but with post but without newline.
   *
   * @param d - the double
   */
  public void _war(double d) {
    print(warnStream, 1, false, false, d);
  }

  /**
   * Prints the double without pre but with post and newline.
   *
   * @param d - the double
   */
  public void _warn(double d) {
    println(warnStream, 1, false, false, d);
  }

  /**
   * Prints the double with pre but without post and newline.
   *
   * @param d - the double
   */
  public void war_(double d) {
    print(warnStream, 1, true, true, d);
  }

  /**
   * Prints the double with pre but without post but with newline.
   *
   * @param d - the double
   */
  public void warn_(double d) {
    println(warnStream, 1, true, true, d);
  }

  /**
   * Prints the double with pre and post but without newline.
   *
   * @param d - the double
   */
  public void war(double d) {
    print(warnStream, 1, true, true, d);
  }

  /**
   * Prints the double with pre and post and newline.
   *
   * @param d - the double
   */
  public void warn(double d) {
    println(warnStream, 1, true, true, d);
  }

  /**
   * Prints the float without pre and post and newline.
   *
   * @param f - the float
   */
  public void _war_(float f) {
    print(warnStream, 1, false, false, f);
  }

  /**
   * Prints the float without pre and post but with newline.
   *
   * @param f - the float
   */
  public void _warn_(float f) {
    println(warnStream, 1, false, false, f);
  }

  /**
   * Prints the float without pre but with post but without newline.
   *
   * @param f - the float
   */
  public void _war(float f) {
    print(warnStream, 1, false, false, f);
  }

  /**
   * Prints the float without pre but with post and newline.
   *
   * @param f - the float
   */
  public void _warn(float f) {
    println(warnStream, 1, false, false, f);
  }

  /**
   * Prints the float with pre but without post and newline.
   *
   * @param f - the float
   */
  public void war_(float f) {
    print(warnStream, 1, true, true, f);
  }

  /**
   * Prints the float with pre but without post but with newline.
   *
   * @param f - the float
   */
  public void warn_(float f) {
    println(warnStream, 1, true, true, f);
  }

  /**
   * Prints the float with pre and post but without newline.
   *
   * @param f - the float
   */
  public void war(float f) {
    print(warnStream, 1, true, true, f);
  }

  /**
   * Prints the float with pre and post and newline.
   *
   * @param f - the float
   */
  public void warn(float f) {
    println(warnStream, 1, true, true, f);
  }

  /**
   * Prints the int without pre and post and newline.
   *
   * @param i - the int
   */
  public void _war_(int i) {
    print(warnStream, 1, false, false, i);
  }

  /**
   * Prints the int without pre and post but with newline.
   *
   * @param i - the int
   */
  public void _warn_(int i) {
    println(warnStream, 1, false, false, i);
  }

  /**
   * Prints the int without pre but with post but without newline.
   *
   * @param i - the int
   */
  public void _war(int i) {
    print(warnStream, 1, false, false, i);
  }

  /**
   * Prints the int without pre but with post and newline.
   *
   * @param i - the int
   */
  public void _warn(int i) {
    println(warnStream, 1, false, false, i);
  }

  /**
   * Prints the int with pre but without post and newline.
   *
   * @param i - the int
   */
  public void war_(int i) {
    print(warnStream, 1, true, true, i);
  }

  /**
   * Prints the int with pre but without post but with newline.
   *
   * @param i - the int
   */
  public void warn_(int i) {
    println(warnStream, 1, true, true, i);
  }

  /**
   * Prints the int with pre and post but without newline.
   *
   * @param i - the int
   */
  public void war(int i) {
    print(warnStream, 1, true, true, i);
  }

  /**
   * Prints the int with pre and post and newline.
   *
   * @param i - the int
   */
  public void warn(int i) {
    println(warnStream, 1, true, true, i);
  }

  /**
   * Prints the long without pre and post and newline.
   *
   * @param l - the long
   */
  public void _war_(long l) {
    print(warnStream, 1, false, false, l);
  }

  /**
   * Prints the long without pre and post but with newline.
   *
   * @param l - the long
   */
  public void _warn_(long l) {
    println(warnStream, 1, false, false, l);
  }

  /**
   * Prints the long without pre but with post but without newline.
   *
   * @param l - the long
   */
  public void _war(long l) {
    print(warnStream, 1, false, false, l);
  }

  /**
   * Prints the long without pre but with post and newline.
   *
   * @param l - the long
   */
  public void _warn(long l) {
    println(warnStream, 1, false, false, l);
  }

  /**
   * Prints the long with pre but without post and newline.
   *
   * @param l - the long
   */
  public void war_(long l) {
    print(warnStream, 1, true, true, l);
  }

  /**
   * Prints the long with pre but without post but with newline.
   *
   * @param l - the long
   */
  public void warn_(long l) {
    println(warnStream, 1, true, true, l);
  }

  /**
   * Prints the long with pre and post but without newline.
   *
   * @param l - the long
   */
  public void war(long l) {
    print(warnStream, 1, true, true, l);
  }

  /**
   * Prints the long with pre and post and newline.
   *
   * @param l - the long
   */
  public void warn(long l) {
    println(warnStream, 1, true, true, l);
  }

  /**
   * Prints the object without pre and post and newline.
   *
   * @param obj - the Object
   */
  public void _war_(Object obj) {
    print(warnStream, 1, false, false, obj);
  }

  /**
   * Prints the object without pre and post but with newline.
   *
   * @param obj - the Object
   */
  public void _warn_(Object obj) {
    println(warnStream, 1, false, false, obj);
  }

  /**
   * Prints the object without pre but with post but without newline.
   *
   * @param obj - the Object
   */
  public void _war(Object obj) {
    print(warnStream, 1, false, false, obj);
  }

  /**
   * Prints the object without pre but with post and newline.
   *
   * @param obj - the Object
   */
  public void _warn(Object obj) {
    println(warnStream, 1, false, false, obj);
  }

  /**
   * Prints the object with pre but without post and newline.
   *
   * @param obj - the Object
   */
  public void war_(Object obj) {
    print(warnStream, 1, true, true, obj);
  }

  /**
   * Prints the object with pre but without post but with newline.
   *
   * @param obj - the Object
   */
  public void warn_(Object obj) {
    println(warnStream, 1, true, true, obj);
  }

  /**
   * Prints the object with pre and post but without newline.
   *
   * @param obj - the Object
   */
  public void war(Object obj) {
    print(warnStream, 1, true, true, obj);
  }

  /**
   * Prints the object with pre and post and newline.
   *
   * @param obj - the Object
   */
  public void warn(Object obj) {
    println(warnStream, 1, true, true, obj);
  }

  /**
   * Prints the string without pre and post and newline.
   *
   * @param s - the String
   */
  public void _war_(String s) {
    print(warnStream, 1, false, false, s);
  }

  /**
   * Prints the string without pre and post but with newline.
   *
   * @param s - the String
   */
  public void _warn_(String s) {
    println(warnStream, 1, false, false, s);
  }

  /**
   * Prints the string without pre but with post but without newline.
   *
   * @param s - the String
   */
  public void _war(String s) {
    print(warnStream, 1, false, false, s);
  }

  /**
   * Prints the string without pre but with post and newline.
   *
   * @param s - the String
   */
  public void _warn(String s) {
    println(warnStream, 1, false, false, s);
  }

  /**
   * Prints the string with pre but without post and newline.
   *
   * @param s - the String
   */
  public void war_(String s) {
    print(warnStream, 1, true, true, s);
  }

  /**
   * Prints the string with pre but without post but with newline.
   *
   * @param s - the String
   */
  public void warn_(String s) {
    println(warnStream, 1, true, true, s);
  }

  /**
   * Prints the string with pre and post but without newline.
   *
   * @param s - the String
   */
  public void war(String s) {
    print(warnStream, 1, true, true, s);
  }

  /**
   * Prints the string with pre and post and newline.
   *
   * @param s - the String
   */
  public void warn(String s) {
    println(warnStream, 1, true, true, s);
  }

  /**
   * Prints the format without pre and post and newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void _warf_(String format, Object... args) {
    format(warnStream, 1, false, false, format, args);
  }

  /**
   * Prints the format without pre and post but with newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void _warnf_(String format, Object... args) {
    formatln(warnStream, 1, false, false, format, args);
  }

  /**
   * Prints the format without pre but with post but without newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void _warf(String format, Object... args) {
    format(warnStream, 1, false, false, format, args);
  }

  /**
   * Prints the format without pre but with post and newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void _warnf(String format, Object... args) {
    formatln(warnStream, 1, false, false, format, args);
  }

  /**
   * Prints the format with pre but without post and newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void warf_(String format, Object... args) {
    format(warnStream, 1, true, true, format, args);
  }

  /**
   * Prints the format with pre but without post but with newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void warnf_(String format, Object... args) {
    formatln(warnStream, 1, true, true, format, args);
  }

  /**
   * Prints the format with pre and post but without newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void warf(String format, Object... args) {
    format(warnStream, 1, true, true, format, args);
  }

  /**
   * Prints the format with pre and post and newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void warnf(String format, Object... args) {
    formatln(warnStream, 1, true, true, format, args);
  }

  //endregion

  //region Error

  /**
   * Prints no string without pre and post and newline.
   *

   */
  public void _err_() {
    print(errorStream, 2, false, false);
  }

  /**
   * Prints no string without pre and post but with newline.
   *

   */
  public void _error_() {
    println(errorStream, 2, false, false);
  }

  /**
   * Prints no string without pre but with post but without newline.
   *

   */
  public void _err() {
    print(errorStream, 2, false, false);
  }

  /**
   * Prints no string without pre but with post and newline.
   *

   */
  public void _error() {
    println(errorStream, 2, false, false);
  }

  /**
   * Prints no string with pre but without post and newline.
   *

   */
  public void err_() {
    print(errorStream, 2, true, true);
  }

  /**
   * Prints no string with pre but without post but with newline.
   *

   */
  public void error_() {
    println(errorStream, 2, true, true);
  }

  /**
   * Prints no string with pre and post but without newline.
   *

   */
  public void err() {
    print(errorStream, 2, true, true);
  }

  /**
   * Prints no string with pre and post and newline.
   *

   */
  public void error() {
    println(errorStream, 2, true, true);
  }

  /**
   * Prints the boolean without pre and post and newline.
   *
   * @param b - the boolean
   */
  public void _err_(boolean b) {
    print(errorStream, 2, false, false, b);
  }

  /**
   * Prints the boolean without pre and post but with newline.
   *
   * @param b - the boolean
   */
  public void _error_(boolean b) {
    println(errorStream, 2, false, false, b);
  }

  /**
   * Prints the boolean without pre but with post but without newline.
   *
   * @param b - the boolean
   */
  public void _err(boolean b) {
    print(errorStream, 2, false, false, b);
  }

  /**
   * Prints the boolean without pre but with post and newline.
   *
   * @param b - the boolean
   */
  public void _error(boolean b) {
    println(errorStream, 2, false, false, b);
  }

  /**
   * Prints the boolean with pre but without post and newline.
   *
   * @param b - the boolean
   */
  public void err_(boolean b) {
    print(errorStream, 2, true, true, b);
  }

  /**
   * Prints the boolean with pre but without post but with newline.
   *
   * @param b - the boolean
   */
  public void error_(boolean b) {
    println(errorStream, 2, true, true, b);
  }

  /**
   * Prints the boolean with pre and post but without newline.
   *
   * @param b - the boolean
   */
  public void err(boolean b) {
    print(errorStream, 2, true, true, b);
  }

  /**
   * Prints the boolean with pre and post and newline.
   *
   * @param b - the boolean
   */
  public void error(boolean b) {
    println(errorStream, 2, true, true, b);
  }

  /**
   * Prints the char[] without pre and post and newline.
   *
   * @param s - the char[]
   */
  public void _err_(char[] s) {
    print(errorStream, 2, false, false, s);
  }

  /**
   * Prints the char[] without pre and post but with newline.
   *
   * @param s - the char[]
   */
  public void _error_(char[] s) {
    println(errorStream, 2, false, false, s);
  }

  /**
   * Prints the char[] without pre but with post but without newline.
   *
   * @param s - the char[]
   */
  public void _err(char[] s) {
    print(errorStream, 2, false, false, s);
  }

  /**
   * Prints the char[] without pre but with post and newline.
   *
   * @param s - the char[]
   */
  public void _error(char[] s) {
    println(errorStream, 2, false, false, s);
  }

  /**
   * Prints the char[] with pre but without post and newline.
   *
   * @param s - the char[]
   */
  public void err_(char[] s) {
    print(errorStream, 2, true, true, s);
  }

  /**
   * Prints the char[] with pre but without post but with newline.
   *
   * @param s - the char[]
   */
  public void error_(char[] s) {
    println(errorStream, 2, true, true, s);
  }

  /**
   * Prints the char[] with pre and post but without newline.
   *
   * @param s - the char[]
   */
  public void err(char[] s) {
    print(errorStream, 2, true, true, s);
  }

  /**
   * Prints the char[] with pre and post and newline.
   *
   * @param s - the char[]
   */
  public void error(char[] s) {
    println(errorStream, 2, true, true, s);
  }

  /**
   * Prints the double without pre and post and newline.
   *
   * @param d - the double
   */
  public void _err_(double d) {
    print(errorStream, 2, false, false, d);
  }

  /**
   * Prints the double without pre and post but with newline.
   *
   * @param d - the double
   */
  public void _error_(double d) {
    println(errorStream, 2, false, false, d);
  }

  /**
   * Prints the double without pre but with post but without newline.
   *
   * @param d - the double
   */
  public void _err(double d) {
    print(errorStream, 2, false, false, d);
  }

  /**
   * Prints the double without pre but with post and newline.
   *
   * @param d - the double
   */
  public void _error(double d) {
    println(errorStream, 2, false, false, d);
  }

  /**
   * Prints the double with pre but without post and newline.
   *
   * @param d - the double
   */
  public void err_(double d) {
    print(errorStream, 2, true, true, d);
  }

  /**
   * Prints the double with pre but without post but with newline.
   *
   * @param d - the double
   */
  public void error_(double d) {
    println(errorStream, 2, true, true, d);
  }

  /**
   * Prints the double with pre and post but without newline.
   *
   * @param d - the double
   */
  public void err(double d) {
    print(errorStream, 2, true, true, d);
  }

  /**
   * Prints the double with pre and post and newline.
   *
   * @param d - the double
   */
  public void error(double d) {
    println(errorStream, 2, true, true, d);
  }

  /**
   * Prints the float without pre and post and newline.
   *
   * @param f - the float
   */
  public void _err_(float f) {
    print(errorStream, 2, false, false, f);
  }

  /**
   * Prints the float without pre and post but with newline.
   *
   * @param f - the float
   */
  public void _error_(float f) {
    println(errorStream, 2, false, false, f);
  }

  /**
   * Prints the float without pre but with post but without newline.
   *
   * @param f - the float
   */
  public void _err(float f) {
    print(errorStream, 2, false, false, f);
  }

  /**
   * Prints the float without pre but with post and newline.
   *
   * @param f - the float
   */
  public void _error(float f) {
    println(errorStream, 2, false, false, f);
  }

  /**
   * Prints the float with pre but without post and newline.
   *
   * @param f - the float
   */
  public void err_(float f) {
    print(errorStream, 2, true, true, f);
  }

  /**
   * Prints the float with pre but without post but with newline.
   *
   * @param f - the float
   */
  public void error_(float f) {
    println(errorStream, 2, true, true, f);
  }

  /**
   * Prints the float with pre and post but without newline.
   *
   * @param f - the float
   */
  public void err(float f) {
    print(errorStream, 2, true, true, f);
  }

  /**
   * Prints the float with pre and post and newline.
   *
   * @param f - the float
   */
  public void error(float f) {
    println(errorStream, 2, true, true, f);
  }

  /**
   * Prints the int without pre and post and newline.
   *
   * @param i - the int
   */
  public void _err_(int i) {
    print(errorStream, 2, false, false, i);
  }

  /**
   * Prints the int without pre and post but with newline.
   *
   * @param i - the int
   */
  public void _error_(int i) {
    println(errorStream, 2, false, false, i);
  }

  /**
   * Prints the int without pre but with post but without newline.
   *
   * @param i - the int
   */
  public void _err(int i) {
    print(errorStream, 2, false, false, i);
  }

  /**
   * Prints the int without pre but with post and newline.
   *
   * @param i - the int
   */
  public void _error(int i) {
    println(errorStream, 2, false, false, i);
  }

  /**
   * Prints the int with pre but without post and newline.
   *
   * @param i - the int
   */
  public void err_(int i) {
    print(errorStream, 2, true, true, i);
  }

  /**
   * Prints the int with pre but without post but with newline.
   *
   * @param i - the int
   */
  public void error_(int i) {
    println(errorStream, 2, true, true, i);
  }

  /**
   * Prints the int with pre and post but without newline.
   *
   * @param i - the int
   */
  public void err(int i) {
    print(errorStream, 2, true, true, i);
  }

  /**
   * Prints the int with pre and post and newline.
   *
   * @param i - the int
   */
  public void error(int i) {
    println(errorStream, 2, true, true, i);
  }

  /**
   * Prints the long without pre and post and newline.
   *
   * @param l - the long
   */
  public void _err_(long l) {
    print(errorStream, 2, false, false, l);
  }

  /**
   * Prints the long without pre and post but with newline.
   *
   * @param l - the long
   */
  public void _error_(long l) {
    println(errorStream, 2, false, false, l);
  }

  /**
   * Prints the long without pre but with post but without newline.
   *
   * @param l - the long
   */
  public void _err(long l) {
    print(errorStream, 2, false, false, l);
  }

  /**
   * Prints the long without pre but with post and newline.
   *
   * @param l - the long
   */
  public void _error(long l) {
    println(errorStream, 2, false, false, l);
  }

  /**
   * Prints the long with pre but without post and newline.
   *
   * @param l - the long
   */
  public void err_(long l) {
    print(errorStream, 2, true, true, l);
  }

  /**
   * Prints the long with pre but without post but with newline.
   *
   * @param l - the long
   */
  public void error_(long l) {
    println(errorStream, 2, true, true, l);
  }

  /**
   * Prints the long with pre and post but without newline.
   *
   * @param l - the long
   */
  public void err(long l) {
    print(errorStream, 2, true, true, l);
  }

  /**
   * Prints the long with pre and post and newline.
   *
   * @param l - the long
   */
  public void error(long l) {
    println(errorStream, 2, true, true, l);
  }

  /**
   * Prints the object without pre and post and newline.
   *
   * @param obj - the Object
   */
  public void _err_(Object obj) {
    print(errorStream, 2, false, false, obj);
  }

  /**
   * Prints the object without pre and post but with newline.
   *
   * @param obj - the Object
   */
  public void _error_(Object obj) {
    println(errorStream, 2, false, false, obj);
  }

  /**
   * Prints the object without pre but with post but without newline.
   *
   * @param obj - the Object
   */
  public void _err(Object obj) {
    print(errorStream, 2, false, false, obj);
  }

  /**
   * Prints the object without pre but with post and newline.
   *
   * @param obj - the Object
   */
  public void _error(Object obj) {
    println(errorStream, 2, false, false, obj);
  }

  /**
   * Prints the object with pre but without post and newline.
   *
   * @param obj - the Object
   */
  public void err_(Object obj) {
    print(errorStream, 2, true, true, obj);
  }

  /**
   * Prints the object with pre but without post but with newline.
   *
   * @param obj - the Object
   */
  public void error_(Object obj) {
    println(errorStream, 2, true, true, obj);
  }

  /**
   * Prints the object with pre and post but without newline.
   *
   * @param obj - the Object
   */
  public void err(Object obj) {
    print(errorStream, 2, true, true, obj);
  }

  /**
   * Prints the object with pre and post and newline.
   *
   * @param obj - the Object
   */
  public void error(Object obj) {
    println(errorStream, 2, true, true, obj);
  }

  /**
   * Prints the string without pre and post and newline.
   *
   * @param s - the String
   */
  public void _err_(String s) {
    print(errorStream, 2, false, false, s);
  }

  /**
   * Prints the string without pre and post but with newline.
   *
   * @param s - the String
   */
  public void _error_(String s) {
    println(errorStream, 2, false, false, s);
  }

  /**
   * Prints the string without pre but with post but without newline.
   *
   * @param s - the String
   */
  public void _err(String s) {
    print(errorStream, 2, false, false, s);
  }

  /**
   * Prints the string without pre but with post and newline.
   *
   * @param s - the String
   */
  public void _error(String s) {
    println(errorStream, 2, false, false, s);
  }

  /**
   * Prints the string with pre but without post and newline.
   *
   * @param s - the String
   */
  public void err_(String s) {
    print(errorStream, 2, true, true, s);
  }

  /**
   * Prints the string with pre but without post but with newline.
   *
   * @param s - the String
   */
  public void error_(String s) {
    println(errorStream, 2, true, true, s);
  }

  /**
   * Prints the string with pre and post but without newline.
   *
   * @param s - the String
   */
  public void err(String s) {
    print(errorStream, 2, true, true, s);
  }

  /**
   * Prints the string with pre and post and newline.
   *
   * @param s - the String
   */
  public void error(String s) {
    println(errorStream, 2, true, true, s);
  }

  /**
   * Prints the format without pre and post and newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void _errf_(String format, Object... args) {
    format(errorStream, 2, false, false, format, args);
  }

  /**
   * Prints the format without pre and post but with newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void _errorf_(String format, Object... args) {
    formatln(errorStream, 2, false, false, format, args);
  }

  /**
   * Prints the format without pre but with post but without newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void _errf(String format, Object... args) {
    format(errorStream, 2, false, false, format, args);
  }

  /**
   * Prints the format without pre but with post and newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void _errorf(String format, Object... args) {
    formatln(errorStream, 2, false, false, format, args);
  }

  /**
   * Prints the format with pre but without post and newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void errf_(String format, Object... args) {
    format(errorStream, 2, true, true, format, args);
  }

  /**
   * Prints the format with pre but without post but with newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void errorf_(String format, Object... args) {
    formatln(errorStream, 2, true, true, format, args);
  }

  /**
   * Prints the format with pre and post but without newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void errf(String format, Object... args) {
    format(errorStream, 2, true, true, format, args);
  }

  /**
   * Prints the format with pre and post and newline.
   *
   * @param format - the String
   * @param args - the arguments
   */
  public void errorf(String format, Object... args) {
    formatln(errorStream, 2, true, true, format, args);
  }

  //endregion

  //endregion

}
