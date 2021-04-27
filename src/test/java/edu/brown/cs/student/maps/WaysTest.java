package edu.brown.cs.student.maps;

import edu.brown.cs.student.ProgramRunner;
import edu.brown.cs.student.maps.commands.MapCommand;
import edu.brown.cs.student.maps.commands.Ways;
import edu.brown.cs.student.maps.components.Database;
import edu.brown.cs.student.maps.components.Node;
import edu.brown.cs.student.stars.components.KDTree;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class WaysTest {


  private MapCommand mapComm = new MapCommand();
  private Ways ways = new Ways();

  /**
   * Sets up the database in the beginning
   */
  @Before
  public void setUp(){
    ProgramRunner.setDb(new Database());
  }

  /**
   * Checks if the input is invalid
   */
  @Test
  public void invalidInputTest(){
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/smallMaps.sqlite3");
    mapComm.execute(input);
    List<String> executeString = new ArrayList<>();
    executeString.add("ways");
    String output = ways.execute(executeString);
    assertEquals(output, "ERROR: Ill-formed ways command");
  }

  /**
   * Checks if the input coordinates are valid
   */
  @Test
  public void invalidCoordinateTest(){
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/smallMaps.sqlite3");
    mapComm.execute(input);
    List<String> executeString = new ArrayList<>();
    executeString.add("ways");
    executeString.add("hello");
    executeString.add("sdjkfl");
    executeString.add("sdjkfl");
    executeString.add("sdjkfl");
    String output = ways.execute(executeString);
    assertEquals(output, "ERROR: Invalid parameters for ways command");

    List<String> executeString2 = new ArrayList<>();
    executeString2.add("ways");
    executeString2.add("41.2");
    executeString2.add("-70.3");
    executeString2.add("42.3");
    executeString2.add("-71.3");
    String output2 = ways.execute(executeString2);
    assertEquals(output2, "ERROR: Invalid coordinates");
  }

  /**
   * Checks if there is connection before calling ways command
   */
  @Test
  public void nullConnectionTest(){
    List<String> executeString = new ArrayList<>();
    executeString.add("ways");
    executeString.add("40.12");
    executeString.add("-70.234");
    executeString.add("-70.234");
    executeString.add("-70.234");
    String output = ways.execute(executeString);
    assertEquals(output, "ERROR: No database connected");
  }

  /**
   * Checks output of the ways command on big database
   */
  @Test
  public void bigDBTest(){
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/maps.sqlite3");
    mapComm.execute(input);
    List<String> executeString = new ArrayList<>();
    executeString.add("ways");
    executeString.add("41.125");
    executeString.add("-71.05");
    executeString.add("41.1");
    executeString.add("-71");
    String output = ways.execute(executeString);
    assertEquals(output, "/w/4110.7099.42295268.990.2\n" +
        "/w/4110.7100.42295268.987.2\n" +
        "/w/4110.7100.42295268.988.1\n" +
        "/w/4110.7100.42295268.988.2\n" +
        "/w/4110.7100.42295268.989.1\n" +
        "/w/4110.7100.42295268.989.2\n" +
        "/w/4110.7100.42295268.990.1\n" +
        "/w/4110.7101.42295268.986.2\n" +
        "/w/4110.7101.42295268.987.1\n" +
        "/w/4111.7101.42295268.984.2\n" +
        "/w/4111.7101.42295268.985.1\n" +
        "/w/4111.7101.42295268.985.2\n" +
        "/w/4111.7101.42295268.986.1\n" +
        "/w/4111.7102.42295268.981.2\n" +
        "/w/4111.7102.42295268.982.1\n" +
        "/w/4111.7102.42295268.982.2\n" +
        "/w/4111.7102.42295268.983.1\n" +
        "/w/4111.7102.42295268.983.2\n" +
        "/w/4111.7102.42295268.984.1\n" +
        "/w/4112.7102.42295268.980.2\n" +
        "/w/4112.7102.42295268.981.1\n" +
        "/w/4112.7103.42295268.980.1");
  }
  /**
   * Checks output of the ways command on small database
   */
  @Test
  public void smallDBTest(){
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/smallMaps.sqlite3");
    mapComm.execute(input);
    List<String> executeString = new ArrayList<>();
    executeString.add("ways");
    executeString.add("42");
    executeString.add("-72");
    executeString.add("41.8");
    executeString.add("-71.3");
    String output = ways.execute(executeString);
    assertEquals(output, "/w/0\n" +
        "/w/1\n" +
        "/w/2\n" +
        "/w/3\n" +
        "/w/4\n" +
        "/w/5\n" +
        "/w/6");
  }
  /**
   * Checks output of the ways command on empty database
   */
  @Test
  public void emptyDBTest(){
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/empty.db");
    mapComm.execute(input);
    List<String> executeString = new ArrayList<>();
    executeString.add("ways");
    executeString.add("42");
    executeString.add("-72");
    executeString.add("41.8");
    executeString.add("-71.3");
    String output = ways.execute(executeString);
    assertEquals(output, "");
  }

}
