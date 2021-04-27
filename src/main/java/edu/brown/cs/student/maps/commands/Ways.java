package edu.brown.cs.student.maps.commands;

import edu.brown.cs.student.ProgramRunner;
import edu.brown.cs.student.maps.components.Way;
import edu.brown.cs.student.stars.abstractions.IMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that implements that Ways command.
 */
public class Ways implements IMethod {
  private  List<Way> output = new ArrayList<>();
  /**
   * Constructor for the Ways command.
   */
  public Ways() {  }

  /**
   * Executes the ways command.
   * @param args - List of Strings parsed in REPL.
   * @return a string of outputs.
   */
  @Override
  public String execute(List<String> args) {
    String toPrint;
    List<String> result = new ArrayList<>();
    //if the argument is the correct size for the ways command
    if (args.size() == 5) {
      //makes sure that there is a database connection
      if (ProgramRunner.getDb().getConnect() != null) {
        try {
          //get the input values from the ways command
          double lat1 = Double.parseDouble(args.get(1));
          double lon1 = Double.parseDouble(args.get(2));
          double lat2 = Double.parseDouble(args.get(3));
          double lon2 = Double.parseDouble(args.get(4));
          if (lat1 >= lat2 || lon1 <= lon2) {
            //gets the ways from the database
            output = ProgramRunner.getDb().getAllWays(lat1, lon1, lat2, lon2);
            //print out the ways
            if (output.size() != 0) {
              for (Way e: output) {
                String format = String.format("%s", e.getId());
                result.add(format);
              }
              toPrint = String.join("\n", result);
            } else {
              toPrint = "";
            }
          } else {
            toPrint = "ERROR: Invalid coordinates";
          }
        } catch (NumberFormatException e) {
          toPrint = "ERROR: Invalid parameters for ways command";
        }
      } else {
        toPrint = "ERROR: No database connected";
      }
    } else {
      toPrint = "ERROR: Ill-formed ways command";
    }
    return toPrint;
  }
  /**
   * Returns the this of outputted ways.
    * @return a list of ways
   */
  public List<Way> getListofWays() {
    return this.output;
  }
}
