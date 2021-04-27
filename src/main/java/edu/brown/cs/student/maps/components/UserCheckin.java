package edu.brown.cs.student.maps.components;

/**
 * class for passing user checkin data.
 */
public class UserCheckin {

  private int id;
  private String name;
  private double ts;
  private double lat;
  private double lon;

  /**
   * Constructor for the usercheckin class.
   * @param userId the ID of the user
   * @param username the name of the user
   * @param timestamp the time of the user
   * @param latitude the latitude of the user
   * @param longitude the longitude of the user
   */
  public UserCheckin(int userId, String username, double timestamp,
                     double latitude, double longitude) {
    id = userId;
    name = username;
    ts = timestamp;
    lat = latitude;
    lon = longitude;
  }

  /**
   * Gets the user id.
   * @return returns the user id.
   */
  public int getId() {
    return id;
  }

  /**
   * Gets the name of the user.
   * @return the name of the user
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the time of the user.
   * @return the timestamp of the user
   */
  public double getTimestamp() {
    return ts;
  }

  /**
   * Gets the latitude of the user.
   * @return the latitude of the user
   */
  public double getLat() {
    return lat;
  }

  /**
   * Gets the longitude of the user.
   * @return the longitude of the user.
   */
  public double getLon() {
    return lon;
  }
}
