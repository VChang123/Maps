package edu.brown.cs.student.maps.commands;

import edu.brown.cs.student.ProgramRunner;
import edu.brown.cs.student.maps.components.Node;
import edu.brown.cs.student.maps.abstractions.NearestNeighborsSearch;
import edu.brown.cs.student.stars.abstractions.IMethod;


import java.util.ArrayList;
import java.util.List;

/**
 * Class that handles the nearest Command.
 */
public class Nearest implements IMethod {

  private NearestNeighborsSearch<Node> search = new NearestNeighborsSearch<>();
  private List<Node> nearestNode = new ArrayList<>();

  /**
   * Constructor for the Nearest Command.
   */
  public Nearest() {
  }

  /**
   * Finds the nearest ways and returns them.
   *
   * @param args - List of Strings parsed in REPL.
   * @return a string of outputs
   */
  @Override
  public String execute(List<String> args) {
    String toPrint;
    //check if input is the correct size
    if (args.size() == 3) {
      if (ProgramRunner.getDb().getConnect() != null) {
        try {
          //get the information needed to find the nearest node
          double lat1 = Double.parseDouble(args.get(1));
          double lon1 = Double.parseDouble(args.get(2));
          List<Double> location = new ArrayList<>();
          location.add(lat1);
          location.add(lon1);
          //call the nearest neighbors search algorithm
          nearestNode = this.search.neighbors(
                  ProgramRunner.getDataKD().getRoot(), location, 1, new ArrayList<>());
          if (nearestNode.size() != 0) {
            //if there is a node print out the id
            toPrint = String.format("%s", nearestNode.get(0).getID());
          } else {
            //otherwise print out nothing
            toPrint = "";
          }
        } catch (NumberFormatException e) {
          toPrint = "ERROR: Invalid parameters for nearest command";
        }
      } else {
        toPrint = "ERROR: No Database available";
      }
    } else {
      toPrint = "ERROR: Ill-formed nearest command";
    }
    return toPrint;
  }

  /**
   * Returns the nearest node.
   * @return the nearest node
   */
  public Node getNearestNode() {
    Node result = null;
    if (nearestNode.size() != 0) {
      result = nearestNode.get(0);
    }
    return result;
  }
}

