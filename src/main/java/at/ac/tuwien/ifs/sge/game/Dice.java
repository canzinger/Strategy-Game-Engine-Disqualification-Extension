package at.ac.tuwien.ifs.sge.game;

import at.ac.tuwien.ifs.sge.util.Util;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Dice {

  private final int numberOfDice;
  private final int numberOfFaces;
  private final Random random;
  private final int[] topFaces;


  public Dice(int numberOfDice) {
    this(numberOfDice, 6);
  }

  public Dice(int numberOfDice, Random random) {
    this(numberOfDice, 6, random);
  }

  public Dice(int numberOfDice, int numberOfFaces) {
    this(numberOfDice, numberOfFaces, new Random());
  }

  public Dice(int numberOfDice, int numberOfFaces, Random random) {
    this.numberOfDice = numberOfDice;
    this.numberOfFaces = numberOfFaces;
    this.random = random;
    topFaces = new int[numberOfDice];
  }

  public void roll(int... dice) {
    if (dice.length == 0) {
      for (int i = 0; i < topFaces.length; i++) {
        topFaces[i] = random.nextInt(numberOfFaces) + 1;
      }
    }
    for (int die : dice) {
      topFaces[die] = random.nextInt(numberOfFaces) + 1;
    }
  }

  public void rollN(int dice) {
    reset();
    for (int die = 0; die < dice; die++) {
      topFaces[die] = random.nextInt(numberOfFaces) + 1;
    }
  }

  public void reset(int... dice) {
    if (dice.length == 0) {
      Arrays.fill(topFaces, -1);
    }
    for (int die : dice) {
      topFaces[die] = -1;
    }
  }

  public void sort() {
    Arrays.sort(topFaces);
  }

  public void sortReverse() {
    sort();
    Util.reverse(topFaces);
  }

  public void sort(Comparator<Integer> comparator) {
    Util.sort(topFaces, comparator);
  }

  public int getFaceOf(int die) {
    return topFaces[die];
  }

  public int[] getFacesOf() {
    return topFaces;
  }

  public int[] getSortedFacesOf() {
    sort();
    return topFaces;
  }

  public int[] getSortedFacesOfReversed() {
    sortReverse();
    return topFaces;
  }

  public int[] getSortedFacesOf(Comparator<Integer> comparator) {
    sort(comparator);
    return topFaces;
  }

  @Override
  public String toString() {
    return numberOfDice + "d" + numberOfFaces;
  }
}
