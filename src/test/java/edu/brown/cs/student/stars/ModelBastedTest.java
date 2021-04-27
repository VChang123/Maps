package edu.brown.cs.student.stars;

import java.util.List;

import edu.brown.cs.student.ProgramRunner;
import edu.brown.cs.student.stars.abstractions.DistanceCalculator;
import edu.brown.cs.student.stars.abstractions.ICoordinate;
import edu.brown.cs.student.stars.commands.NeighborsKD;
import edu.brown.cs.student.stars.commands.NeighborsNaive;
import edu.brown.cs.student.stars.commands.RadiusKD;
import edu.brown.cs.student.stars.commands.RadiusNaive;
import edu.brown.cs.student.stars.commands.Stars;
import edu.brown.cs.student.stars.components.StarObject;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;

/**
 * ModelBasedTest class.
 * @param <T>
 */
public class ModelBastedTest<T extends ICoordinate> extends DistanceCalculator {

  /**
   * fields for ModelBasedTest.
   */
  private Stars starsMethod;
  private NeighborsNaive neighborsMethodNaive;
  private NeighborsKD neighborsMethodKD;
  private RadiusNaive radiusMethodNaive;
  private RadiusKD radiusMethodKD;
  private static List<StarObject> starsList;
  List<String> argsNaive;
  List<String> argsKD;


  /**
   * Sets up naive and kd radius methods as well as starslist and corresponding arguments.
   */
  @Before
  public void setUpRadius() {
    starsMethod = new Stars();
    radiusMethodNaive = new RadiusNaive();
    radiusMethodKD = new RadiusKD();
    argsNaive = new ArrayList<>();
    argsKD = new ArrayList<>();
    this.argsNaive.add("naive_radius");
    this.argsKD.add("radius");
    readStars();
    starsList = ProgramRunner.getObjList();
  }

  /**
   * Sets up naive and kd neighbors methods as well as starslist and corresponding arguments.
   */
  @Before
  public void setUpNeighbors() {
    starsMethod = new Stars();
    neighborsMethodNaive = new NeighborsNaive();
    neighborsMethodKD = new NeighborsKD();
    argsNaive = new ArrayList<>();
    argsKD = new ArrayList<>();
    this.argsNaive.add("naive_neighbors");
    this.argsKD.add("neighbors");
    readStars();
    starsList = ProgramRunner.getObjList();
  }

  /**
   * Resets all of the fields.
   */
  @After
  public void tearDown() {
    starsMethod = null;
    neighborsMethodNaive = null;
    neighborsMethodKD = null;
    radiusMethodNaive = null;
    radiusMethodKD = null;
    starsList = null;
    argsNaive = null;
    argsKD = null;
  }

  /**
   * Adds input double to args.
   * @param num - a double to put into args.
   */
  public void addToArgs(double num) {
    String d = Double.toString(num);
    argsNaive.add(d);
    argsKD.add(d);
  }

  /**
   * Adds input integer to args.
   * @param num - an integer to put into args.
   */
  public void addToArgs(int num) {
    String d = Integer.toString(num);
    argsNaive.add(d);
    argsKD.add(d);
  }

  /**
   * randomly picks a file to read from and calls the stars method to fill the starsList.
   */
  public void readStars() {
    List<String> args = new ArrayList<>();
    List<String> paths = new ArrayList<>();
    paths.add("data/stars/stardata.csv");
    paths.add("data/stars/empty.csv");
    paths.add("data/stars/one-star.csv");
    paths.add("data/stars/ten-star.csv");
    paths.add("data/stars/three-star.csv");
    paths.add("data/stars/tie-star.csv");
    paths.add("data/stars/two-stars.csv");

    args.add("stars");
    int idx = ThreadLocalRandom.current().nextInt(0, paths.size());
    args.add(paths.get(idx));
    starsMethod.execute(args);
  }

  /**
   * checks if the results of the naive and kd radius commands are valid, meaning they are the same
   * size and no element in either list has a larger distance than the radius.
   * @param naive - result of naive radius search
   * @param kd - result of kd radius search
   * @param x - target x coordinate
   * @param y - target y coordinate
   * @param z - target z coordinate
   * @param r - specified radius
   * @return - true if the output lists meet these criteria; false otherwise
   */
  public boolean isValidRadius(List<StarObject> naive, List<StarObject> kd, Double x, Double y,
                               Double z,
                               int r) {
    if (naive == kd) {
      return true;
    }

    if (naive.size() != kd.size()) {
      return false;
    } else {
      if (naive.size() == 0) {
        return true;
      }
    }

    List<Double> coords = new ArrayList<>();
    coords.add(x);
    coords.add(y);
    coords.add(z);

    for (int i = 0; i < naive.size(); i++) {
      double naiveDist = distance(coords, naive.get(i).getCoords());
      double kdDist = distance(coords, kd.get(i).getCoords());
      if (naiveDist > r || kdDist > r) {
        return false;
      }
    }
    return true;

  }

  /**
   * checks if the results of the naive and kd neighbors commands are valid, meaning they are the
   * same size, their last elements have the same distance, and no element in either list has a larger
   * distance than the radius.
   * @param naive - result of naive neighbor search
   * @param kd - result of kd neighbor search
   * @param x - target x coordinate
   * @param y - target y coordinate
   * @param z - target z coordinate
   * @return - true if the output lists meet these criteria; false otherwise
   */
  public boolean isValidNeighbor(List<StarObject> naive, List<StarObject> kd, Double x, Double y,
                                 Double z) {
    if (naive == kd) {
      return true;
    }

    if (naive.size() != kd.size()) {
      return false;
    } else {
      if (naive.size() == 0) {
        return true;
      }
    }

    StarObject lastNaive = naive.get(naive.size() - 1);
    StarObject lastKD = kd.get(naive.size() - 1);


    List<Double> coords = new ArrayList<>();
    coords.add(x);
    coords.add(y);
    coords.add(z);

    double lastNaiveDist = distance(coords, lastNaive.getCoords());
    double lastKDDist = distance(coords, lastKD.getCoords());

    if (lastNaiveDist != lastKDDist) {
      return false;
    }

    double farthestDist = distance(coords, lastNaive.getCoords());

    for (int i = 0; i < naive.size(); i++) {
      double naiveDist = distance(coords, naive.get(i).getCoords());
      double kdDist = distance(coords, kd.get(i).getCoords());
      if (naiveDist > farthestDist || kdDist > farthestDist) {
        return false;
      }
    }
    return true;

  }

  /**
   * checks if every object in input list is in starsList
   * @param lst - list that contains stars that need to be checked
   * @return true if every star is in starsList; false otherwise
   */
  public boolean inStarsList(List<StarObject> lst) {
    for (StarObject s : lst) {
      if (!starsList.contains(s)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Generates random inputs and compares the outputs of the naive and kd radius searches.
   * Checks if results lists are valid and also checks to see that every elt in the result
   * lists are in starsList.
   */
  @Test
  public void testRadius() {

    for (int i = 0; i < 25; i++) {
      setUpRadius();

      int randomR = new Random().nextInt(50);
      double randomX = Math.random() * 100;
      double randomY = Math.random() * 100;
      double randomZ = Math.random() * 100;
      addToArgs(randomR);
      addToArgs(randomX);
      addToArgs(randomY);
      addToArgs(randomZ);

      radiusMethodNaive.execute(argsNaive);
      radiusMethodKD.execute(argsKD);

      List<StarObject> naive = radiusMethodNaive.getNeighborsToPrint();
      List<StarObject> kd = radiusMethodKD.getNeighborsToPrint();

      assertTrue(inStarsList(naive));
      assertTrue(inStarsList(kd));

      assertTrue(isValidRadius(naive, kd, randomX, randomY, randomZ, randomR));

      tearDown();
    }

  }

  /**
   * Generates random inputs and compares the outputs of the naive and kd neighbor searches.
   * Checks if results lists are valid and also checks to see that every elt in the result
   * lists are in starsList.
   */
  @Test
  public void testNeighbors() {

    for (int i = 0; i < 25; i++) {
      setUpNeighbors();

      int randomK = new Random().nextInt(100);
      double randomX = Math.random() * 100;
      double randomY = Math.random() * 100;
      double randomZ = Math.random() * 100;
      addToArgs(randomK);
      addToArgs(randomX);
      addToArgs(randomY);
      addToArgs(randomZ);

      neighborsMethodNaive.execute(argsNaive);
      neighborsMethodKD.execute(argsKD);

      List<StarObject> naive = neighborsMethodNaive.getNeighborsToPrint();
      List<StarObject> kd = neighborsMethodKD.getNeighborsToPrint();

      assertTrue(inStarsList(naive));
      assertTrue(inStarsList(kd));

      assertTrue(isValidNeighbor(naive, kd, randomX, randomY, randomZ));

      tearDown();
    }

  }

}
