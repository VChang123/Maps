package edu.brown.cs.student.maps;

import edu.brown.cs.student.ProgramRunner;
import edu.brown.cs.student.maps.commands.MapCommand;
import edu.brown.cs.student.maps.commands.RouteCommand;
import edu.brown.cs.student.maps.components.Database;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RouteCommandTest {

  private MapCommand mapComm = new MapCommand();
  private RouteCommand routeCommand = new RouteCommand();

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
    executeString.add("route");
    String output = routeCommand.execute(executeString);
    assertEquals(output, "ERROR: Ill-formed route command");

    input.clear();
    input.add("map");
    input.add("data/maps/smallMaps.sqlite3");
    mapComm.execute(input);
    List<String> executeString2 = new ArrayList<>();
    executeString2.add("route");
    executeString2.add("hello");
    executeString2.add("sdjkfl");
    executeString2.add("asdjfjkfl");
    executeString2.add("sdwtejwuphjkfl");
    String output2 = routeCommand.execute(executeString2);
    assertEquals(output2, "ERROR: Invalid parameters for route command");
  }

  /**
   * Checks if there is connection before calling route command
   */
  @Test
  public void nullConnectionTest(){
    List<String> executeString = new ArrayList<>();
    executeString.add("route");
    executeString.add("40.12");
    executeString.add("-70.234");
    executeString.add("12.32478");
    executeString.add("-7.1");
    String output = routeCommand.execute(executeString);
    assertEquals(output, "ERROR: No connection to Database");
  }

  /**
   * Checks output of the route command on big database
   */
  @Test
  public void bigDBTest(){
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/maps.sqlite3");
    mapComm.execute(input);

    // tests intersection
    List<String> executeStringIntersect = new ArrayList<>();
    executeStringIntersect.add("route");
    executeStringIntersect.add("\"Waterman Street\"");
    executeStringIntersect.add("\"Prospect Street\"");
    executeStringIntersect.add("\"Angell Street\"");
    executeStringIntersect.add("\"Hope Street\"");
    String outputIntersect = routeCommand.execute(executeStringIntersect);
    assertEquals(outputIntersect,
        "/n/4182.7140.1955940309 -> /n/4182.7140.1955930559 : /w/4182.7140.19402129.16.1\n" +
        "/n/4182.7140.1955930559 -> /n/4182.7140.1955930563 : /w/4182.7140.19402129.17.1\n" +
        "/n/4182.7140.1955930563 -> /n/4182.7140.1955930555 : /w/4182.7140.19402129.18.1\n" +
        "/n/4182.7140.1955930555 -> /n/4182.7140.1955930561 : /w/4182.7140.19402129.19.1\n" +
        "/n/4182.7140.1955930561 -> /n/4182.7140.1957912106 : /w/4182.7140.19402129.20.1\n" +
        "/n/4182.7140.1957912106 -> /n/4182.7140.1874015357 : /w/4182.7140.19402129.21.1\n" +
        "/n/4182.7140.1874015357 -> /n/4182.7140.1975107996 : /w/4182.7140.19402129.22.1\n" +
        "/n/4182.7140.1975107996 -> /n/4182.7140.1955930553 : /w/4182.7140.19402129.23.1\n" +
        "/n/4182.7140.1955930553 -> /n/4182.7140.1955930557 : /w/4182.7140.19402129.24.1\n" +
        "/n/4182.7140.1955930557 -> /n/4182.7140.1955940297 : /w/4182.7140.19402129.25.1\n" +
        "/n/4182.7140.1955940297 -> /n/4182.7139.1957915164 : /w/4182.7140.19402129.26.1\n" +
        "/n/4182.7139.1957915164 -> /n/4182.7139.1957915187 : /w/4182.7139.19402129.27.1\n" +
        "/n/4182.7139.1957915187 -> /n/4182.7139.201365753 : /w/4182.7139.19402129.28.1\n" +
        "/n/4182.7139.201365753 -> /n/4182.7139.201365755 : /w/4182.7139.132173987.14.1\n" +
        "/n/4182.7139.201365755 -> /n/4182.7139.201718946 : /w/4182.7139.19401852.1.1\n" +
        "/n/4182.7139.201718946 -> /n/4182.7139.201718950 : /w/4182.7139.49071762.18.1\n" +
        "/n/4182.7139.201718950 -> /n/4182.7139.201280078 : /w/4182.7139.49071762.19.1");

    // test coordinates and no path
    List<String> executeStringCoords = new ArrayList<>();
    executeStringCoords.add("route");
    executeStringCoords.add("41.153187");
    executeStringCoords.add("-71.559311");
    executeStringCoords.add("41.153683");
    executeStringCoords.add("-71.558861");
    String outputCoords = routeCommand.execute(executeStringCoords);
    assertEquals(outputCoords, "/n/4136.7159.1607709582 -/- /n/4136.7159.1607709582");
  }
  /**
   * Checks output of the route command on small database
   */
  @Test
  public void smallDBTest(){
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/smallMaps.sqlite3");
    mapComm.execute(input);

    // test coords
    List<String> executeStringCoords = new ArrayList<>();
    executeStringCoords.add("route");
    executeStringCoords.add("41.8");
    executeStringCoords.add("-71.3");
    executeStringCoords.add("42");
    executeStringCoords.add("-72");
    String outputCoords = routeCommand.execute(executeStringCoords);
    assertEquals(outputCoords, "/n/0 -> /n/1 : /w/0\n" +
        "/n/1 -> /n/2 : /w/1\n" +
        "/n/2 -> /n/5 : /w/4");

    // tests intersection
    List<String> executeStringIntersect = new ArrayList<>();
    executeStringIntersect.add("route");
    executeStringIntersect.add("\"Sootball Ln\"");
    executeStringIntersect.add("\"Chihiro Ave\"");
    executeStringIntersect.add("\"Sootball Ln\"");
    executeStringIntersect.add("\"Yubaba St\"");
    String outputIntersect = routeCommand.execute(executeStringIntersect);
    assertEquals(outputIntersect, "/n/1 -> /n/4 : /w/3");
  }

  /**
   * Checks output of the route command on empty database
   */
  @Test
  public void emptyDBTest(){
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/empty.db");
    mapComm.execute(input);

    // intersection
    List<String> executeStringIntersect = new ArrayList<>();
    executeStringIntersect.add("route");
    executeStringIntersect.add("\"Waterman Street\"");
    executeStringIntersect.add("\"Prospect Street\"");
    executeStringIntersect.add("\"Angell Street\"");
    executeStringIntersect.add("\"Hope Street\"");
    String outputIntersect = routeCommand.execute(executeStringIntersect);
    assertEquals(outputIntersect, "ERROR: An intersection does not exist between two of the roads");

    // coords
    List<String> executeString = new ArrayList<>();
    executeString.add("route");
    executeString.add("314.237");
    executeString.add("237.2");
    executeString.add("-123.413");
    executeString.add("124.236");
    String output = routeCommand.execute(executeString);
    assertEquals(output, "ERROR: No traversable nodes found at given coordinates");
  }
}
