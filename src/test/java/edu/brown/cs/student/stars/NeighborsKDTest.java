package edu.brown.cs.student.stars;

import edu.brown.cs.student.ProgramRunner;
import edu.brown.cs.student.stars.abstractions.DistanceCalculator;
import edu.brown.cs.student.stars.commands.NeighborsKD;
import edu.brown.cs.student.stars.components.KDTree;
import edu.brown.cs.student.stars.components.StarObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for NeighborsKD.
 */
public class NeighborsKDTest extends DistanceCalculator {

  public NeighborsKD neighbors = new NeighborsKD();

  /**
   * Sets up empty starsList and KDTree.
   */
  @Before
  public void setUpEmpty() {
    List<StarObject> emptyList = new ArrayList<>();
    ProgramRunner.setObjList(emptyList);

    KDTree<StarObject> newKD = new KDTree<>();
    newKD.createTree(ProgramRunner.getObjList(), 3, 0);
    ProgramRunner.setKd(newKD);
  }

  /**
   * Sets up starsList and KDTree with one element.
   */
  @Before
  public void setUpOne() {
    List<StarObject> oneList = new ArrayList<>();
    Double[] star2Coords = {1., 27.7191, 1.};
    StarObject star2 = new StarObject(2, "second",
        new ArrayList<>(Arrays.asList(star2Coords)), 2);
    oneList.add(star2);
    ProgramRunner.setObjList(oneList);

    KDTree<StarObject> newKD = new KDTree<>();
    newKD.createTree(ProgramRunner.getObjList(), 3, 0);
    ProgramRunner.setKd(newKD);
  }

  /**
   * Sets up starsList and KDTree with 7 elts.
   */
  @Before
  public void setUpMulti() {
    Double[] star1Coords = {0., 0., 0.};
    Double[] star2Coords = {1., 27.7191, 1.};
    Double[] star3Coords = {1., 27.7191, 1.};
    Double[] star4Coords = {105., 17., -2.764};
    Double[] star5Coords = {15.752, 12.6823, 1.462};
    Double[] star6Coords = {-0.005, 0.2364391, 23.57};
    Double[] star7Coords = {4., 5., -1.};
    StarObject star1 = new StarObject(1, "first",
        new ArrayList<>(Arrays.asList(star1Coords)), 3);
    StarObject star2 = new StarObject(2, "second",
        new ArrayList<>(Arrays.asList(star2Coords)), 3);
    StarObject star3 = new StarObject(3, "third",
        new ArrayList<>(Arrays.asList(star3Coords)), 3);
    StarObject star4 = new StarObject(4, "fourth",
        new ArrayList<>(Arrays.asList(star4Coords)), 3);
    StarObject star5 = new StarObject(5, "fifth",
        new ArrayList<>(Arrays.asList(star5Coords)), 3);
    StarObject star6 = new StarObject(6, "sixth",
        new ArrayList<>(Arrays.asList(star6Coords)), 3);
    StarObject star7 = new StarObject(7, "",
        new ArrayList<>(Arrays.asList(star7Coords)), 3);

    ProgramRunner.setObjList(new ArrayList<>(
        Arrays.asList(star1, star2, star3, star4, star5, star6, star7)));

    KDTree<StarObject> newKD = new KDTree<>();
    newKD.createTree(ProgramRunner.getObjList(), 3, 0);
    ProgramRunner.setKd(newKD);
  }

  /**
   * Resets the NeighborsKD.
   */
  @After
  public void tearDown() {
    this.neighbors = null;
  }

  /**
   * Tests neighbors on KDTree with 7 elts.
   */
  @Test
  public void testMulti() {

    setUpMulti();

    // ties
    neighbors.execute(new ArrayList<>(Arrays.asList("neighbors", "1", "0", "0", "0")));
    StarObject result1 = neighbors.getNeighborsToPrint().get(0);

    neighbors.execute(new ArrayList<>(Arrays.asList("neighbors", "1", "0", "0", "0")));
    StarObject result2 = neighbors.getNeighborsToPrint().get(0);

    neighbors.execute(new ArrayList<>(Arrays.asList("neighbors", "1", "0", "0", "0")));
    StarObject result3 = neighbors.getNeighborsToPrint().get(0);

    assertEquals(distance(result1.getCoords(), new ArrayList<>(Arrays.asList(0., 0., 0.))),
        distance(result2.getCoords(), new ArrayList<>(Arrays.asList(0., 0., 0.))), 0.0);
    assertEquals(distance(result3.getCoords(), new ArrayList<>(Arrays.asList(0., 0., 0.))),
        distance(result2.getCoords(), new ArrayList<>(Arrays.asList(0., 0., 0.))), 0.0);
    assertEquals(distance(result1.getCoords(), new ArrayList<>(Arrays.asList(0., 0., 0.))),
        distance(result3.getCoords(), new ArrayList<>(Arrays.asList(0., 0., 0.))), 0.0);

    // 0  neighbors
    assertNull(neighbors.execute(new ArrayList<>(Arrays.asList("neighbors", "0", "17", "157.683",
        "-2.3462"))));

    // negative neighbors
    assertEquals(neighbors.execute(new ArrayList<>(Arrays.asList("neighbors", "-1", "17", "157.683",
        "-2.3462"))), "ERROR: Number of neighbors must be non-negative");

    // neighbors >= number stars
    assertEquals(neighbors.execute(new ArrayList<>(Arrays.asList("neighbors", "7", "0", "0", "0"))),
        "1\n7\n5\n6\n2\n3\n4");
    assertEquals(neighbors.execute(new ArrayList<>(Arrays.asList("neighbors", "7", "\"first\""))),
        "7\n5\n6\n2\n3\n4");

    // wrong args
    assertEquals(neighbors.execute(Collections.singletonList("neighbors")),
        "ERROR: syntax is neighbors k x y z or neighbors k \"name\"");
    assertEquals(neighbors.execute(new ArrayList<>(Arrays.asList("neighbors", "1"))),
        "ERROR: syntax is neighbors k x y z or neighbors k \"name\"");
    assertEquals(
        neighbors.execute(new ArrayList<>(Arrays.asList(
            "neighbors", "0", "17", "157.683", "-2.3462", "5472"))),
        "ERROR: syntax is neighbors k x y z or neighbors k \"name\"");

    // name not in quotes
    assertEquals(neighbors.execute(new ArrayList<>(Arrays.asList("neighbors", "7", "first"))),
        "ERROR: Star name must be in quotes");

    // number format exception
    assertEquals(neighbors.execute(
        new ArrayList<>(Arrays.asList("neighbors", "haha", "haha", "haha", "haha"))),
        "ERROR: Number of neighbors and coordinates must be numbers");

    // star not found
    assertEquals(neighbors.execute(new ArrayList<>(Arrays.asList(
        "neighbors", "7", "\"heehee hoohoo\""))), "ERROR: Star not found");

    // search for star w/o name
    assertEquals(neighbors.execute(new ArrayList<>(Arrays.asList("neighbors", "7", "\"\""))),
        "ERROR: Cannot search for star without a name");

    tearDown();

  }

  /**
   * Tests neighbors on KDTree with 1 elt.
   */
  @Test
  public void testOne() {

    setUpOne();

    // coords vs. name
    assertEquals(neighbors.execute(new ArrayList<>(Arrays.asList("neighbors", "1", "1.",
        "27.7191", "1."))),
        "2");
    assertNull(neighbors.execute(new ArrayList<>(Arrays.asList("neighbors", "1", "\"second\""))));

    // 0  neighbors
    assertNull(neighbors.execute(new ArrayList<>(Arrays.asList("neighbors", "0", "17", "157.683",
        "-2.3462"))));

    // negative neighbors
    assertEquals(neighbors.execute(new ArrayList<>(Arrays.asList("neighbors", "-1", "17", "157.683", "-2.3462"))),
        "ERROR: Number of neighbors must be non-negative");

    // neighbors > number stars
    assertEquals(neighbors.execute(new ArrayList<>(Arrays.asList("neighbors", "2", "0", "0", "0"))),
        "2");

    // wrong args
    assertEquals(neighbors.execute(Collections.singletonList("neighbors")),
        "ERROR: syntax is neighbors k x y z or neighbors k \"name\"");
    assertEquals(neighbors.execute(new ArrayList<>(Arrays.asList("neighbors", "1"))),
        "ERROR: syntax is neighbors k x y z or neighbors k \"name\"");
    assertEquals(
        neighbors.execute(new ArrayList<>(Arrays.asList(
            "neighbors", "0", "17", "157.683","-2.3462", "5472"))),
        "ERROR: syntax is neighbors k x y z or neighbors k \"name\"");

    // name not in quotes
    assertEquals(neighbors.execute(new ArrayList<>(Arrays.asList("neighbors", "7", "second"))),
        "ERROR: Star name must be in quotes");

    //number format exception
    assertEquals(neighbors.execute(new ArrayList<>(Arrays.asList("neighbors", "haha", "haha",
        "haha", "haha"))),"ERROR: Number of neighbors and coordinates must be numbers");

    tearDown();

  }

  /**
   * Tests neighbors on empty KDTree.
   */
  @Test
  public void testEmpty() {

    setUpEmpty();

    // star not found
    assertNull(neighbors.execute(new ArrayList<>(Arrays.asList(
        "neighbors", "1", "1.", "27.7191", "1."))));
    assertEquals(neighbors.execute(new ArrayList<>(
        Arrays.asList("neighbors", "1", "\"second\""))), "ERROR: Star not found");

    // 0  neighbors
    assertNull(neighbors.execute(new ArrayList<>(Arrays.asList("neighbors", "0", "17", "157.683",
        "-2.3462"))));

    // negative neighbors
    assertEquals(neighbors.execute(new ArrayList<>(Arrays.asList("neighbors", "-1", "17", "157.683",
        "-2.3462"))), "ERROR: Number of neighbors must be non-negative");

    // neighbors > number stars
    assertNull(neighbors.execute(new ArrayList<>(Arrays.asList("neighbors", "2", "0", "0", "0"))));

    // wrong args
    assertEquals(neighbors.execute(Collections.singletonList("neighbors")),
        "ERROR: syntax is neighbors k x y z or neighbors k \"name\"");
    assertEquals(neighbors.execute(new ArrayList<>(Arrays.asList("neighbors", "1"))),
        "ERROR: syntax is neighbors k x y z or neighbors k \"name\"");
    assertEquals(
        neighbors.execute(new ArrayList<>(Arrays.asList(
            "neighbors", "0", "17", "157.683","-2.3462", "5472"))),
        "ERROR: syntax is neighbors k x y z or neighbors k \"name\"");

    // name not in quotes
    assertEquals(neighbors.execute(new ArrayList<>(Arrays.asList("neighbors", "7", "second"))),
        "ERROR: Star name must be in quotes");

    // number format exception
    assertEquals(neighbors.execute(new ArrayList<>(
        Arrays.asList("neighbors", "haha", "haha", "haha", "haha"))),
        "ERROR: Number of neighbors and coordinates must be numbers");

    tearDown();

  }


}


