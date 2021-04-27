package edu.brown.cs.student.maps;

import edu.brown.cs.student.ProgramRunner;
import edu.brown.cs.student.maps.commands.MapCommand;
import edu.brown.cs.student.maps.components.Database;
import edu.brown.cs.student.maps.components.Node;
import edu.brown.cs.student.stars.components.KDTree;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Map Command tests
 */
public class MapCommandTest {

  private MapCommand mapComm = new MapCommand();

  /**
   * Sets up the database in the beginning
   */
  @Before
  public void setUp(){
    ProgramRunner.setDb(new Database());
  }

  /**
   * Checks for the correct parameters in the input.
   */
  @Test
  public void illFormedCommandTest(){
    List<String> input = new ArrayList<>();
    input.add("map");
    String message = mapComm.execute(input);
    assertEquals("ERROR: Ill-formed maps command", message);

  }

  /**
   * Checks to see if error is thrown when the file does not exist
   */
  @Test
  public void nonexistentFileTest(){
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/hello.db");
    String message = mapComm.execute(input);
    assertEquals("ERROR: File doesn't exist", message);
  }

  /**
   * Checks if a small database is correctly loaded
   */
  @Test
  public void smallDBTest(){
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/smallMaps.sqlite3");
    String message = mapComm.execute(input);
    assertEquals("map set to data/maps/smallMaps.sqlite3", message);
  }
  /**
   * Checks if a big database is correctly loaded
   */
  @Test
  public void bigDBTest(){
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/maps.sqlite3");
    String message = mapComm.execute(input);
    assertEquals("map set to data/maps/maps.sqlite3", message);
  }
  /**
   * Checks if a empty database is correctly loaded
   */
  @Test
  public void emptyDBTest(){
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/empty.db");
    String message = mapComm.execute(input);
    assertEquals("map set to data/maps/empty.db", message);
  }
}
