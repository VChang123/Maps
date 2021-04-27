package edu.brown.cs.student.maps.components;

import edu.brown.cs.student.maps.abstractions.Compairator;
import edu.brown.cs.student.maps.abstractions.IEdge;
import edu.brown.cs.student.maps.abstractions.IVertex;
import edu.brown.cs.student.maps.abstractions.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Dijkstras class that contains the Dijikstras algorithm.
 * @param <V> a vertex
 * @param <E> an edge
 */
public class DijkstraAStar<V extends IVertex<V, E>, E extends IEdge<V, E>> {
  private static final double INF = Double.POSITIVE_INFINITY;

  /**
   * Constructor.
   */
  public DijkstraAStar() {
  }

  /**
   * Main Dijkstras algorithm.
   * @param start the start node
   * @param end the end node
   * @return a list of edges
   */
  public List<E> algorithm(V start, V end) {
    // initialize pq and distances
    Set<V> visited = new HashSet<>();
    Map<V, Double> distances = new HashMap<>();
    Map<V, E> parents = new HashMap<>();
    PriorityQueue<Pair<V, Double>> pq = new PriorityQueue<>(new Compairator<>());
    init(start, distances, pq);

    // update distances
    while (!pq.isEmpty()) {
      // get next Pair
      Pair<V, Double> head = pq.poll();
      V curr = head.getFst();
      // check if we've reached destination
      if (end.equals(curr)) {
        break;
      }
      if (!visited.contains(curr)) {
        List<E> edgesList = curr.edges();
        for (E edge : edgesList) {
          // only addEdgeInfo of edge has not been added to distances
          V tgt = edge.to();
          if (!distances.containsKey(tgt)) {
            addEdgeInfo(edge, distances, pq);
          }
          // new dist, compare
          double newDist = edge.weight() + distances.get(curr);
          if (Double.compare(newDist, distances.get(tgt)) == -1) {
            distances.replace(tgt, newDist);    // update distances
            parents.put(tgt, edge);   // update parent
            double heuristic = tgt.getHeuristic(end);
            Pair<V, Double> newPair = new Pair<>(tgt, distances.get(tgt) + heuristic);
            pq.add(newPair);  // update pq
          }
        }
        visited.add(curr);    // finished w curr
      }
    }
    List<E> path = this.shortestPath(start, end, parents);
    //printTotalDist(path);
    return path;
  }

  /**
   * Prints the total distance of the path.
   * @param path the path to print the distance of
   */
  public void printTotalDist(List<E> path) {
    double total = 0.;
    for (E edge : path) {
      total += edge.weight();
    }
    System.out.println(total);
  }

  /**
   * Recursively gets the parent of the current node to get the full path.
   * @param start the starting node
   * @param end the ending node
   * @param path the final path
   * @param parents the map of nodes
   * @return a List of Edges
   */
  public List<E> shortestPathHelper(V start, V end, List<E> path, Map<V, E> parents) {
    E firstWay = parents.get(end);
    if (firstWay != null) {
      if (!firstWay.from().equals(start)) {
        shortestPathHelper(start, firstWay.from(), path, parents);
      }
      path.add(firstWay);
    }
    return path;
  }

  /**
   * Gets the shortest path from start to end.
   * @param start the start node
   * @param end the end node
   * @param parents the map of nodes
   * @return a list of edges
   */
  public List<E> shortestPath(V start, V end, Map<V, E> parents) {
    return shortestPathHelper(start, end, new ArrayList<>(), parents);
  }

  /**
   * Initializes the start information for the priority queue.
   * @param source the start node
   * @param distances the distance map
   * @param pq the priority queue.
   */
  public void init(V source, Map<V, Double> distances, PriorityQueue<Pair<V, Double>> pq) {
    Pair<V, Double> newPairSource = new Pair<>(source, 0.);
    pq.add(newPairSource);
    distances.put(source, newPairSource.getScnd());
  }

  /**
   * Adds the information needed to an edge.
   * @param edge the edge the information needs to be added to.
   * @param distances the distance map
   * @param pq the priority queue.
   */
  public void addEdgeInfo(E edge, Map<V, Double> distances, PriorityQueue<Pair<V, Double>> pq) {
    Pair<V, Double> newPairEdge = new Pair<>(edge.to(), INF);
    distances.put(edge.to(), newPairEdge.getScnd());
    pq.add(newPairEdge);
  }
}

