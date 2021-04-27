package edu.brown.cs.student.stars.abstractions;

import java.util.Comparator;
import java.util.List;

/**
 * CoordDistanceComparator class.
 */
public class CoordDistanceComparator extends DistanceCalculator implements Comparator<ICoordinate> {

  /**
   * CoordDistanceComparator fields.
   */
  private List<Double> targetCoords;

  /**
   * CoordDistanceComparator constructor.
   * @param coords - coordinates of ICoordinate
   */
  public CoordDistanceComparator(List<Double> coords) {
    this.targetCoords = coords;
  }

  /**
   * Compares the distance of two ICoordinate objects with respect to a target location.
   * @param a - first ICoordinate
   * @param b - second ICoordinate
   * @return - if the distance between a and the target is the same as the distance between b and
   * the target, return 0. otherwise, return -1 if Star a is closer and 1 if Star b is closer.
   */
  public int compare(ICoordinate a, ICoordinate b) {
    double aDist = distance(this.targetCoords, a.getCoords());
    double bDist = distance(this.targetCoords, b.getCoords());
    return Double.compare(aDist, bDist);
  }
}
