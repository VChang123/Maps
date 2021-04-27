package edu.brown.cs.student.maps.commands;

import edu.brown.cs.student.ProgramRunner;
import edu.brown.cs.student.maps.components.Database;
import edu.brown.cs.student.maps.components.Node;
import edu.brown.cs.student.stars.abstractions.IMethod;
import edu.brown.cs.student.stars.components.KDTree;

import java.io.File;
import java.sql.Connection;
import java.util.List;

/**
 * Class that handles the Maps Command.
 */
public class MapCommand implements IMethod {

  /**
   * Constructor of the Maps Command.
   */
  public MapCommand() {

  }

  /**
   * Reads in the database file.
   *
   * @param args - List of Strings parsed in REPL.
   * @return a string of outputs
   */
  public String execute(List<String> args) {
    String toPrint = null;
    Connection newConnection;
    Connection copyConnect;
    KDTree.KDNode<Node> copyTree = null;
    Database db = ProgramRunner.getDb();
    boolean setConnection;
    if (args.size() == 2) {
      File file = new File(args.get(1));
      //get the database file pathway
      String datafile = args.get(1);
      //copy information just incase
      copyConnect = ProgramRunner.getDb().getConnect();
      copyTree = ProgramRunner.getDataKD().getRoot();
      List<Node> datapoints;
      if (file.exists()) {
        //connect and download the database
        setConnection = ProgramRunner.getDb().makeConnect(datafile);
        ProgramRunner.getDb().setConnection(ProgramRunner.getDb().getConnect());
        //get the connection
        newConnection = ProgramRunner.getDb().getConnect();
        //if the connection is not null
        if (newConnection != null && setConnection) {
          toPrint = String.format("map set to %s", datafile);
          //find nodes traversable nodes
          datapoints = ProgramRunner.getDb().getTraversableNodes();
          //make kdtree
          ProgramRunner.getDataKD().createTree(datapoints, 2, 0);
        } else {
          //if the connection happens to error, set it back to the previous database
          db.setConnection(copyConnect);
          ProgramRunner.getDataKD().setRoot(copyTree);
        }
      } else {
        //if the connection does not go through set the info back to the previous info
        db.setConnection(copyConnect);
        ProgramRunner.getDataKD().setRoot(copyTree);
        toPrint = "ERROR: File doesn't exist";
      }
    } else {
      //if the connection happens to error, set it back to the previous database
      db.setConnection(null);
      ProgramRunner.getDataKD().setRoot(copyTree);
      //if the input argument does not have the correct length
      toPrint = "ERROR: Ill-formed maps command";
    }
    return toPrint;
  }
}
