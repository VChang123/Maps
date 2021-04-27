package edu.brown.cs.student.maps.abstractions;

import edu.brown.cs.student.stars.abstractions.CoordDistanceComparator;
import edu.brown.cs.student.stars.abstractions.DistanceCalculator;
import edu.brown.cs.student.stars.abstractions.ICoordinate;
import edu.brown.cs.student.stars.components.KDTree;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Class that implements nearest neighbor search.
 * @param <T> the object type the nearest neighbor search will be performed on
 */
public class NearestNeighborsSearch<T extends ICoordinate> extends DistanceCalculator {

  /**
   * Traverses KD-Tree to find the k nearest neighbors and outputs them in a list.
   * @param root - KDTree to traverse
   * @param targetCoords - coordinates of target Star
   * @param k - number of neighbors to find
   * @param neighborsList - list of neighbors to fill and return
   * @return neighborsList with the k nearest neighbors
   */
  public List<T> neighbors(KDTree.KDNode<T> root, List<Double> targetCoords,
                           int k, List<T> neighborsList) {
    // base case: if kd.val is null return neighborsList
    if (root == null || k == 0) {
      return neighborsList;
    }

    // tree info
    T currVal = root.getObject();
    int numDim = currVal.getNumDim();
    List<Double> currCoords = root.getObject().getCoords();
    int axis = root.getDepth() % numDim;

    if (neighborsList.size() < k) {
      neighborsList.add(currVal);
    } else {
      // distances
      double targetToCurrent = distance(targetCoords, currCoords);

      ICoordinate farthestNeighbor = neighborsList.get(k - 1);
      double targetToFarthestNeighbor = distance(targetCoords, farthestNeighbor.getCoords());

      if (targetToCurrent < targetToFarthestNeighbor) {
        neighborsList.set(k - 1, currVal);
      } else if (Double.compare(targetToCurrent, targetToFarthestNeighbor) == 0) {
        Random r = new Random();
        if (r.nextBoolean()) {
          neighborsList.set(k - 1, currVal);
        }
      }
    }

    if (neighborsList.size() == 0) {
      return neighbors(root.getRight(), targetCoords, k,
          neighbors(root.getLeft(), targetCoords, k, neighborsList));
    }

    Collections.sort(neighborsList, new CoordDistanceComparator(targetCoords));

    ICoordinate farthestNeighbor = neighborsList.get(neighborsList.size() - 1);
    double targetToFarthestNeighbor = distance(targetCoords, farthestNeighbor.getCoords());
    double targetToCurrentAxisDist = Math.abs(targetCoords.get(axis) - currCoords.get(axis));

    if ((targetToFarthestNeighbor >= targetToCurrentAxisDist) || neighborsList.size() < k) {
      return neighbors(root.getRight(), targetCoords, k,
          neighbors(root.getLeft(), targetCoords, k, neighborsList));
    } else {
      if (currCoords.get(axis) < targetCoords.get(axis)) {
        return neighbors(root.getRight(), targetCoords, k, neighborsList);
      } else {
        return neighbors(root.getLeft(), targetCoords, k, neighborsList);
      }
    }
  }
}
