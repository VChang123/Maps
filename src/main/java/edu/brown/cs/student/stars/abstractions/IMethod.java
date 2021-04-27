package edu.brown.cs.student.stars.abstractions;

import java.util.List;

/**
 * IMethod interface.
 */
public interface IMethod {

  /**
   * Executes the specified command.
   *
   * @param args - List of Strings parsed in REPL.
   * @return - a String that contains the result of execute.
   */
  String execute(List<String> args);
}
