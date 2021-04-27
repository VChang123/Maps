package edu.brown.cs.student.maps.components;

import edu.brown.cs.student.ProgramRunner;
import edu.brown.cs.student.maps.abstractions.Haversine;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Database class that handles all interactions with the database.
 */
public class Database {
  private Connection connect;
  private static final int NUM_CACHE = 500;
  private LoadingCache<Node, List<Way>> edgeCache;
  private LoadingCache<String, Node> nodeCache;

  /**
   * The constructor for the database class
   * When instantiated the initial connection is set to null.
   */
  public Database() {
    this.connect = null;
  }

  /**
   * Makes a connection between the filepath given to the database.
   *
   * @param datafile the file to create the connection to
   * @return a boolean which is true if a connection has been
   * successfully made.
   */
  public boolean makeConnect(String datafile) {
    try {
      Class.forName("org.sqlite.JDBC");
      String urlToDB = "jdbc:sqlite:" + datafile;
      this.connect = DriverManager.getConnection(urlToDB);
      //makes sure the database is valid before setting a connection
      if (this.checkValidDatabase()) {
        PreparedStatement prep1;
        prep1 = this.getConnect().prepareStatement("DROP TABLE IF EXISTS userdata");
        prep1.executeUpdate();
        prep1.close();
        PreparedStatement prep2;
        prep2 = this.getConnect().prepareStatement("CREATE TABLE IF NOT EXISTS userdata("
                + "id INTEGER,"
                + "username TEXT,"
                + "timeinterval DOUBLE,"
                + "latitude DOUBLE,"
                + "longitude DOUBLE)");
        prep2.executeUpdate();
        prep2.close();
        return true;
      }
    } catch (SQLException | ClassNotFoundException e) {
      e.getMessage();
    }
    return false;
  }

  /**
   * Gets the current connection.
   *
   * @return a connection
   */
  public Connection getConnect() {
    return this.connect;
  }

  /**
   * Sets the connection to the specified connection.
   *
   * @param conn the connection to be set to
   */
  public void setConnection(Connection conn) {
    this.connect = conn;
  }

  /**
   * Gets all the outgoing edges of a node.
   *
   * @param node the node to get the outgoing edges for
   * @return a list of outgoing edges
   */
  public List<Way> fillEdges(Node node) {
    //calls the cache
    fillEdgesCache();
    //returns the information from the cache
    return edgeCache.getUnchecked(node);
  }

  /**
   * A cache specifically for the outgoing edges of a node.
   */
  public void fillEdgesCache() {
    edgeCache = CacheBuilder.newBuilder()
            .maximumSize(NUM_CACHE)
            .build(new CacheLoader<>() {
              @Override
              public List<Way> load(Node n) {
                Database db = ProgramRunner.getDb();
                List<Way> output = new LinkedList<>();
                try {
                  //SQL statement that gets the outgoing edges from a node
                  String strSelect = "SELECT * FROM way WHERE way.start = ?"
                          + "AND way.type != 'unclassified' AND way.type != ''";
                  PreparedStatement prep = db.getConnect().prepareStatement(strSelect);
                  prep.setString(1, n.getID());
                  ResultSet result = prep.executeQuery();
                  while (result.next()) {
                    //extracts information
                    String wayID = result.getString("id");
                    String toID = result.getString("end");
                    String type = result.getString("type");
                    Node to = extractNode(toID);
                    Way newWay = new Way(wayID, n, to, type, calcWeight(n, to));
                    output.add(newWay);
                  }
                  result.close();
                  prep.close();
                } catch (SQLException e) {
                  System.out.println("ERROR: Could not execute SQL");
                }
                return output;
              }
            });
  }

  /**
   * Helper methode that calculated the weight of an edge.
   *
   * @param v1 the start node of that edge
   * @param v2 the end node of that edge
   * @return the distance between the start and end node of an edge.
   */
  public double calcWeight(Node v1, Node v2) {
    Haversine formula = new Haversine();
    double lat1 = v1.getLatitude();
    double lon1 = v1.getLongitude();
    double lat2 = v2.getLatitude();
    double lon2 = v2.getLongitude();
    return formula.haversine(lat1, lon1, lat2, lon2);
  }

  /**
   * Gets two nodes from the database.
   *
   * @param node1 the first node to be found
   * @param node2 the second node to be found
   * @return a list of nodes
   */
  public List<Node> getTwoNodes(String node1, String node2) {
    Database db = ProgramRunner.getDb();
    Node output = null;
    List<Node> nodes = new ArrayList<>();
    try {
      //SQL statement for getting a node's information
      String strSelect = "SELECT * FROM node WHERE node.id = ? OR node.id = ?";
      PreparedStatement prep = db.getConnect().prepareStatement(strSelect);
      prep.setString(1, node1);
      prep.setString(2, node2);
      ResultSet result = prep.executeQuery();
      while (result.next()) {
        //extract information and return results
        String id = result.getString("id");
        Double latitude = result.getDouble("latitude");
        Double longitude = result.getDouble("longitude");
        output = new Node(id, latitude, longitude);
        nodes.add(output);
      }
      result.close();
      prep.close();
    } catch (SQLException e) {
      System.out.println("ERROR: Could not execute SQL");
    }
    return nodes;
  }

  /**
   * Gets the node based on the node ID.
   *
   * @param nodeID the node ID to find
   * @return a node with that ID
   */
  public Node extractNode(String nodeID) {
    extractNodeCache();
    return nodeCache.getUnchecked(nodeID);
  }

  /**
   * A cache that keeps track of node IDs.
   */
  public void extractNodeCache() {
    nodeCache = CacheBuilder.newBuilder()
            .maximumSize(NUM_CACHE)
            .build(new CacheLoader<>() {
              @Override
              public Node load(String s) {
                Database db = ProgramRunner.getDb();
                Node output = null;
                try {
                  //SQL statement for getting a node's information
                  String strSelect = "SELECT * FROM node WHERE node.id = ?";
                  PreparedStatement prep = db.getConnect().prepareStatement(strSelect);
                  prep.setString(1, s);
                  ResultSet result = prep.executeQuery();
                  while (result.next()) {
                    //extract information and return results
                    String id = result.getString("id");
                    Double latitude = result.getDouble("latitude");
                    Double longitude = result.getDouble("longitude");
                    output = new Node(id, latitude, longitude);
                  }
                  result.close();
                  prep.close();
                } catch (SQLException e) {
                  System.out.println("ERROR: Could not execute SQL");
                }
                return output;
              }
            });
  }

  /**
   * SQL query that returns all traversable nodes and is used to make the KDTree.
   *
   * @return a list of traversable nodes
   */
  public List<Node> getTraversableNodes() {
    Connection newConnection = ProgramRunner.getDb().getConnect();
    List<Node> datapoints = new ArrayList<>();
    try {

      //find nodes traversable nodes
      String select = "SELECT node.* FROM node JOIN way "
              + "ON node.id = way.start OR node.id = way.end "
              + "WHERE way.type != 'unclassified' AND way.type != ''"
              + "GROUP BY node.id ";
      PreparedStatement prep = newConnection.prepareStatement(select);
      ResultSet result = prep.executeQuery();
      while (result.next()) {
        String id = result.getString("id");
        Double latitude = result.getDouble("latitude");
        Double longitude = result.getDouble("longitude");
        Node node = new Node(id, latitude, longitude);
        datapoints.add(node);
      }
      result.close();
      prep.close();
    } catch (SQLException e) {
      System.out.println("ERROR: Could not execute SQL");
    }
    return datapoints;
  }

  /**
   * An SQL query that gets all the ways in the specified parameters.
   *
   * @param lat1 the first latitude
   * @param lon1 the first longitude
   * @param lat2 the second latitude
   * @param lon2 the second longitude
   * @return a list of all the way ID's
   */
  public List<Way> getAllWays(Double lat1, Double lon1, Double lat2, Double lon2) {
    Connection conn = ProgramRunner.getDb().getConnect();
    List<Way> output = new ArrayList<>();
    try {

      String strSelect = "SELECT way.* FROM node JOIN way "
              + "ON way.start = node.id OR way.end = node.id "
              + "WHERE node.latitude <= ?"
              + "AND node.latitude >= ?"
              + "AND node.longitude <= ?"
              + "AND node.longitude >= ?"
              + "GROUP BY way.id ORDER BY way.id";
      PreparedStatement prep = conn.prepareStatement(strSelect);
      prep.setDouble(1, lat1);
      prep.setDouble(2, lat2);
      prep.setDouble(3, lon2);
      prep.setDouble(4, lon1);
      ResultSet result = prep.executeQuery();
      //extract the data from the results
      while (result.next()) {
        String wayID = result.getString("id");
        String startID = result.getString("start");
        String endID = result.getString("end");
        String type = result.getString("type");
        List<Node> nodes = getTwoNodes(startID, endID);
        Node startNode = nodes.get(0);
        Node endNode = nodes.get(1);
        Way newWay = new Way(wayID, startNode, endNode, type, calcWeight(startNode, endNode));
        output.add(newWay);
      }
      result.close();
      prep.close();

    } catch (SQLException e) {
      System.out.println("ERROR: SQL did not execute properly");
    }
    return output;
  }

  /**
   * An SQL query that gets the intersecting node between 2 streets.
   *
   * @param street1 the first street
   * @param street2 the second street
   * @return the node that is the intersection
   */
  public List<Node> getIntersection(String street1, String street2) {
    Connection conn = ProgramRunner.getDb().getConnect();
    List<Node> intersect = new ArrayList<>();
    try {

      String strSelect = "SELECT node.id,node.latitude,node.longitude FROM node JOIN way "
              + "ON node.id = way.start OR node.id = way.end WHERE way.name = ?"
              + "INTERSECT "
              + "SELECT node.id,node.latitude,node.longitude FROM node JOIN way "
              + "ON node.id = way.start OR node.id = way.end WHERE way.name = ?";

      PreparedStatement prep = conn.prepareStatement(strSelect);
      prep.setString(1, street1);
      prep.setString(2, street2);
      ResultSet result = prep.executeQuery();

      while (result.next()) {
        String id = result.getString("id");
        Double latitude = result.getDouble("latitude");
        Double longitude = result.getDouble("longitude");
        Node node = new Node(id, latitude, longitude);
        intersect.add(node);
      }
      result.close();
      prep.close();

    } catch (SQLException e) {
      System.out.println("ERROR: Could not execute SQL");
    }
    return intersect;
  }

  /**
   * Get all UserData from a Name.
   *
   * @param id the user name to get all the data for
   * @return a list of user data.
   */
  public List<UserCheckin> getAllUserData(Integer id) {
    Connection conn = ProgramRunner.getDb().getConnect();
    List<UserCheckin> data = new ArrayList<>();
    try {
      String strSelect = "SELECT * FROM userdata WHERE userdata.id = ?";
      PreparedStatement prep = conn.prepareStatement(strSelect);
      prep.setInt(1, id);
      ResultSet result = prep.executeQuery();
      while (result.next()) {
        Integer id2 = result.getInt("id");
        String username = result.getString("username");
        Double timestamp = result.getDouble("timeinterval");
        Double lat = result.getDouble("latitude");
        Double lon = result.getDouble("longitude");
        UserCheckin user = new UserCheckin(id2, username, timestamp, lat, lon);
        data.add(user);
      }
      result.close();
      prep.close();
    } catch (SQLException e) {
      System.out.println("ERROR: Could not query SQL");
    }
    return data;
  }

  /**
   * The data to be inserted into the table.
   *
   * @param id1        the id of the user
   * @param username1  the name of the user
   * @param timestamp1 the timestamp of the user
   * @param lat1       the latitude of the user
   * @param lon1       the longitude of the user
   */
  public void insertUserData(int id1, String username1,
                             double timestamp1, double lat1, double lon1) {

    try {
      PreparedStatement prep2;
      Connection conn = ProgramRunner.getDb().getConnect();
      String insert = "INSERT INTO userdata VALUES (?, ?, ?, ?, ?)";
      prep2 = conn.prepareStatement(insert);
      prep2.setInt(1, id1);
      prep2.setString(2, username1);
      prep2.setDouble(3, timestamp1);
      prep2.setDouble(4, lat1);
      prep2.setDouble(5, lon1);
      prep2.executeUpdate();
      prep2.close();
    } catch (SQLException e) {
      System.out.println("ERROR: Could not insert into table");
    }

  }

  /**
   * Checks if the Database is valid.
   *
   * @return a boolean, true if valid , false if invalid
   */
  public boolean checkValidDatabase() {
    Connection conn = ProgramRunner.getDb().getConnect();
    List<String> nodeTableNames = new ArrayList<>();
    List<String> wayTableNames = new ArrayList<>();
    boolean nodeTable = false;
    boolean wayTable = false;
    try {
      //checking the node table names
      String strSelect = "SELECT name FROM pragma_table_info('node')";
      PreparedStatement prep = conn.prepareStatement(strSelect);
      ResultSet result = prep.executeQuery();
      while (result.next()) {
        String name = result.getString("name");
        nodeTableNames.add(name);
      }
      result.close();
      prep.close();
    } catch (SQLException e) {
      e.getMessage();
    }
    if (nodeTableNames.size() == 3) {
      List<String> nodeNames = new ArrayList<>();
      nodeNames.add("id");
      nodeNames.add("latitude");
      nodeNames.add("longitude");
      nodeTable = this.checkListElements(nodeTableNames, nodeNames);
    }
    try {
      //checking the way table names
      String strSelect = "SELECT name FROM pragma_table_info('way')";
      PreparedStatement prep = conn.prepareStatement(strSelect);
      ResultSet result = prep.executeQuery();
      while (result.next()) {
        String name = result.getString("name");
        wayTableNames.add(name);
      }
      result.close();
      prep.close();
    } catch (SQLException e) {
      e.getMessage();
    }
    if (wayTableNames.size() == 5) {
      List<String> wayNames = new ArrayList<>();
      wayNames.add("id");
      wayNames.add("name");
      wayNames.add("type");
      wayNames.add("start");
      wayNames.add("end");
      wayTable = this.checkListElements(wayTableNames, wayNames);
    }
    if (nodeTable && wayTable) {
      return true;
    }
    System.out.println("ERROR: Database table is incorrectly formatted");
    return false;
  }

  /**
   * Helper method that checks if the names of the tables are what they should be.
   *
   * @param list1 the output list from the sql query to get column names
   * @param list2 the list of names the table should have
   * @return a boolean, true if it contains all the names, false if they do not.
   */
  public boolean checkListElements(List<String> list1, List<String> list2) {
    for (int i = 0; i < list2.size(); i++) {
      if (!list1.contains(list2.get(i))) {
        System.out.println("ERROR: Database Table is incorrectly formatted");
        return false;
      }
    }
    return true;
  }
}
