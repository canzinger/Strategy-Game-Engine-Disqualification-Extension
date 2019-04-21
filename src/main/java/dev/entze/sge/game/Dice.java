package dev.entze.sge.game;

import dev.entze.sge.util.Util;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Dice {

  private final int numberOfDice;
  private final int numberOfFaces;
  private final Random random;
  private final int[] topFaces;

  public Dice(int numberOfDice, int numberOfFaces) {
    this(numberOfDice, numberOfFaces, new Random());
  }

  public Dice(int numberOfDice, int numberOfFaces, Random random) {
    this.numberOfDice = numberOfDice;
    this.numberOfFaces = numberOfFaces;
    this.random = random;
    topFaces = new int[numberOfDice];
  }

  public void roll() {
    for (int i = 0; i < topFaces.length; i++) {
      topFaces[i] = random.nextInt(numberOfFaces) + 1;
    }
  }

  public int getFaceOf(int die) {
    return topFaces[die];
  }

  public int[] getFacesOf() {
    return topFaces;
  }

  public int[] getSortedFacesOf() {
    Arrays.sort(topFaces);
    return topFaces;
  }

  public int[] getSortedFacesOfReversed() {
    Arrays.sort(topFaces);
    Util.reverse(topFaces);
    return topFaces;
  }

  public int[] getSortedFacesOf(Comparator<Integer> comparator) {
    Util.sort(topFaces, comparator);
    return topFaces;
  }

  @Override
  public String toString() {
    return numberOfDice + "d" + numberOfFaces;
  }
}
