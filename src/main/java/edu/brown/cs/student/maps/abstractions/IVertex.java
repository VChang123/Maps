package edu.brown.cs.student.maps.abstractions;


import java.util.List;

/**
 * Vertex interface.
 * @param <V> the Vertex type
 * @param <E> the Edge type
 */
public interface IVertex<V extends IVertex<V, E>, E extends IEdge<V, E>> {
  /**
   * Gets the outgoing edges for a vertex.
   * @return a List of edges
   */
  List<E> edges();

  /**
   * Gets the Heuristic between the vertex and the vertex passed in.
   * @param vertex the vertex to calculate the heuristic to.
   * @return a distance between the two vertexes.
   */
  double getHeuristic(V vertex);
}
