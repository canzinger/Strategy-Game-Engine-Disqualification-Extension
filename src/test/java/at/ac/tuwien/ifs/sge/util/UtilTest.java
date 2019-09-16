package at.ac.tuwien.ifs.sge.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import at.ac.tuwien.ifs.sge.util.unit.InformationUnit;
import java.util.List;
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
  public void test_convertUnitToReadableString_12() {
    assertEquals("20.12s",
        Util.convertUnitToReadableString(20120397010L, TimeUnit.NANOSECONDS, TimeUnit.SECONDS));
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


  @Test
  public void test_combinations() {
    int[] is = new int[] {0, 1, 2};
    is = Util.combinations(is);
    assertArrayEquals(new int[] {0, 1, 3}, is);
    is = Util.combinations(is);
    assertArrayEquals(new int[] {0, 2, 3}, is);
    is = Util.combinations(is);
    assertArrayEquals(new int[] {1, 2, 3}, is);
    is = Util.combinations(is);
    assertArrayEquals(new int[] {0, 1, 4}, is);
    is = Util.combinations(is);
    assertArrayEquals(new int[] {0, 2, 4}, is);
    is = Util.combinations(is);
    assertArrayEquals(new int[] {1, 2, 4}, is);
    is = Util.combinations(is);
    assertArrayEquals(new int[] {0, 3, 4}, is);
    is = Util.combinations(is);
    assertArrayEquals(new int[] {1, 3, 4}, is);
    is = Util.combinations(is);
    assertArrayEquals(new int[] {2, 3, 4}, is);
  }


  @Test
  public void test_separateByDifference_equals() {
    String a = "abc";
    String b = "abc";

    assertEquals(List.of("abc"), Util.separateByDifferences(a, b));
  }

  @Test
  public void test_separateByDifference_emptyToNew() {
    String a = "";
    String b = "abc";

    assertEquals(List.of("", "abc"), Util.separateByDifferences(a, b));
  }

  @Test
  public void test_separateByDifference_toEmpty() {
    String a = "abc";
    String b = "";

    assertEquals(List.of(""), Util.separateByDifferences(a, b));
  }

  @Test
  public void test_separateByDifference_3middle() {
    String a = "abc";
    String b = "a2c";

    assertEquals(List.of("a", "2", "c"), Util.separateByDifferences(a, b));
  }

  @Test
  public void test_separateByDifference_3left() {
    String a = "abc";
    String b = "1bc";

    assertEquals(List.of("", "1", "bc"), Util.separateByDifferences(a, b));
  }

  @Test
  public void test_separateByDifference_3right() {
    String a = "abc";
    String b = "ab3";

    assertEquals(List.of("ab", "3"), Util.separateByDifferences(a, b));
  }

  @Test
  public void test_separateByDifference_3insertNew() {
    String a = "abc";
    String b = "abbc";

    assertEquals(List.of("ab", "b", "c"), Util.separateByDifferences(a, b));
  }

  @Test
  public void test_separateByDifference_6insertNew() {
    String a = "abcdef";
    String b = "abccdff";

    assertEquals(List.of("abc", "c", "d", "f", "f"), Util.separateByDifferences(a, b));
  }

  @Test
  public void test_separateByDifference_3completelyDifferent() {
    String a = "abc";
    String b = "123";

    assertEquals(List.of("", "123"), Util.separateByDifferences(a, b));
  }

  @Test
  public void test_separateByDifference_1() {
    String a = "aaaaa";
    String b = "111aaa222aaa";

    assertEquals(List.of("", "111", "aaa", "222", "aa", "a"), Util.separateByDifferences(a, b));
  }

  @Test
  public void test_separateByDifference_2() {
    String a = "| 0:2 |";
    String b = "| 1:1 |";

    assertEquals(List.of("| ", "1", ":", "1", " |"), Util.separateByDifferences(a, b));
  }

  @Test
  public void test_separateByDifference_Risk(){
    String a = "+-----+\n"
        + "| 1:2 |\n"
        + "+-----+\n"
        + "       \\     +-----+\n"
        + "        \\____| 0:3 |\n"
        + "        /    +-----+\n"
        + "       /\n"
        + "+-----+\n"
        + "| 1:1 |\n"
        + "+-----+\n";
    String b = "+-----+\n"
        + "| 1:2 |\n"
        + "+-----+\n"
        + "       \\     +-----+\n"
        + "        \\____| 0:3 |\n"
        + "        /    +-----+\n"
        + "       /\n"
        + "+-----+\n"
        + "| 1:2 |\n"
        + "+-----+";

    assertEquals(List.of("+-----+\n"
        + "| 1:2 |\n"
        + "+-----+\n"
        + "       \\     +-----+\n"
        + "        \\____| 0:3 |\n"
        + "        /    +-----+\n"
        + "       /\n"
        + "+-----+\n"
        + "| 1:", "2",  " |\n"
        + "+-----+"), Util.separateByDifferences(a, b));

  }

}