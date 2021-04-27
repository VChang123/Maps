package edu.brown.cs.student;

import edu.brown.cs.student.stars.abstractions.IMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that executes repl.
 */
public class REPL {
  private HashMap<String, IMethod> commands;

  /**
   * Constructor for REPL object.
   */
  public REPL() {
    commands = new HashMap<>();
  }

  /**
   * register command into REPL.
   * @param name name of the command
   * @param ca object that implements CommandAction, which contains instructions for a command
   */
  public void registerCommand(String name, IMethod ca) {
    commands.put(name, ca);
  }

  /**
   * start REPL method.
   */
  public void startRepl() {
    try {
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      String line = in.readLine();

      // break REPL if EOF
      while (line != null) {
        // split line by spaces except in quotes - I consulted a post from stackoverflow
        // https://stackoverflow.com/questions/7804335/split-string-on-spaces-in-java-except-if-between-quotes-i-e-treat-hello-wor
        List<String> tokens = new ArrayList<>();
        Pattern p = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"");
        Matcher m = p.matcher(line);
        if (!line.equals("")) {
          while (m.find()) {
            tokens.add(m.group());
          }
          parseTokens(tokens);
        }
        line = in.readLine();
      }
     // close buffered reader
      in.close();
    } catch (IOException e) {
      System.err.println("ERROR: failed to read line");
    }
  }

  //method to parse tokens of each row
  private void parseTokens(List<String> tokens) {
    // empty token
    if (tokens.size() == 0) {
      return;
    }

    String ca = tokens.get(0);

    // check if no command
    if (ca == null) {
      System.out.println("ERROR: Invalid Command");
      return;
    }

    // valid command action found
    if (commands.containsKey(ca)) {
      String output = commands.get(ca).execute(tokens);
      if (output != null && !output.equals("")) {
        System.out.println(output);
      }
    } else {
      System.out.printf("ERROR: %s is not a valid command\n", ca);
    }
  }
}
