package edu.brown.cs.student.stars.abstractions;

import java.util.List;

/**
 * DistanceCalculator class.
 */
public abstract class DistanceCalculator {

  /**
   * DistanceCalculator constructor.
   */
  public DistanceCalculator() { }

  /**
   * Calculates the distance between two sets of coordinates represented as a list of doubles.
   * @param point1 - first coordinate
   * @param point2 - second coordinate
   * @return - a double representing the Euclidean distance between the two coordinates
   */
  public double distance(List<Double> point1, List<Double> point2) {
    double toRoot = 0.;
    for (int i = 0; i < point1.size(); i++) {
      double diff = point1.get(i) - point2.get(i);
      toRoot += Math.pow(diff, 2);
    }
    return Math.sqrt(toRoot);
  }

}
