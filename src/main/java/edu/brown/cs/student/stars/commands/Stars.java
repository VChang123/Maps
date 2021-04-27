package edu.brown.cs.student.stars.commands;

import edu.brown.cs.student.ProgramRunner;
import edu.brown.cs.student.stars.abstractions.IMethod;
import edu.brown.cs.student.stars.components.KDTree;
import edu.brown.cs.student.stars.components.StarObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * StarsMethod class.
 */
public class Stars implements IMethod {

  private List<StarObject> objList;

  /**
   * StarsMethod constructor.
   */
  public Stars() {
    this.objList = ProgramRunner.getObjList();
  }

  /**
   * Executes the star command.
   * @param args - List of Strings parsed in REPL.
   * @return a list of outputs
   */
  public String execute(List<String> args) {
    List<String> toPrint = new ArrayList<>();
    // case: syntax error
    if (args.size() != 2) {
      toPrint.add("ERROR: syntax is stars [filepath]");
    } else {
      String path = args.get(1);
      if (this.objList == null) {
        this.objList = new ArrayList<>();
      } else {
        this.objList.clear();
      }
      try (BufferedReader input = new BufferedReader(new FileReader(path))) {
        String line = input.readLine();
        while (line != null) {
          // skip first line of csv
          if (!line.equals("StarID,ProperName,X,Y,Z")) {
            try {
              String[] info = line.split(",");
              // case: erroneously-formatted csv
              if (info.length == 0) {
                toPrint.add("ERROR: ID and coordinates must be numbers");
              } else {
                // extract star info from row
                int id = Integer.parseInt(info[0]);
                String name = info[1];
                List<Double> coords = new ArrayList<>();
                coords.add(Double.parseDouble(info[2]));
                coords.add(Double.parseDouble(info[3]));
                coords.add(Double.parseDouble(info[4]));
                // create new star from info and add to objList
                StarObject newStar = new StarObject(id, name, coords, coords.size());
                this.objList.add(newStar);
              }
            } catch (NumberFormatException e) {
              toPrint.add("ERROR: ID and coordinates must be numbers");
            }
          }
          line = input.readLine();
        }
        // check size of stars list to print result
        if (this.objList.size() > 0) {
          toPrint.add(String.format("Read %d stars from %s", this.objList.size(), path));
        }
      } catch (FileNotFoundException e) {
        toPrint.add("ERROR: File does not exist");
      } catch (IOException e) {
        toPrint.add("ERROR: input/output exception");
      }
    }
    // update star list and create kd tree in ProgramRunner
    if (objList != null) {
      ProgramRunner.setObjList(objList);
      KDTree<StarObject> newKD = new KDTree<>();
      newKD.createTree(objList, 3, 0);
      ProgramRunner.setKd(newKD);
    }
    return String.join("\n", toPrint);
  }
}
