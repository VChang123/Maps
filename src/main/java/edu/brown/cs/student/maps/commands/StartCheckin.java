package edu.brown.cs.student.maps.commands;
import edu.brown.cs.student.ProgramRunner;
import edu.brown.cs.student.stars.abstractions.IMethod;

import java.util.List;

/**
 * The command that starts the checkin server thread.
 */
public class StartCheckin implements IMethod {
  /**
   * Executes the start of the checkin server thread.
   * @param args - List of Strings parsed in REPL.
   * @return a String denoting weather or not the thread was successfully started or not.
   */
  @Override
  public String execute(List<String> args) {
    String toString;
    if (ProgramRunner.getDb().getConnect() != null) {
      if (args.size() == 1) {
        ProgramRunner.getThread().start();
        toString = "Server thread started";
      } else {
        toString = "Could not start server thread";
      }
    } else {
      toString = "ERROR: No database loaded in";
    }

    return toString;
  }
}
