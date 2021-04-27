package edu.brown.cs.student.maps.components;

import edu.brown.cs.student.ProgramRunner;
import edu.brown.cs.student.maps.abstractions.Haversine;
import edu.brown.cs.student.maps.abstractions.IVertex;
import edu.brown.cs.student.stars.abstractions.ICoordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Node object class.
 */
public class Node implements ICoordinate, IVertex<Node, Way> {
  private String id;
  private Double lat;
  private Double lon;
  private List<Double> coords;
  private int dimension;

  /**
   * Node Constructor.
   * @param nodeId the node id
   * @param latitude the node's latitude
   * @param longitude the node's longitude
   */
  public Node(String nodeId, Double latitude, Double longitude) {
    this.id = nodeId;
    this.lat = latitude;
    this.lon = longitude;
    this.coords = new ArrayList<>();
    this.dimension = 2;

  }

  /**
   * Overriding equals.
   * @param o the object to be checked
   * @return a boolean
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Node node = (Node) o;
    return Objects.equals(id, node.id) && Objects.equals(lat, node.lat)
        && Objects.equals(lon, node.lon)
        && Objects.equals(dimension, node.dimension);
  }

  /**
   * Overrides hashcode.
   * @return and integer
   */
  @Override
  public int hashCode() {
    return Objects.hash(id, lat, lon, dimension);
  }

  /**
   * Gets the list of outgoing edges for a node.
   * @return a list of edges
   */
  public List<Way> edges() {
    return ProgramRunner.getDb().fillEdges(this);
  }

  /**
   * Sets the node ID to the string passed in.
   * @param nodeId the nodeID to be set to
   */
  public void setID(String nodeId) {
    this.id = nodeId;
  }

  /**
   * Gets the ID of a node.
   * @return a string with the node ID
   */
  public String getID() {
    return this.id;
  }

  /**
   * Sets the latitude to the given input.
   * @param latitude the latitude to be set to
   */
  public void setLatitude(Double latitude) {
    this.lat = latitude;
  }

  /**
   * Gets the latitude of a node.
   * @return a double with the latitude
   */
  public Double getLatitude() {
    return this.lat;
  }

  /**
   * Sets the longitude to the given input.
   * @param longitude the longitude to be set to
   */
  public void setLongitude(Double longitude) {
    this.lon = longitude;
  }

  /**
   * Gets the longitude of a node.
   * @return a double with the longitude of a node
   */
  public Double getLongitude() {
    return this.lon;
  }

  /**
   * Gets the list of coordinates for a node.
   * @return a list of coordinates for a node
   */
  @Override
  public List<Double> getCoords() {
    this.coords.add(this.getLatitude());
    this.coords.add(this.getLongitude());
    return this.coords;
  }

  /**
   * Gets the dimension of a node.
   * @return integer of the dimension
   */
  @Override
  public int getNumDim() {
    return this.dimension;
  }

  /**
   * Calculates Haversine distance between the given node and the node inputted.
   * @param n the end node to calculate the distance
   * @return the distance between this node and the node inputted
   */
  public double getHeuristic(Node n) {
    Haversine formula = new Haversine();
    return formula.haversine(this.lat, this.lon, n.getLatitude(), n.getLongitude());
  }
}
