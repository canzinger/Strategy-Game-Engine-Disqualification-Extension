package dev.entze.sge.util;

import dev.entze.sge.game.ActionRecord;
import dev.entze.sge.game.Game;
import dev.entze.sge.util.node.GameNode;
import dev.entze.sge.util.tree.Tree;
import dev.entze.sge.util.unit.TimeUnitWrapper;
import dev.entze.sge.util.unit.Unit;
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

  public static <E> Collection<Collection<E>> combinations(Collection<E> collection, int r) {
    final int n = collection.size();
    if (n == 0 || r <= 0) {
      return Collections.emptyList();
    }
    if (n <= r) {
      return List.of(List.copyOf(collection));
    }
    List<E> list;
    if (collection instanceof List) {
      list = (List<E>) collection;
    } else {
      list = new ArrayList<>(collection);
    }
    int[] indices = new int[r];
    for (int i = 0; i < r; i++) {
      indices[i] = i;
    }

    Collection<Collection<E>> combinations = new ArrayList<>();
    Collection<E> combination = new ArrayList<>(r);
    do {
      combination.clear();
      for (int index : indices) {
        combination.add(list.get(index));
      }
      combinations.add(List.copyOf(combination));
      indices = combinations(indices);
    } while (indices[r - 1] < n);

    return combinations;
  }

  public static int[] combinations(int[] last) {
    int[] next = last.clone();
    for (int i = 0; i + 1 < next.length; i++) {
      if ((next[i] + 1) < next[i + 1]) {
        next[i]++;
        return next;
      }
    }
    next[next.length - 1]++;
    return next;
  }

  public static <E> Collection<List<E>> permutations(Collection<E> collection, int r) {
    final int n = collection.size();
    if (n == 0 || r <= 0) {
      return Collections.emptyList();
    }
    if (n <= r) {
      return List.of(List.copyOf(collection));
    }
    List<E> list;
    if (collection instanceof List) {
      list = (List<E>) collection;
    } else {
      list = new ArrayList<>(collection);
    }

    int[] indices = new int[r];
    for (int i = 0; i < r; i++) {
      indices[i] = i;
    }

    Collection<List<E>> permutations = new ArrayList<>();
    List<E> permutation = new ArrayList<>(r);

    do {
      permutation.clear();
      for (int index : indices) {
        permutation.add(list.get(index));
      }
      permutations.add(List.copyOf(permutation));
      do {
        indices = permutations(indices, r);
      } while (hasDuplicates(indices) && !allEqualTo(indices, 0));
    } while (!allEqualTo(indices, 0));

    return permutations;
  }

  public static int[] permutations(int[] last, int r) {
    final int n = last.length;
    int[] next = last.clone();

    for (int i = 0; i < n; i++) {
      next[i]++;
      if (next[i] >= r) {
        next[i] = 0;
      } else {
        break;
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

    StringBuilder stringBuilder = new StringBuilder();
    for (int i = minimalTimes.length - 1; i >= 0 && nonZeros >= 0; i--) {
      if (minimalTimes[i] > 0) {
        if (i < minimalTimes.length - 1) {
          if (nonZeros > 1) {
            stringBuilder.append(", ");
          } else {
            stringBuilder.append(" and ");
          }
        }
        stringBuilder.append(minimalTimes[i]).append(' ')
            .append(targetUnits[i].toString().toLowerCase());
        nonZeros--;
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

  public static double[] scoreOutOfUtility(double[] utility) {
    double[] score = new double[utility.length];
    double max = Double.NEGATIVE_INFINITY;
    int lastChange = -1;
    for (int i = 0; i < utility.length; i++) {
      if (max < utility[i]) {
        max = utility[i];
        lastChange = i;
      }
      score[i] =
          (utility[i] >= max ? (1D / utility.length) : 0D)
              + (utility[i] > max ? (((double) (utility.length - 1)) / utility.length) : 0D);
    }

    if (lastChange > 0) {
      for (int i = 0; i < score.length; i++) {
        score[i] =
            (utility[i] >= max ? (1D / utility.length) : 0D)
                + (utility[i] > max ? (((double) (utility.length - 1)) / utility.length) : 0D);
      }
    }

    return score;
  }

}
