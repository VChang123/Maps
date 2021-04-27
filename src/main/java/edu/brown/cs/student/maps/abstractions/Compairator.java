package edu.brown.cs.student.maps.abstractions;

import java.util.Comparator;

/**
 * Compairator class used to compare the pairs in the priority queue.
 * @param <X> the type to the object
 */
public class Compairator<X> implements Comparator<Pair<X, Double>> {

  /**
   * Compairator constructor.
   */
  public Compairator() { }

  /**
   * Compares the two pairs.
   * @param o1 the first pair
   * @param o2 the second pair
   * @return an int result
   */
  @Override
  public int compare(Pair<X, Double> o1, Pair<X, Double> o2) {
    return Double.compare(o1.getScnd(), o2.getScnd());
  }
}
