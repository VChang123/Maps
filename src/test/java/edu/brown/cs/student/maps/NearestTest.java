package edu.brown.cs.student.maps;

import edu.brown.cs.student.ProgramRunner;
import edu.brown.cs.student.maps.commands.MapCommand;
import edu.brown.cs.student.maps.commands.Nearest;
import edu.brown.cs.student.maps.components.Database;
import edu.brown.cs.student.maps.components.Node;
import edu.brown.cs.student.stars.components.KDTree;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;

public class NearestTest {
  // wrong input
  // invalid coords
  // connection null
  // nearest on big db
  // nearest on empty db (nearest neighbor search returns empty)
  // nearest on small db

  private MapCommand mapComm = new MapCommand();
  private Nearest nearest = new Nearest();

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
    executeString.add("nearest");
    String output = nearest.execute(executeString);
    assertEquals(output, "ERROR: Ill-formed nearest command");
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
    executeString.add("nearest");
    executeString.add("hello");
    executeString.add("sdjkfl");
    String output = nearest.execute(executeString);
    assertEquals(output, "ERROR: Invalid parameters for nearest command");
  }

  /**
   * Checks if there is connection before calling nearest command
   */
  @Test
  public void nullConnectionTest(){
    List<String> executeString = new ArrayList<>();
    executeString.add("nearest");
    executeString.add("40.12");
    executeString.add("-70.234");
    String output = nearest.execute(executeString);
    assertEquals(output, "ERROR: No Database available");
  }

  /**
   * Checks output of the nearest command on big database
   */
  @Test
  public void bigDBTest(){
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/maps.sqlite3");
    mapComm.execute(input);
    List<String> executeString = new ArrayList<>();
    executeString.add("nearest");
    executeString.add("40.12");
    executeString.add("-70.234");
    String output = nearest.execute(executeString);
    assertEquals(output, "/n/4146.7116.201238418");
  }
  /**
   * Checks output of the nearest command on small database
   */
  @Test
  public void smallDBTest(){
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/smallMaps.sqlite3");
    mapComm.execute(input);
    List<String> executeString = new ArrayList<>();
    executeString.add("nearest");
    executeString.add("40.12");
    executeString.add("-70.234");
    String output = nearest.execute(executeString);
    assertEquals(output, "/n/0");
  }
  /**
   * Checks output of the nearest command on empty database
   */
  @Test
  public void emptyDBTest(){
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/empty.db");
    mapComm.execute(input);
    List<String> executeString = new ArrayList<>();
    executeString.add("nearest");
    executeString.add("40.12");
    executeString.add("-70.234");
    String output = nearest.execute(executeString);
    assertEquals(output, "");
  }
}
