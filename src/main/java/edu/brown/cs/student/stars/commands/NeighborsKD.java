package edu.brown.cs.student.stars.commands;

import edu.brown.cs.student.ProgramRunner;
import edu.brown.cs.student.maps.abstractions.NearestNeighborsSearch;
import edu.brown.cs.student.stars.abstractions.IMethod;
import edu.brown.cs.student.stars.abstractions.DistanceCalculator;
import edu.brown.cs.student.stars.components.KDTree;
import edu.brown.cs.student.stars.components.StarObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * NeighborsMethodKD class.
 */
public class NeighborsKD extends DistanceCalculator implements IMethod {
  /**
   * Field for NeighborsMethodKD.
   */
  private List<StarObject> objList;
  private List<StarObject> neighborsToPrint;
  private NearestNeighborsSearch<StarObject> search = new NearestNeighborsSearch<>();

  /**
   * NeighborsMethodKD constructor.
   */
  public NeighborsKD() { }

  @Override
  public String execute(List<String> args) {
    String toPrint = null;
    this.objList = ProgramRunner.getObjList();
    // case: stars not called
    if (this.objList == null) {
      toPrint = "ERROR: Call stars command to read in data";
    } else if (args.size() == 5) {          // case: search by coordinates
      try {
        int numNeighbors = Integer.parseInt(args.get(1));
        // case: number of neighbors < 0
        if (numNeighbors < 0) {
          toPrint = "ERROR: Number of neighbors must be non-negative";
        } else {
          // extracts target coordinates from input
          List<Double> targetCoords = new ArrayList<>();
          targetCoords.add(Double.parseDouble(args.get(2)));
          targetCoords.add(Double.parseDouble(args.get(3)));
          targetCoords.add(Double.parseDouble(args.get(4)));
          KDTree.KDNode<StarObject> root = ProgramRunner.getKd().getRoot();
          // runs neighbors algorithm and record results in neighborsToPrint
          List<StarObject> neighbors = this.search.neighbors(root, targetCoords, numNeighbors,
              new ArrayList<>());
          // converts neighbors list to id list and adds it to toPrint
          this.neighborsToPrint = neighbors;
          List<String> idList = new ArrayList<>();
          for (StarObject n : neighbors) {
            idList.add(String.valueOf(n.getId()));
          }
          if (idList.size() != 0) {
            toPrint = String.join("\n", idList);
          }
        }
      } catch (NumberFormatException e) {
        toPrint = "ERROR: Number of neighbors and coordinates must be numbers";
      }
    } else if (args.size() == 3) {        // case: search by name
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
              int numNeighbors = Integer.parseInt(args.get(1));
              // case: number of neighbors is negative
              if (numNeighbors < 0) {
                toPrint = "ERROR: Number of neighbors must be non-negative";
              } else {
                // case: all valid inputs
                KDTree.KDNode<StarObject> root = ProgramRunner.getKd().getRoot();
                // runs neighbors algorithm and record results in neighborsToPrint
                List<StarObject> neighbors = this.search.neighbors(root, target.getCoords(),
                    numNeighbors + 1, new ArrayList<>());
                neighbors.remove(target);
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
              toPrint = "ERROR: Number of neighbors and coordinates must be numbers";
            }
          }
        }
      } else {
        // this means regex wasn't able to extract anything
        toPrint = "ERROR: Star name must be in quotes";
      }
    } else {
      // case: syntax error
      toPrint = "ERROR: syntax is neighbors k x y z or neighbors k \"name\"";
    }
    return toPrint;
  }

  /**
   * iterates through starsList and finds star with matching name.
   * @param starName - name of StarObject to find
   * @return if StarObject with name exists, return that Star; otherwise, return null
   */
  public StarObject findMatchingStar(String starName) {
    for (StarObject s : this.objList) {
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
