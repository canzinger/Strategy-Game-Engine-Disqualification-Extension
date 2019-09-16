package at.ac.tuwien.ifs.sge.util.unit;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TimeUnitWrapper implements Unit {

  private final TimeUnit timeUnit;

  public TimeUnitWrapper() {
    this(TimeUnit.SECONDS);
  }

  public TimeUnitWrapper(TimeUnit timeUnit) {
    this.timeUnit = timeUnit;
  }

  @Override
  public long convert(long sourceItem, Unit sourceUnit) {
    if (sourceUnit instanceof TimeUnitWrapper) {
      return timeUnit.convert(sourceItem, ((TimeUnitWrapper) sourceUnit).timeUnit);
    }

    return sourceItem;
  }

  @Override
  public Unit[] allValues() {
    TimeUnit[] timeUnits = TimeUnit.values();
    Unit[] values = new Unit[timeUnits.length];

    for (int i = 0; i < values.length; i++) {
      values[i] = new TimeUnitWrapper(timeUnits[i]);
    }

    return values;
  }

  @Override
  public String toShortString() {
    switch (timeUnit) {
      case DAYS:
        return "d";
      case HOURS:
        return "h";
      case MINUTES:
        return "m";
      case SECONDS:
        return "s";
      case MILLISECONDS:
        return "ms";
      case MICROSECONDS:
        return "µs";
      case NANOSECONDS:
        return "ns";
    }
    return null;
  }

  @Override
  public String toString() {
    return timeUnit.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TimeUnitWrapper that = (TimeUnitWrapper) o;
    return timeUnit == that.timeUnit;
  }

  @Override
  public int hashCode() {
    return Objects.hash(timeUnit);
  }
}
