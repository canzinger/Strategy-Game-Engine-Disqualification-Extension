package dev.entze.sge.util.unit;

public interface Unit {

  long convert(long sourceItem, Unit sourceUnit);

  Unit[] allValues();

  String toShortString();

  /**
   * From TimeUnit.java General conversion utility.
   *
   * @param d duration
   * @param dst result unit scale
   * @param src source unit scale
   */
  static long generalConversionUtility(long d, long dst, long src) {
    long r, m;
    if (src == dst) {
      return d;
    } else if (src < dst) {
      return d / (dst / src);
    } else if (d > (m = Long.MAX_VALUE / (r = src / dst))) {
      return Long.MAX_VALUE;
    } else if (d < -m) {
      return Long.MIN_VALUE;
    } else {
      return d * r;
    }
  }

}
