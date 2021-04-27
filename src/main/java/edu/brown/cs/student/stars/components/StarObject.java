package edu.brown.cs.student.stars.components;

import edu.brown.cs.student.stars.abstractions.ICoordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * StarObject class.
 */
public class StarObject implements ICoordinate {
  /**
   * StarObject fields.
   */
  private int id;
  private String name;
  private List<Double> coords;
  private int dim;

  /**
   * StarObject constructor.
   * @param id - id of star
   * @param name - name of star
   * @param coords - location of star
   * @param dim - number of dimensions in star coordinates
   */
  public StarObject(int id, String name, List<Double> coords, int dim) {
    this.id = id;
    this.name = name;
    this.coords = coords;
    this.dim = dim;
  }

  /**
   * getter for id.
   * @return - id.
   */
  public int getId() {
    return this.id;
  }

  /**
   * getter for name.
   * @return - name.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Gets a list of coordinates.
   * @return a list of coordinates.
   */
  @Override
  public List<Double> getCoords() {
    return this.coords;
  }

  /**
   * Gets the dimension of the object.
   * @return the int dimension
   */
  @Override
  public int getNumDim() {
    return this.dim;
  }

  /**
   * Turns the coordinates to a string.
   * @return a string of coordinates.
   */
  public String coordToString() {
    List<String> stringCoords = new ArrayList<>();
    for (Double d : this.coords) {
      stringCoords.add(String.valueOf(d));
    }
    return String.join(", ", stringCoords);
  }
}
