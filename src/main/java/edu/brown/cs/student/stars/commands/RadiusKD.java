package edu.brown.cs.student.stars.commands;

import edu.brown.cs.student.ProgramRunner;
import edu.brown.cs.student.stars.abstractions.IMethod;
import edu.brown.cs.student.stars.abstractions.CoordDistanceComparator;
import edu.brown.cs.student.stars.abstractions.DistanceCalculator;
import edu.brown.cs.student.stars.components.KDTree;
import edu.brown.cs.student.stars.components.StarObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RadiusKD class.
 */
public class RadiusKD extends DistanceCalculator implements IMethod {

  /**
   * RadiusKD fields.
   */
  private List<StarObject> objList;
  private List<StarObject> neighborsToPrint;

  /**
   * RadiusKD constructor.
   */
  public RadiusKD() {  }

  @Override
  public String execute(List<String> args) {
    String toPrint = null;
    this.objList = ProgramRunner.getObjList();
    // case: stars not called
    if (this.objList == null) {
      toPrint = "ERROR: Call stars command to read in data";
    } else if (args.size() == 5) {          // case: search by coordinates
      try {
        double radius = Double.parseDouble(args.get(1));
        // case: radius < 0
        if (radius < 0) {
          toPrint = "ERROR: Radius must be non-negative";
        } else {
          // extracts target coordinates from input
          List<Double> targetCoords = new ArrayList<>();
          targetCoords.add(Double.parseDouble(args.get(2)));
          targetCoords.add(Double.parseDouble(args.get(3)));
          targetCoords.add(Double.parseDouble(args.get(4)));
          KDTree.KDNode<StarObject> root = ProgramRunner.getKd().getRoot();
          // runs radius algorithm and record results in neighborsToPrint
          List<StarObject> neighbors = radius(root, targetCoords, radius, new ArrayList<>(), null);
          this.neighborsToPrint = neighbors;
          // converts neighbors list to id list and adds it to toPrint
          List<String> idList = new ArrayList<>();
          for (StarObject n : neighbors) {
            idList.add(String.valueOf(n.getId()));
          }
          if (idList.size() != 0) {
            toPrint = String.join("\n", idList);
          }
        }
      } catch (NumberFormatException e) {
        toPrint = "ERROR: Radius and coordinates must be numbers";
      }
    } else if (args.size() == 3) {          // case: search by name
      // extract star name from quotes
      Pattern p = Pattern.compile("\"([^\"]*)\"");
      Matcher m = p.matcher(args.get(2));
      String starName;
      if (m.find()) {
        starName = m.group(1);
        // case: star without a name
        if (starName.equals("")) {
          toPrint = "ERROR: Cannot search for star without a name";
        } else {
          StarObject target = findMatchingStar(starName);
          if (target == null) {
            // case: star not found
            toPrint = "ERROR: Star not found";
          } else {
            // case: star is found
            try {
              double radius = Double.parseDouble(args.get(1));
              // case: radius is negative
              if (radius < 0) {
                toPrint = "ERROR: Radius must be non-negative";
              } else {
                // case: all valid inputs
                KDTree.KDNode<StarObject> root = ProgramRunner.getKd().getRoot();
                // runs neighbors algorithm and record results in neighborsToPrint
                List<StarObject> neighbors = radius(root, target.getCoords(), radius,
                    new ArrayList<>(), target);
                this.neighborsToPrint = neighbors;
                // converts neighbors list to id list and adds it to toPrint
                List<String> idList = new ArrayList<>();
                for (StarObject n : neighbors) {
                  idList.add(String.valueOf(n.getId()));
                }
                if (idList.size() != 0) {
                  toPrint = String.join("\n", idList);
                }
              }
            } catch (NumberFormatException e) {
              toPrint = "ERROR: Radius and coordinates must be numbers";
            }
          }
        }
      } else {
        // case: regex wasn't able to extract anything
        toPrint = "ERROR: Star name must be in quotes";
      }
    } else {
      // case: syntax error
      toPrint = "ERROR: syntax is radius k x y z or radius k \"name\"";
    }
    return toPrint;
  }

  /**
   * Traverses KD-Tree to find the neighbors within the specified radius and outputs them in a list.
   * @param root - KDTree to traverse
   * @param targetCoords - coordinates of target Star
   * @param r - radius to search
   * @param neighborsList - list of neighbors to fill and return
   * @param byName - name of Star, if searched for by name
   * @return - neighborsList with the neighbors in r
   */
  public List<StarObject> radius(KDTree.KDNode<StarObject> root, List<Double> targetCoords,
                                 double r, List<StarObject> neighborsList, StarObject byName) {
    // base case: if kd.val is null return neighborsList
    if (root == null) {
      return neighborsList;
    }

    // tree info
    StarObject currVal = root.getObject();
    int numDim = currVal.getNumDim();
    List<Double> currCoords = root.getObject().getCoords();
    int axis = root.getDepth() % numDim;

    // distances
    double targetToCurrent = distance(targetCoords, currCoords);
    double targetToCurrentAxisDist = Math.abs(targetCoords.get(axis) - currCoords.get(axis));

    boolean skip = false;

    if (byName != null) {
      skip = currVal.getId() == byName.getId();
    }

    if (!skip) {
      if (targetToCurrent <= r) {
        neighborsList.add(currVal);
        neighborsList.sort(new CoordDistanceComparator(targetCoords));
      }
    }

    if (r >= targetToCurrentAxisDist) {
      return radius(root.getRight(), targetCoords, r,
          radius(root.getLeft(), targetCoords, r, neighborsList, byName), byName);
    } else {
      if (currCoords.get(axis) < targetCoords.get(axis)) {
        return radius(root.getRight(), targetCoords, r, neighborsList, byName);
      } else {
        return radius(root.getLeft(), targetCoords, r, neighborsList, byName);
      }
    }
  }

  /**
   * iterates through starsList and finds star with matching name.
   * @param starName - name of StarObject to find
   * @return if StarObject with name exists, return that StarObject; otherwise, return null
   */
  public StarObject findMatchingStar(String starName) {
    for (StarObject s : objList) {
      if (s.getName().equals(starName)) {
        return s;
      }
    }
    return null;
  }

  /**
   * getter for neighborsToPrint.
   * @return - neighborsToPrint
   */
  public List<StarObject> getNeighborsToPrint() {
    return neighborsToPrint;
  }
}
