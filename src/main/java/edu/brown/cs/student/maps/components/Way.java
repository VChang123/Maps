package edu.brown.cs.student.maps.components;

import edu.brown.cs.student.maps.abstractions.IEdge;

/**
 * Way object class.
 */
public class Way implements IEdge<Node, Way> {
  private String id;
  private Node from;
  private Node to;
  private double weight;
  private String classify;

  /**
   * Way Constructor.
   * @param id the way ID
   * @param from the start node of a way
   * @param to the end node of a way
   * @param weight the weight of the way
   * @param type the type of the way
   */
  public Way(String id, Node from, Node to, String type, double weight) {
    this.id = id;
    this.from = from;
    this.to = to;
    this.classify = type;
    this.weight = weight;
  }

  /**
   * Gets the type of a way.
   * @return the type of the way
   */
  public String getClassify() {
    return classify;
  }

  /**
   * Sets the type of the way.
   * @param classify the type the way should be classified as.
   */
  public void setClassify(String classify) {
    this.classify = classify;
  }

  /**
   * Gets the way id.
   * @return the way id in the form of a string
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the id of a way.
   * @param id the id to be set to
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Gets the start node of a way.
   * @return the start node of a way
   */
  public Node from() {
    return from;
  }

  /**
   * Sets the start node of a way.
   * @param from the node to be set
   */
  public void setFrom(Node from) {
    this.from = from;
  }
  /**
   * Gets the end node of a way.
   * @return the end node of a way
   */
  public Node to() {
    return to;
  }

  /**
   * Sets the end node of a way.
   * @param to the node to be set to
   */
  public void setTo(Node to) {
    this.to = to;
  }

  /**
   * Gets the weight of an way.
   * @return the weight of an way
   */
  public double weight() {
    return weight;
  }

  /**
   * Sets the weight of an way.
   * @param weight the wieght to set the way to
   */
  public void setWeight(double weight) {
    this.weight = weight;
  }
}

