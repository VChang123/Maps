package edu.brown.cs.student.stars.abstractions;

import java.util.List;

/**
 * ICoordinate interface.
 */
public interface ICoordinate {

  /**
   * Getter for ICoordinate coordinates.
   * @return ICoordinate coordinates, a List of Doubles
   */
  List<Double> getCoords();

  /**
   * Getter for number of dimensions represented in an ICoordinate.
   * @return - number of dimensions represented in an ICoordinate, an integer
   */
  int getNumDim();
}
