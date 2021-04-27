package edu.brown.cs.student.maps.abstractions;

/**
 * The Edge interface.
 * @param <V> the Vertex type
 * @param <E> the Edge type
 */
public interface IEdge<V extends IVertex<V, E>, E extends IEdge<V, E>> {
  /**
   * Gets the start vertex of an edge.
   * @return the start vertex
   */
  V from();

  /**
   * Gets the end vertex of an edge.
   * @return the end vertex
   */
  V to();

  /**
   * Gets the weight of the edge.
   * @return the weight of the edge in form of a double
   */
  double weight();
}
