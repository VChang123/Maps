package edu.brown.cs.student.maps.abstractions;

/**
 * Class that implements Haversine distance.
 */
public class Haversine {
  //stores the earth's radius
  private static final double EARTHRAD = 6371.0;

  /**
   * Method that computes the Haversine distance.
   * @param lat1 the first latitude
   * @param lon1 the first longitude
   * @param lat2 the second latitude
   * @param lon2 the second longitude
   * @return a double of the distance between the two points.
   */
  public double haversine(double lat1, double lon1, double lat2, double lon2) {
    // distance between latitudes and longitudes
    double xDist = Math.toRadians(lat2 - lat1);
    double yDist = Math.toRadians(lon2 - lon1);

    // convert to radians
    lat1 = Math.toRadians(lat1);
    lat2 = Math.toRadians(lat2);

    // apply haversine formula
    double a = Math.pow(Math.sin(xDist / 2), 2) + Math.pow(Math.sin(yDist / 2),
        2) * Math.cos(lat1) * Math.cos(lat2);
    double c = 2 * Math.asin(Math.sqrt(a));
    return EARTHRAD * c;
  }
}
