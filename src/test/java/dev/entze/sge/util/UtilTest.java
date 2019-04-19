package dev.entze.sge.util;

import static org.junit.Assert.assertEquals;

import dev.entze.sge.util.unit.InformationUnit;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

public class UtilTest {

  @Test
  public void test_convertUnitToReadableString_1() {
    assertEquals("0.5s",
        Util.convertUnitToReadableString(500L, TimeUnit.MILLISECONDS, TimeUnit.SECONDS));
  }

  @Test
  public void test_convertUnitToReadableString_2() {
    assertEquals("1d",
        Util.convertUnitToReadableString(24L, TimeUnit.HOURS, TimeUnit.DAYS));
  }

  @Test
  public void test_convertUnitToReadableString_3() {
    assertEquals("24h",
        Util.convertUnitToReadableString(1L, TimeUnit.DAYS, TimeUnit.HOURS));
  }

  @Test
  public void test_convertUnitToReadableString_4() {
    assertEquals("1.5m",
        Util.convertUnitToReadableString(90L, TimeUnit.SECONDS, TimeUnit.MINUTES));
  }

  @Test
  public void test_convertUnitToReadableString_5() {
    assertEquals("1KiB",
        Util.convertUnitToReadableString(1024L, InformationUnit.BYTE, InformationUnit.KIBIBYTE));
  }

  @Test
  public void test_convertUnitToReadableString_6() {
    assertEquals("1.5GiB",
        Util.convertUnitToReadableString(1024L + 512L, InformationUnit.MEBIBYTE,
            InformationUnit.GIBIBYTE));
  }

  @Test
  public void test_convertUnitToReadableString_7() {
    assertEquals("0.5KiB",
        Util.convertUnitToReadableString(512 * 8, InformationUnit.BIT, InformationUnit.KIBIBYTE));
  }


  @Test
  public void test_convertUnitToReadableString_8() {
    assertEquals("0EiB",
        Util.convertUnitToReadableString(0, InformationUnit.MEBIBYTE,
            InformationUnit.EXBIBYTE));
  }

  @Test
  public void test_convertUnitToReadableString_9() {
    assertEquals("1.02kb",
        Util.convertUnitToReadableString(1024, InformationUnit.BIT, InformationUnit.KILOBIT));
  }

  @Test
  public void test_convertUnitToReadableString_10() {
    assertEquals("8000b",
        Util.convertUnitToReadableString(1, InformationUnit.KILOBYTE, InformationUnit.BIT));
  }

  @Test
  public void test_convertUnitToReadableString_11() {
    assertEquals("2.05kb",
        Util.convertUnitToReadableString(2048, InformationUnit.BIT, InformationUnit.KILOBIT));
  }


  @Test
  public void test_convertUnitToApproximation() {
  }

  @Test
  public void test_convertUnitToMinimalString_0() {
    assertEquals("4 days, 3 hours, 17 minutes and 16 seconds",
        Util.convertUnitToMinimalString(357436L, TimeUnit.SECONDS));
  }

  @Test
  public void test_convertUnitToMinimalString_1() {
    assertEquals("0b",
        Util.convertUnitToMinimalString(0L, InformationUnit.MEBIBIT));
  }

  @Test
  public void test_convertToMinimalUnits() {
  }

}