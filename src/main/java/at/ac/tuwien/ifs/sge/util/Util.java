package at.ac.tuwien.ifs.sge.util;

import at.ac.tuwien.ifs.sge.game.ActionRecord;
import at.ac.tuwien.ifs.sge.util.node.GameNode;
import at.ac.tuwien.ifs.sge.util.tree.Tree;
import at.ac.tuwien.ifs.sge.util.unit.TimeUnitWrapper;
import com.google.common.base.Strings;
import at.ac.tuwien.ifs.sge.game.Game;
import at.ac.tuwien.ifs.sge.util.unit.Unit;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Util {

  public static int nPr(int n, int k) {
    int res = 1;

    for (int i = (n - k) + 1; i <= n; i++) {
      res *= i;
    }

    return res;
  }


  /**
   * Taken from <a href=https://floating-point-gui.de/errors/comparison/>https://floating-point-gui.de/errors/comparison/</a>
   *
   * @param a - a float
   * @param b - a float
   * @param epsilon - relative error
   * @return if the two numbers are within the relative epsilon of one another
   */
  public static boolean nearlyEquals(float a, float b, float epsilon) {
    final float absA = Math.abs(a);
    final float absB = Math.abs(b);
    final float diff = Math.abs(a - b);

    if (a == b) { // shortcut, handles infinities
      return true;
    } else if (a == 0 || b == 0 || (absA + absB < Float.MIN_NORMAL)) {
      // a or b is zero or both are extremely close to it
      // relative error is less meaningful here
      return diff < (epsilon * Float.MIN_NORMAL);
    } else { // use relative error
      return diff / Math.min((absA + absB), Float.MAX_VALUE) < epsilon;
    }
  }

  public static boolean nearlyEquals(double a, double b, double epsilon) {
    final double absA = Math.abs(a);
    final double absB = Math.abs(b);
    final double diff = Math.abs(a - b);

    if (a == b) { // shortcut, handles infinities
      return true;
    } else if (a == 0 || b == 0 || (absA + absB < Double.MIN_NORMAL)) {
      // a or b is zero or both are extremely close to it
      // relative error is less meaningful here
      return diff < (epsilon * Double.MIN_NORMAL);
    } else { // use relative error
      return diff / Math.min((absA + absB), Double.MAX_VALUE) < epsilon;
    }
  }

  public static boolean nearlyEquals(double a, double b, int significantFigures) {
    if (a == b) {
      return true;
    }

    for (int i = 0; i <= significantFigures; i++) {
      if (((long) a) != ((long) b)) {
        return false;
      }
      a *= 10;
      b *= 10;
    }

    return true;
  }

  public static long doubleAsLong(double a, int significantFigures) {
    return Math.round(Math.floor(a * Math.pow(10, significantFigures)));
  }

  public static String convertDoubleToString(double a, int digits) {
    return String.format(String.format("%s%d%c", "%.", digits, 'f'), a);
  }

  public static String convertDoubleToString(double a) {
    return convertDoubleToString(a, significantDigits(a));
  }

  public static int significantDigits(double a) {
    int d = -1;
    double r;
    boolean allEqual = true;
    do {
      d++;
      r = roundTo(a, d);
      allEqual = true;
      for (int s = 18; allEqual && s > d; s--) {
        allEqual = nearlyEquals(a, r, s);
      }
    } while (d < 19 && !allEqual);

    return d;
  }

  public static String convertDoubleToMinimalString(double a, int maxDigits) {
    return convertDoubleToString(a, Math.min(maxDigits, significantDigits(a)));
  }

  public static double roundTo(double a, int digits) {
    return BigDecimal.valueOf(a).setScale(digits, RoundingMode.HALF_EVEN).doubleValue();
  }

  public static StringBuilder appendWithBlankBuffer(StringBuilder stringBuilder, double d,
      int width) {
    String string = Util.convertDoubleToMinimalString(d, width - 2);
    width -= string.length();
    Util.appendNTimes(stringBuilder, ' ', width / 2 + (width % 2))
        .append(string);
    Util.appendNTimes(stringBuilder, ' ', width / 2);
    return stringBuilder;
  }

  public static StringBuilder appendWithBlankBuffer(StringBuilder stringBuilder, String string,
      int width) {
    width -= string.length();
    Util.appendNTimes(stringBuilder, ' ', width / 2 + (width % 2))
        .append(string);
    Util.appendNTimes(stringBuilder, ' ', width / 2);
    return stringBuilder;
  }

  public static <E> E selectRandom(Collection<? extends E> collection) {
    return selectRandom(collection, ThreadLocalRandom.current());
  }

  @SuppressWarnings("unchecked")
  public static <E> E selectRandom(Collection<? extends E> coll, Random random) {
    if (coll.size() == 0) {
      return null;
    }

    int index = random.nextInt(coll.size());
    if (coll instanceof List) { // optimization
      return ((List<? extends E>) coll).get(index);
    } else {
      Iterator<? extends E> iter = coll.iterator();
      for (int i = 0; i < index; i++) {
        iter.next();
      }
      return iter.next();
    }
  }

  public static <E> boolean findRoot(Tree<? extends GameNode<E>> tree,
      Game<E, ?> game) {

    boolean foundRoot = false;
    if (!tree.isEmpty() && tree.getNode().getGame() != null) {

      List<ActionRecord<E>> actionRecords = game.getActionRecords();
      List<ActionRecord<E>> oldActionRecords = tree.getNode().getGame().getActionRecords();

      foundRoot = true;
      for (int i = oldActionRecords.size(); i < actionRecords.size() && foundRoot; i++) {
        boolean foundNextBranch = false;
        List<? extends Tree<? extends GameNode<E>>> children = tree.getChildren();
        for (int c = 0; c < children.size(); c++) {
          if (children.get(c).getNode().getGame().getPreviousActionRecord()
              .equals(actionRecords.get(i))) {
            tree.reRoot(c);
            foundNextBranch = true;
            break;
          }
        }
        foundRoot = foundNextBranch;
      }

    }

    if (!foundRoot) {
      tree.dropChildren();
      tree.getNode().setGame(game);
    }

    tree.dropParent();
    return foundRoot;
  }

  public static void swap(int[] array, int indexA, int indexB) {
    int a = array[indexA];
    array[indexA] = array[indexB];
    array[indexB] = a;
  }

  private static void sort2(int[] array, Comparator<Integer> comparator) {
    if (comparator.compare(array[0], array[1]) > 0) {
      swap(array, 0, 1);
    }
  }

  private static void sort3(int[] array, Comparator<Integer> comparator) {
    if (comparator.compare(array[0], array[1]) > 0) {
      if (comparator.compare(array[1], array[2]) < 0) {
        swap(array, 0, 1);
      }
      if (comparator.compare(array[0], array[2]) > 0) {
        swap(array, 2, 0);
      }
    } else if (comparator.compare(array[1], array[2]) > 0) {
      swap(array, 1, 2);
      if (comparator.compare(array[0], array[1]) > 0) {
        swap(array, 0, 1);
      }
    }


  }

  public static void reverse(int[] array) {
    for (int i = 0; i < array.length / 2; i++) {
      swap(array, i, array.length - (i + 1));
    }
  }

  public static void sort(int[] array, Comparator<Integer> comparator) {
    if (array.length > 1) {
      if (array.length == 2) {
        sort2(array, comparator);
      } else if (array.length == 3) {
        sort3(array, comparator);
      } else {
        int[] newArray = Arrays.stream(array).boxed().sorted(comparator).mapToInt(i -> i).toArray();
        System.arraycopy(newArray, 0, array, 0, array.length);
      }
    }
  }

  public static boolean hasDuplicates(int[] array) {
    for (int i = 0; i < array.length; i++) {
      for (int j = (i + 1); j < array.length; j++) {
        if (array[i] == array[j]) {
          return true;
        }
      }
    }
    return false;
  }

  public static boolean allEqual(int[] array) {
    for (int i = 0; i + 1 < array.length; i++) {
      if (array[i] != array[i + 1]) {
        return false;
      }
    }
    return true;
  }

  public static boolean allEqualTo(int[] array, int e) {
    for (int i = 0; i < array.length; i++) {
      if (array[i] != e) {
        return false;
      }
    }
    return true;
  }

  public static int numberOfEqualTo(int[] array, int e) {
    int c = 0;
    for (int i = 0; i < array.length; i++) {
      if (array[i] == e) {
        c++;
      }
    }
    return c;
  }

  public static boolean contains(int[] array, int e) {
    for (int i = 0; i < array.length; i++) {
      if (array[i] == e) {
        return true;
      }
    }
    return false;
  }

  public static boolean isAscending(int[] array) {
    for (int i = 0; i < array.length - 1; i++) {
      if (array[i] > array[i + 1]) {
        return false;
      }
    }
    return true;
  }

  public static boolean isStrictlyAscending(int[] array) {
    for (int i = 0; i < array.length - 1; i++) {
      if (array[i] >= array[i + 1]) {
        return false;
      }
    }
    return true;
  }

  public static boolean isDescending(int[] array) {
    for (int i = 0; i < array.length - 1; i++) {
      if (array[i] < array[i + 1]) {
        return false;
      }
    }
    return true;
  }

  public static boolean isStrictlyDescending(int[] array) {
    for (int i = 0; i < array.length - 1; i++) {
      if (array[i] <= array[i + 1]) {
        return false;
      }
    }
    return true;
  }

  public static boolean isIndexEqualToValue(int[] array) {
    for (int i = 0; i < array.length; i++) {
      if (array[i] != i) {
        return false;
      }
    }
    return true;
  }

  public static boolean isReverseIndexEqualToValue(int[] array) {
    for (int i = 0; i < array.length; i++) {
      if (array[i] != (array.length - 1) - i) {
        return false;
      }
    }
    return true;
  }

  public static <E> void shuffle(Deque<E> deque) {
    shuffle(deque, new Random());
  }

  public static <E> void shuffle(Deque<E> deque, Random random) {
    if (deque instanceof LinkedList) {
      Collections.shuffle((LinkedList<E>) deque, random);
    } else {
      List<E> list = new ArrayList<>(deque);
      deque.clear();
      Collections.shuffle(list, random);
      deque.addAll(list);
    }
  }

  public static int[] combinations(int[] last) {
    int[] next = last.clone();
    for (int i = 0; i + 1 < next.length; i++) {
      if ((next[i] + 1) < next[i + 1]) {
        next[i]++;
        for (int j = 0; j < i; j++) {
          next[j] = j;
        }
        return next;
      }
    }
    next[next.length - 1]++;
    for (int i = 0; i < next.length - 1; i++) {
      next[i] = i;
    }
    return next;
  }

  public static int[] permutations(int[] last, int r) {
    int[] next = last;
    do {
      next = multipermutations(next, r);
    } while (hasDuplicates(next));

    return next;
  }

  public static int[] multipermutations(int[] last, int r) {
    int[] next = last.clone();
    for (int i = 0; i < next.length; i++) {
      next[i]++;
      if (next[i] > r) {
        next[i] = 0;
      } else {
        return next;
      }
    }
    return next;
  }

  public static <E> List<E> asList(Collection<E> collection) {
    if (collection.isEmpty()) {
      return Collections.emptyList();
    }
    if (collection instanceof List) {
      return (List<E>) collection;
    }
    return List.copyOf(collection);
  }

  public static String convertUnitToReadableString(long item, TimeUnit unit) {
    return convertUnitToReadableString(item, unit, unit);
  }

  public static String convertUnitToReadableString(long item, Unit unit) {
    return convertUnitToReadableString(item, unit, unit);
  }

  public static String convertUnitToReadableString(long item, TimeUnit unit, TimeUnit target) {
    return convertUnitToReadableString(item, new TimeUnitWrapper(unit),
        new TimeUnitWrapper(target));
  }

  public static String convertUnitToReadableString(long item, Unit unit, Unit targetUnit) {
    String readable = String.format("%.2f", convertUnitToApproximation(item, unit, targetUnit));

    return readable.replaceAll("\\.?0*$", "") + targetUnit.toShortString();
  }

  public static double convertUnitToApproximation(long item, TimeUnit unit, TimeUnit targetUnit) {
    return convertUnitToApproximation(item, new TimeUnitWrapper(unit),
        new TimeUnitWrapper(targetUnit));
  }

  public static double convertUnitToApproximation(long item, Unit unit, Unit targetUnit) {

    if (item == 0) {
      return 0D;
    }

    Unit[] allUnits = targetUnit.allValues();
    int targetIndex;
    for (targetIndex = 0; targetIndex < allUnits.length; targetIndex++) {
      if (allUnits[targetIndex].equals(targetUnit)) {
        break;
      }
    }

    Unit[] lessThanTarget = new Unit[targetIndex + 1];
    System.arraycopy(allUnits, 0, lessThanTarget, 0, lessThanTarget.length);

    long[] minimalUnits = convertToMinimalUnits(item, unit, lessThanTarget);

    double fraction = 0;

    for (int i = 0; i < minimalUnits.length - 1; i++) {
      fraction += lessThanTarget[0].convert(minimalUnits[i], lessThanTarget[i]);
    }

    return (double) minimalUnits[targetIndex] + (fraction / ((double) lessThanTarget[0]
        .convert(1L, targetUnit)));
  }

  public static String convertUnitToMinimalString(long item, TimeUnit timeUnit) {
    return convertUnitToMinimalString(item, new TimeUnitWrapper(timeUnit));
  }

  public static String convertUnitToMinimalString(long item, Unit unit) {
    return convertUnitToMinimalString(item, unit, unit.allValues());
  }

  public static String convertUnitToMinimalString(long item, TimeUnit unit,
      TimeUnit[] targetUnits) {
    Unit[] units = new Unit[targetUnits.length];
    for (int i = 0; i < units.length; i++) {
      units[i] = new TimeUnitWrapper(targetUnits[i]);
    }
    return convertUnitToMinimalString(item, new TimeUnitWrapper(unit), units);
  }

  public static String convertUnitToMinimalString(long item, Unit unit, Unit[] targetUnits) {
    long[] minimalTimes = convertToMinimalUnits(item, unit, targetUnits);
    int nonZeros = 0;
    for (long minimalTime : minimalTimes) {
      if (minimalTime > 0) {
        nonZeros++;
      }
    }
    if (nonZeros <= 0) {
      return "0" + targetUnits[0].toShortString();
    }

    boolean first = true;
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = minimalTimes.length - 1; i >= 0 && nonZeros >= 0; i--) {
      if (minimalTimes[i] > 0) {
        if (i < minimalTimes.length - 1 && !first) {
          if (nonZeros > 1) {
            stringBuilder.append(", ");
          } else {
            stringBuilder.append(" and ");
          }
        }
        stringBuilder.append(minimalTimes[i]).append(' ')
            .append(targetUnits[i].toString().toLowerCase());
        nonZeros--;
        first = false;
      }
    }

    return stringBuilder.toString();
  }

  public static long[] convertToMinimalUnits(long item, TimeUnit unit) {
    return convertToMinimalUnits(item, new TimeUnitWrapper(unit));
  }

  public static long[] convertToMinimalUnits(long item, Unit unit) {
    return convertToMinimalUnits(item, unit, unit.allValues());
  }

  public static long[] convertToMinimalUnits(long item, TimeUnit unit, TimeUnit[] targetUnits) {
    Unit[] units = new Unit[targetUnits.length];
    for (int i = 0; i < units.length; i++) {
      units[i] = new TimeUnitWrapper(targetUnits[i]);
    }
    return convertToMinimalUnits(item, new TimeUnitWrapper(unit), units);
  }

  /**
   * Return an array of minimal units. For example 25 hours are converted to 1 day and 1 hour.
   *
   * @param item - the item
   * @param unit - the unit
   * @param targetUnits - the targeted Units
   * @return an array of minimal timeUnits
   */
  public static long[] convertToMinimalUnits(long item, Unit unit, Unit[] targetUnits) {
    long[] items = new long[targetUnits.length];

    if (item == 0) {
      return items;
    }

    for (int i = 0; i < items.length; i++) {
      items[i] = targetUnits[i].convert(item, unit);
    }

    for (int i = items.length - 2; i >= 0; i--) {
      for (int j = items.length - 1; j > i; j--) {
        items[i] -= targetUnits[i].convert(items[j], targetUnits[j]);
      }
    }

    return items;
  }

  public static double percentage(double f, double t) {
    return 100D * f / t;
  }

  public static double percentage(int f, int t) {
    if (t == 0 && f == t) {
      return 0D;
    }
    return 100D * ((double) f) / ((double) t);
  }

  public static double percentage(long f, long t) {
    if (t == 0 && f == t) {
      return 0D;
    }
    return 100D * ((double) f) / ((double) t);
  }

  public static StringBuilder appendNTimes(StringBuilder stringBuilder, boolean b, int n) {
    for (int a = 0; a < n; a++) {
      stringBuilder.append(b);
    }
    return stringBuilder;
  }

  public static StringBuilder appendNTimes(StringBuilder stringBuilder, char c, int n) {
    for (int a = 0; a < n; a++) {
      stringBuilder.append(c);
    }
    return stringBuilder;
  }

  public static StringBuilder appendNTimes(StringBuilder stringBuilder, char[] c, int n) {
    for (int a = 0; a < n; a++) {
      stringBuilder.append(c);
    }
    return stringBuilder;
  }

  public static StringBuilder appendNTimes(StringBuilder stringBuilder, CharSequence c, int n) {
    for (int a = 0; a < n; a++) {
      stringBuilder.append(c);
    }
    return stringBuilder;
  }

  public static StringBuilder appendNTimes(StringBuilder stringBuilder, double d, int n) {
    for (int a = 0; a < n; a++) {
      stringBuilder.append(d);
    }
    return stringBuilder;
  }

  public static StringBuilder appendNTimes(StringBuilder stringBuilder, float f, int n) {
    for (int a = 0; a < n; a++) {
      stringBuilder.append(f);
    }
    return stringBuilder;
  }

  public static StringBuilder appendNTimes(StringBuilder stringBuilder, int i, int n) {
    for (int a = 0; a < n; a++) {
      stringBuilder.append(i);
    }
    return stringBuilder;
  }

  public static StringBuilder appendNTimes(StringBuilder stringBuilder, long l, int n) {
    for (int a = 0; a < n; a++) {
      stringBuilder.append(l);
    }
    return stringBuilder;
  }

  public static StringBuilder appendNTimes(StringBuilder stringBuilder, Object o, int n) {
    for (int a = 0; a < n; a++) {
      stringBuilder.append(o);
    }
    return stringBuilder;
  }

  public static StringBuilder appendNTimes(StringBuilder stringBuilder, String s, int n) {
    for (int a = 0; a < n; a++) {
      stringBuilder.append(s);
    }
    return stringBuilder;
  }

  public static StringBuilder appendNTimes(StringBuilder stringBuilder, StringBuffer s, int n) {
    for (int a = 0; a < n; a++) {
      stringBuilder.append(s);
    }
    return stringBuilder;
  }

  public static double scoreOutOfUtility(double[] utility, int i) {
    double value = utility[i];
    double max = Double.NEGATIVE_INFINITY;
    for (int v = 0; v < utility.length; v++) {
      if (v != i) {
        max = Math.max(max, utility[v]);
      }
    }

    return (value >= max ? (1D / utility.length) : 0D)
        + (value > max ? (((double) (utility.length - 1)) / utility.length) : 0D);
  }

  public static double[] scoresOutOfUtilities(double[] utility) {
    return scoresOutOfUtilities(utility, 8);
  }

  public static double[] scoresOutOfUtilities(double[] utility, final int significantFigures) {
    double[] score = new double[utility.length];
    double max = Double.NEGATIVE_INFINITY;
    int numberOfMaxs = 0;
    for (double u : utility) {
      if (max <= u || nearlyEquals(max, u, significantFigures)) {
        if (max < u && !nearlyEquals(max, u, significantFigures)) {
          numberOfMaxs = 0;
          max = u;
        }
        numberOfMaxs++;
      }
    }

    for (int i = 0; i < score.length; i++) {
      score[i] = (utility[i] >= max || nearlyEquals(max, utility[i], significantFigures) ? (1D
          / ((double) numberOfMaxs)) : 0);
    }

    return score;
  }

  public static List<String> separateByDifferences(String original, String other) {
    List<String> separations = new ArrayList<>();

    String common;
    do {

      //separate a common prefix
      common = Strings.commonPrefix(original, other);
      separations.add(common);
      int begin = common.length();
      original = original.substring(begin);
      other = other.substring(begin);

      //find after which char other equals original again
      int skipOther = other.length();
      int skipOriginal = original.length();

      for (int oth = 0; oth < other.length() && skipOther == other.length(); oth++) {
        for (int ori = 0; ori < original.length(); ori++) {
          if (original.charAt(ori) == other.charAt(oth)) {
            skipOther = oth;
            skipOriginal = ori;
            break;
          }
        }
      }

      if (0 < skipOther) {
        separations.add(other.substring(0, skipOther));
      }
      other = other.substring(skipOther);
      original = original.substring(skipOriginal);

    } while (!other.isEmpty());

    return separations;
  }

}
