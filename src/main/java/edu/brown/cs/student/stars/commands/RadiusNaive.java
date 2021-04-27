package edu.brown.cs.student.stars.commands;

import edu.brown.cs.student.ProgramRunner;
import edu.brown.cs.student.stars.abstractions.IMethod;
import edu.brown.cs.student.stars.abstractions.CoordDistanceComparator;
import edu.brown.cs.student.stars.abstractions.DistanceCalculator;
import edu.brown.cs.student.stars.components.StarObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RadiusNaive class.
 */
public class RadiusNaive extends DistanceCalculator implements IMethod {

  /**
   * RadiusNaive fields.
   */
  private List<StarObject> objList;
  private List<StarObject> neighborsToPrint;

  /**
   * RadiusNaive constructor.
   */
  public RadiusNaive() {  }

  /**
   * Executes the naive radius command.
   * @param args - List of Strings parsed in REPL.
   * @return a string of outputs.
   */
  public String execute(List<String> args) {
    String toPrint = null;
    this.objList = ProgramRunner.getObjList();
    // case: stars not called
    if (objList == null) {
      toPrint = "ERROR: Call stars command to read in data";
    } else if (args.size() == 5) {        // case: search by coordinate
      try {
        // extracts target coordinates from input
        List<Double> targetCoords = new ArrayList<>();
        targetCoords.add(Double.parseDouble(args.get(2)));
        targetCoords.add(Double.parseDouble(args.get(3)));
        targetCoords.add(Double.parseDouble(args.get(4)));
        // sorts list by distance from target coordinates
        Collections.sort(objList, new CoordDistanceComparator(targetCoords));
        double radius = Double.parseDouble(args.get(1));
        // case: radius < 0
        if (radius < 0) {
          toPrint = "ERROR: Radius must be non-negative";
        } else {
          // saves neighbors in field and converts them to list of ids
          List<String> idList = new ArrayList<>();
          neighborsToPrint = new ArrayList<>();
          for (StarObject s : objList) {
            double distance = distance(targetCoords, s.getCoords());
            if (distance <= radius) {
              neighborsToPrint.add(s);
              idList.add(String.valueOf(s.getId()));
            }
          }
          if (idList.size() != 0) {
            toPrint = String.join("\n", idList);
          }
        }
      } catch (NumberFormatException e) {
        toPrint = "ERROR: Radius and coordinates must be numbers";
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
          StarObject target = findMatchingStar(starName, (objList));
          // case: star not in list
          if (target == null) {
            toPrint = "ERROR: Star not found";
          } else {
            // sorts list by distance from target coordinates
            Collections.sort(objList, new CoordDistanceComparator(target.getCoords()));
            try {
              double radius = Double.parseDouble(args.get(1));
              // case: radius < 0
              if (radius < 0) {
                toPrint = "ERROR: Radius must be non-negative";
              } else {
                // saves neighbors in field and converts them to list of ids
                List<String> idList = new ArrayList<>();
                neighborsToPrint = new ArrayList<>();
                for (StarObject s : objList) {
                  double distance = distance(target.getCoords(), s.getCoords());
                  if (distance <= radius && s.getId() != target.getId()) {
                    neighborsToPrint.add(s);
                    idList.add(String.valueOf(s.getId()));
                  }
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
      toPrint = "ERROR: syntax is naive_radius k x y z or naive_radius k \"name\"";
    }
    return toPrint;
  }

  /**
   * iterates through starsList and finds star with matching name.
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
