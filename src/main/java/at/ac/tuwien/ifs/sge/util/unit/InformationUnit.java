package at.ac.tuwien.ifs.sge.util.unit;

public enum InformationUnit implements Unit {

  BIT("b", InformationUnit.BIT_SCALE),
  BYTE("B", InformationUnit.BYTE_SCALE),
  KILOBIT("kb", InformationUnit.KILO, InformationUnit.BIT_SCALE),
  KIBIBIT("Kib", InformationUnit.KIBI, InformationUnit.BIT_SCALE),
  KILOBYTE("kB", InformationUnit.KILO, InformationUnit.BYTE_SCALE),
  KIBIBYTE("KiB", InformationUnit.KIBI, InformationUnit.BYTE_SCALE),
  MEGABIT("Mb", InformationUnit.MEGA, InformationUnit.BIT_SCALE),
  MEBIBIT("Mib", InformationUnit.MEBI, InformationUnit.BIT_SCALE),
  MEGABYTE("MB", InformationUnit.MEGA, InformationUnit.BYTE_SCALE),
  MEBIBYTE("MiB", InformationUnit.MEBI, InformationUnit.BYTE_SCALE),
  GIGABIT("Gb", InformationUnit.GIGA, InformationUnit.BIT_SCALE),
  GIBIBIT("Gib", InformationUnit.GIBI, InformationUnit.BIT_SCALE),
  GIGABYTE("GB", InformationUnit.GIGA, InformationUnit.BYTE_SCALE),
  GIBIBYTE("GiB", InformationUnit.GIBI, InformationUnit.BYTE_SCALE),
  PETABIT("Pb", InformationUnit.PETA, InformationUnit.BIT_SCALE),
  PEBIBIT("Pib", InformationUnit.PEBI, InformationUnit.BIT_SCALE),
  PETABYTE("PB", InformationUnit.PETA, InformationUnit.BYTE_SCALE),
  PEBIBYTE("PiB", InformationUnit.PEBI, InformationUnit.BYTE_SCALE),
  EXABIT("Eb", InformationUnit.EXA, InformationUnit.BIT_SCALE),
  EXBIBIT("Eib", InformationUnit.EXBI, InformationUnit.BIT_SCALE),
  EXABYTE("EB", InformationUnit.EXA, InformationUnit.BYTE_SCALE),
  EXBIBYTE("EiB", InformationUnit.EXBI, InformationUnit.BYTE_SCALE),

  ;

  private static final long BIT_SCALE = 1L;
  private static final long BYTE_SCALE = 8L * BIT_SCALE;
  private static final long KILO = 1000L;
  private static final long KIBI = 1024L;
  private static final long MEGA = 1000L * KILO;
  private static final long MEBI = 1024L * KIBI;
  private static final long GIGA = 1000L * MEGA;
  private static final long GIBI = 1024L * MEBI;
  private static final long TERA = 1000L * GIGA;
  private static final long TEBI = 1024L * GIBI;
  private static final long PETA = 1000L * TERA;
  private static final long PEBI = 1024L * TEBI;
  private static final long EXA = 1000L * PETA;
  private static final long EXBI = 1024L * PEBI;
  private final long SCALE;
  private final String SHORT_STRING;

  InformationUnit(String shortString, long... scales) {
    SHORT_STRING = shortString;
    long theScale = 1;
    for (long scale : scales) {
      theScale *= scale;
    }
    SCALE = theScale;
  }

  @Override
  public long convert(long sourceItem, Unit sourceUnit) {
    if (sourceUnit instanceof InformationUnit) {
      return Unit.generalConversionUtility(sourceItem, SCALE, ((InformationUnit) sourceUnit).SCALE);
    }

    return sourceItem;
  }

  @Override
  public Unit[] allValues() {
    InformationUnit[] informationUnits = InformationUnit.values();
    Unit[] values = new Unit[informationUnits.length];
    for (int i = 0; i < values.length; i++) {
      values[i] = informationUnits[i];
    }
    return values;
  }

  @Override
  public String toShortString() {
    return SHORT_STRING;
  }

}
