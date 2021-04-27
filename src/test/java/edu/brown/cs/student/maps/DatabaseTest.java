package edu.brown.cs.student.maps;

import edu.brown.cs.student.ProgramRunner;
import edu.brown.cs.student.maps.commands.MapCommand;
import edu.brown.cs.student.maps.components.Database;
import edu.brown.cs.student.maps.components.Node;
import edu.brown.cs.student.maps.components.UserCheckin;
import edu.brown.cs.student.maps.components.Way;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DatabaseTest {
  private MapCommand mapComm = new MapCommand();

  public DatabaseTest (){

  }
  /**
   * Sets up the database in the beginning
   */
  @Before
  public void setUp(){
    ProgramRunner.setDb(new Database());
  }

  /**
   * Checks if and invalid database does not get loaded in.
   */
  @Test
  public void invalidDatabaseTest() {
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/notValid.db");
    String output = mapComm.execute(input);
    assertEquals(output, null);
  }

  /**
   * Tests if an empty database can be loaded in.
   */
  @Test
  public void emptyDatabaseTest() {
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/empty.db");
    String output = mapComm.execute(input);
    assertEquals(output,"map set to data/maps/empty.db");

  }

  /**
   * Tests if all the ways in the bounding box are correctly gotten.
   */
  @Test
  public void getAllWaysTest() {
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/smallMaps.sqlite3");
    mapComm.execute(input);
    List<Way> output = ProgramRunner.getDb().getAllWays(42.0, -72.0, 41.8 ,-71.3);
    assertEquals(output.size(), 7);

  }

  /**
   * Tests if all the outgoing edges are gotten for a node
   */
  @Test
  public void fillEdgesTest() {
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/smallMaps.sqlite3");
    mapComm.execute(input);
    Node param = new Node("/n/0",41.82,-71.4 );
    List<Way> output = ProgramRunner.getDb().fillEdges(param);
    assertEquals(output.size(), 2);

  }

  /**
   * Tests if two nodes are extracted correctly.
   */
  @Test
  public void getTwoNodesTest() {
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/smallMaps.sqlite3");
    mapComm.execute(input);
    List<Node> output = ProgramRunner.getDb().getTwoNodes("/n/0", "/n/1");
    assertEquals(output.size(), 2);
    assertTrue(output.get(0).getLatitude() == 41.82 );
    assertTrue(output.get(0).getLongitude() == -71.4 );
    assertTrue(output.get(1).getLatitude() == 41.8203);
    assertTrue(output.get(1).getLongitude() == -71.4);
  }

  /**
   * Tests is the correct node is gotten when the name is written.
   */
  @Test
  public void extractNodeTest() {
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/smallMaps.sqlite3");
    mapComm.execute(input);
    Node output = ProgramRunner.getDb().extractNode("/n/0");
    assertTrue(output.getLatitude() == 41.82 );
    assertTrue(output.getLongitude() == -71.4 );
  }

  /**
   * Tests the traversable nodes are correct.
   */
  @Test
  public void getTraversableNodesTest() {
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/smallMaps.sqlite3");
    mapComm.execute(input);
    List<Node> output = ProgramRunner.getDb().getTraversableNodes();
    assertEquals(output.size(), 6);
  }

  /**
   * Tests if the correct intersection is retrieved.
   */
  @Test
  public void getIntersectionTest() {
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/smallMaps.sqlite3");
    mapComm.execute(input);
    List<Node> output = ProgramRunner.getDb().getIntersection("Chihiro Ave", "Sootball Ln");
    assertEquals(output.get(0).getID(), "/n/1" );
  }

  /**
   * Tests if the userdata table is created
   */
  @Test
  public void checkTableCreation() {
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/userdata.db");
    mapComm.execute(input);
    List<String> datanames = new ArrayList<>();
    try {
      //checking the way table names
      Connection conn = ProgramRunner.getDb().getConnect();
      String strSelect = "SELECT name FROM pragma_table_info('userdata')";
      PreparedStatement prep = conn.prepareStatement(strSelect);
      ResultSet result = prep.executeQuery();
      while (result.next()) {
        String name = result.getString("name");
        datanames.add(name);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    assertTrue(datanames.contains("id"));
    assertTrue(datanames.contains("username"));
    assertTrue(datanames.contains("timeinterval"));
    assertTrue(datanames.contains("latitude"));
    assertTrue(datanames.contains("longitude"));
  }

  /**
   * Tests if the user data is correctly inserted in to the table
   */
  @Test
  public void insertDataTest() {
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/userdata.db");
    mapComm.execute(input);
    ProgramRunner.getDb().insertUserData(1, "hello",234.23, 41, -70);
    List<UserCheckin> data = new ArrayList<>();
    try{
      Connection conn = ProgramRunner.getDb().getConnect();
      PreparedStatement prep;
      prep = conn.prepareStatement("SELECT * FROM userdata WHERE userdata.id = ?");
      prep.setInt(1,1);
      ResultSet result = prep.executeQuery();
      while (result.next()) {
        int id = result.getInt("id");
        String name = result.getString("username");
        Double timestamp = result.getDouble("timeinterval");
        Double lat = result.getDouble("latitude");
        Double lon = result.getDouble("longitude");
        UserCheckin user1 = new UserCheckin(id, name, timestamp, lat, lon);
        data.add(user1);
      }
    }catch (SQLException e) {
      e.printStackTrace();
    }
    assertEquals(data.size(), 1);
    assertEquals(data.get(0).getId(), 1);
    assertEquals(data.get(0).getName(), "hello");
    assertTrue(Double.compare(data.get(0).getTimestamp(),234.23) == 0);
    assertTrue(Double.compare(data.get(0).getLat(), 41) == 0);
    assertTrue(Double.compare(data.get(0).getLon(),-70) == 0);

  }

  /**
   * Tests if the user data is retrieved properly
   */
  @Test
  public void getUserData() {
    List<String> input = new ArrayList<>();
    input.add("map");
    input.add("data/maps/userdata.db");
    mapComm.execute(input);
    ProgramRunner.getDb().insertUserData(1, "hello", 234.23, 41, -70);
    ProgramRunner.getDb().insertUserData(1, "hello", 345.25, 43, -71);
    ProgramRunner.getDb().insertUserData(1, "hello", 34.23, 42, -72);
    List<UserCheckin> data = new ArrayList<>();
    try {
      Connection conn = ProgramRunner.getDb().getConnect();
      PreparedStatement prep;
      prep = conn.prepareStatement("SELECT * FROM userdata WHERE userdata.id = ?");
      prep.setInt(1, 1);
      ResultSet result = prep.executeQuery();
      while (result.next()) {
        int id = result.getInt("id");
        String name = result.getString("username");
        Double timestamp = result.getDouble("timeinterval");
        Double lat = result.getDouble("latitude");
        Double lon = result.getDouble("longitude");
        UserCheckin user1 = new UserCheckin(id, name, timestamp, lat, lon);
        data.add(user1);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    assertTrue(data.size()==3);
    assertTrue(Double.compare(data.get(0).getTimestamp(),234.23) == 0);
    assertTrue(Double.compare(data.get(1).getTimestamp(),345.25) == 0);
    assertTrue(Double.compare(data.get(2).getTimestamp(),34.23) == 0);
  }
}
