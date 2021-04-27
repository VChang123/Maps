package edu.brown.cs.student.stars.commands;

import edu.brown.cs.student.stars.abstractions.IMethod;
import edu.brown.cs.student.stars.components.MockPerson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * MockMethod class.
 */
public class MockMethod implements IMethod {

  /**
   * MockMethod constructor.
   */
  public MockMethod() { }

  /**
   * Executes the Mock Method.
   * @param args - List of Strings parsed in REPL.
   * @return a string of outputs.
   */
  public String execute(List<String> args) {
    List<String> toPrint = new ArrayList<>();
    String path = args.get(1);
    try (BufferedReader input = new BufferedReader(new FileReader(path))) {
      String line = input.readLine();
      while (line != null) {
        if (!line.equals("first_name,last_name,datetime,email,gender,street_address")) {
          String[] info = line.split(",");
          if (info.length == 0) {
            toPrint.add("ERROR: Malformed row\n");
          } else {
            MockPerson newMockPerson = new MockPerson(info[0], info[1], info[2], info[3],
                info[4], info[5]);
            toPrint.add(newMockPerson.toString());
          }
        }
        line = input.readLine();
      }
    } catch (FileNotFoundException e) {
      toPrint.add("ERROR: File does not exist\n");
    } catch (IOException e) {
      toPrint.add("ERROR: input/output exception\n");
    }
    return String.join("\n", toPrint);
  }
}
