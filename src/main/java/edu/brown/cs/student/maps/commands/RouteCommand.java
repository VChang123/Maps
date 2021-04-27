package edu.brown.cs.student.maps.commands;

import edu.brown.cs.student.ProgramRunner;
import edu.brown.cs.student.maps.abstractions.NearestNeighborsSearch;
import edu.brown.cs.student.maps.components.DijkstraAStar;
import edu.brown.cs.student.maps.components.Node;
import edu.brown.cs.student.maps.components.Way;
import edu.brown.cs.student.stars.abstractions.IMethod;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that handles the Route Method.
 */
public class RouteCommand implements IMethod {
  private NearestNeighborsSearch<Node> search = new NearestNeighborsSearch<>();
  private DijkstraAStar<Node, Way> algorithm = new DijkstraAStar<>();
  private List<Way> pathway = new ArrayList<>();
  /**
   * Constructor for the route method.
   */
  public RouteCommand() {

  }

  /**
   * Executes the route methods actions.
   *
   * @param args - List of Strings parsed in REPL.
   * @return a string of outputs
   */
  @Override
  public String execute(List<String> args) {
    //will use dijkstras for the traversable node portion
    String toPrint = null;
    int info = this.extractInfo(args).size();
    List<String> streetName = this.extractInfo(args);
    if (ProgramRunner.getDb().getConnect() != null) {
      if (args.size() == 5) {
        if (info == 4) {
          //street/crossroads given
          String st1 = streetName.get(0);
          String cross1 = streetName.get(1);
          String st2 = streetName.get(2);
          String cross2 = streetName.get(3);
          //find the closest traversable node
          List<Node> firstNode = ProgramRunner.getDb().getIntersection(st1, cross1);
          List<Node> secondNode = ProgramRunner.getDb().getIntersection(st2, cross2);
          //if there is an intersection between the roads given
          if (firstNode.size() != 0 && secondNode.size() != 0) {
            Node node1 = ProgramRunner.getDb().getIntersection(st1, cross1).get(0);
            Node node2 = ProgramRunner.getDb().getIntersection(st2, cross2).get(0);
            List<Node> start1 = search.neighbors(ProgramRunner.getDataKD().getRoot(),
                    node1.getCoords(), 1, new ArrayList<>());
            List<Node> start2 = search.neighbors(ProgramRunner.getDataKD().getRoot(),
                    node2.getCoords(), 1, new ArrayList<>());
            Node start = start1.get(0);
            Node end = start2.get(0);
            //use A* to find the shortest path
            pathway = algorithm.algorithm(start, end);
            List<String> output = new ArrayList<>();
            //print out the path
            if (pathway.size() != 0) {
              for (Way e : pathway) {
                String format = String.format("%s -> %s : %s", e.from().getID(), e.to().getID(),
                        e.getId());
                output.add(format);
              }
            } else {
              //if there is no path available
              String format = String.format("%s -/- %s", start.getID(), end.getID());
              output.add(format);
            }
            toPrint = String.join("\n", output);
          } else {
            toPrint = "ERROR: An intersection does not exist between two of the roads";
          }
        } else if (info == 0) {
          //locations were given
          try {
            double lat1 = Double.parseDouble(args.get(1));
            double lon1 = Double.parseDouble(args.get(2));
            double lat2 = Double.parseDouble(args.get(3));
            double lon2 = Double.parseDouble(args.get(4));
            //get the information need to run A*
            List<Double> node1 = new ArrayList<>();
            node1.add(lat1);
            node1.add(lon1);
            List<Node> result = search.neighbors(
                    ProgramRunner.getDataKD().getRoot(), node1, 1, new ArrayList<>());
            List<Double> node2 = new ArrayList<>();
            node2.add(lat2);
            node2.add(lon2);
            List<Node> result2 = search.neighbors(
                    ProgramRunner.getDataKD().getRoot(), node2, 1, new ArrayList<>());
            if (result.size() != 0 && result2.size() != 0) {
              Node start = result.get(0);
              Node end = result2.get(0);
              //run the algorithm
              pathway = algorithm.algorithm(start, end);
              List<String> output = new ArrayList<>();
              //print out the path
              if (pathway.size() != 0) {
                for (Way e : pathway) {
                  String format = String.format("%s -> %s : %s", e.from().getID(), e.to().getID(),
                          e.getId());
                  output.add(format);
                }
              } else {
                String format = String.format("%s -/- %s", start.getID(), end.getID());
                output.add(format);
              }
              toPrint = String.join("\n", output);
            } else {
              toPrint = "ERROR: No traversable nodes found at given coordinates";
            }
          } catch (NumberFormatException e) {
            toPrint = "ERROR: Invalid parameters for route command";
          }
        } else {
          toPrint = "ERROR: Invalid parameters for route command";
        }
      } else {
        toPrint = "ERROR: Ill-formed route command";
      }
    } else {
      toPrint = "ERROR: No connection to Database";
    }
    return toPrint;
  }

  /**
   * Checks if the input are all Locations or all numbers.
   *
   * @param input a list of the user input
   * @return a boolean, false if all inputs are doubles and true if the
   * input is all street names.
   */
  public List<String> extractInfo(List<String> input) {
    //matches the pattern with the inputs given
    Pattern p = Pattern.compile("\"([^\"]*)\"");
    List<String> streets = new ArrayList<>();
    for (int i = 1; i < input.size(); i++) {
      Matcher m = p.matcher(input.get(i));
      String streetname;
      if (m.find()) {
        streetname = m.group(1);
        streets.add(streetname);
      }
    }
    return streets;
  }

  /**
   * Gets the final pathway.
   * @return the final pathway in the form of a lists
   */
  public List<Way> getFinalPathway() {
    return this.pathway;
  }
}
