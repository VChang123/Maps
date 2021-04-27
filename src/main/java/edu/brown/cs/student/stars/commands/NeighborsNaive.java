package edu.brown.cs.student.stars.commands;

import edu.brown.cs.student.ProgramRunner;
import edu.brown.cs.student.stars.abstractions.IMethod;
import edu.brown.cs.student.stars.abstractions.CoordDistanceComparator;
import edu.brown.cs.student.stars.components.StarObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * NeighborsNaive class.
 */
public class NeighborsNaive implements IMethod {

  /**
   * NeighborsNaive fields.
   */
  private List<StarObject> objList;
  private List<StarObject> neighborsToPrint;

  /**
   * NeighborsMethod constructor.
   */
  public NeighborsNaive() {  }

  /**
   * Executes the naive neighbors command.
   * @param args - List of Strings parsed in REPL.
   * @return a string of outputs
   */
  public String execute(List<String> args) {
    String toPrint = null;
    this.objList = ProgramRunner.getObjList();
    // case: stars not called
    if (objList == null) {
      toPrint = "ERROR: Call stars command to read in data";
    } else {
      Collections.shuffle(objList);
      if (args.size() == 5) {       // case: search by coordinate
        try {
          // extracts target coordinates from input
          List<Double> targetCoords = new ArrayList<>();
          targetCoords.add(Double.parseDouble(args.get(2)));
          targetCoords.add(Double.parseDouble(args.get(3)));
          targetCoords.add(Double.parseDouble(args.get(4)));
          // sorts list by distance from target coordinates
          Collections.sort(objList, new CoordDistanceComparator(targetCoords));
          int neighbors = Integer.parseInt(args.get(1));
          // case: number of neighbors < 0
          if (neighbors < 0) {
            toPrint = "ERROR: Number of neighbors must be non-negative";
          } else {
            int index = 0;          // tracks current index
            int counter = 0;        // tracks size of neighbors list
            List<String> idList = new ArrayList<>();
            this.neighborsToPrint = new ArrayList<>();
            // saves neighbors in field and converts them to list of ids
            while (index < objList.size() && counter < neighbors) {
              StarObject s = objList.get(index);
              neighborsToPrint.add(s);
              idList.add(String.valueOf(s.getId()));
              counter++;
              index++;
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
          // case: search for star without name
          if (starName.equals("")) {
            toPrint = "ERROR: Cannot search for star without a name";
          } else {
            StarObject target = findMatchingStar(starName, objList);
            // case: star not in list
            if (target == null) {
              toPrint = "ERROR: Star not found";
            } else {
              // sorts list by distance from target coordinates
              Collections.sort(objList, new CoordDistanceComparator(target.getCoords()));
              try {
                int neighbors = Integer.parseInt(args.get(1));
                // case: number of neighbors < 0
                if (neighbors < 0) {
                  toPrint = "ERROR: Number of neighbors must be non-negative";
                } else {
                  int index = 0;          // tracks current index
                  int counter = 0;        // tracks size of neighbors list
                  List<String> idList = new ArrayList<>();
                  neighborsToPrint = new ArrayList<>();
                  // saves neighbors in field and converts them to list of ids
                  while (index < objList.size() && counter < neighbors) {
                    StarObject s = objList.get(index);
                    if (s.getId() != target.getId()) {
                      neighborsToPrint.add(s);
                      idList.add(String.valueOf(s.getId()));
                      counter++;
                    }
                    index++;
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
          // case: regex wasn't able to extract anything
          toPrint = "ERROR: Star name must be in quotes";
        }
      } else {
        // case: syntax error
        toPrint =
            "ERROR: syntax is naive_neighbors k x y z or naive_neighbors k \"name\"";

      }
    }
    return toPrint;
  }

  /**
   * iterates through starsList and finds StarObject with matching name.
   * @param starName - name of StarObject to find
   * @param starsList - list of StarObjects to search
   * @return if StarObject with name exists, return that StarObject; otherwise, return null
   */
  public StarObject findMatchingStar(String starName, List<StarObject> starsList) {
    for (StarObject s : starsList) {
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
