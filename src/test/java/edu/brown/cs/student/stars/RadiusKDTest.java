package edu.brown.cs.student.stars;

import edu.brown.cs.student.ProgramRunner;
import edu.brown.cs.student.stars.commands.RadiusKD;
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
 * Test class for RadiusKD.
 */
public class RadiusKDTest {
  public RadiusKD radius = new RadiusKD();

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
    StarObject star2 = new StarObject(2, "second", new ArrayList<>(Arrays.asList(star2Coords)), 2);
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

    ProgramRunner.setObjList(new ArrayList<>(Arrays.asList(star1, star2, star3, star4, star5,
        star6, star7)));

    KDTree<StarObject> newKD = new KDTree<>();
    newKD.createTree(ProgramRunner.getObjList(), 3, 0);
    ProgramRunner.setKd(newKD);
  }

  /**
   * Resets the RadiusKD.
   */
  @After
  public void tearDown() {
    this.radius = null;
  }

  /**
   * Tests neighbors on KDTree with 7 elts.
   */
  @Test
  public void testMulti() {

    setUpMulti();

    // 0  radius
    assertEquals(radius.execute(new ArrayList<>(Arrays.asList("radius", "0", "1", "27.7191", "1"))),
        "2\n3");
    assertEquals(radius.execute(new ArrayList<>(Arrays.asList("radius", "0", "\"second\""))), "3");

    // double radius
    String testRadius = String.valueOf(Math.sqrt(2. + Math.pow(27.7191, 2)));
    assertEquals(radius.execute(new ArrayList<>(Arrays.asList("radius", testRadius,
        "1", "27.7191", "1"))), "2\n3\n5\n7\n1");
    assertEquals(radius.execute(new ArrayList<>(Arrays.asList("radius", testRadius, "\"second\""))),
        "3\n5\n7\n1");

    // negative radius
    assertEquals(radius.execute(new ArrayList<>(Arrays.asList("radius", "-1", "17", "157.683",
        "-2.3462"))), "ERROR: Radius must be non-negative");

    // radius >= farthest star
    assertEquals(radius.execute(new ArrayList<>(Arrays.asList("radius", "200", "0", "0", "0"))),
        "1\n7\n5\n6\n2\n3\n4");
    assertEquals(radius.execute(new ArrayList<>(Arrays.asList("radius", "200", "\"first\""))),
        "7\n5\n6\n2\n3\n4");

    // wrong args
    assertEquals(radius.execute(Collections.singletonList("radius")),
        "ERROR: syntax is radius k x y z or radius k \"name\"");
    assertEquals(radius.execute(new ArrayList<>(Arrays.asList("radius", "1"))),
        "ERROR: syntax is radius k x y z or radius k \"name\"");
    assertEquals(
        radius.execute(new ArrayList<>(Arrays.asList(
            "radius", "0", "17", "157.683", "-2.3462", "5472"))),
        "ERROR: syntax is radius k x y z or radius k \"name\"");

    // name not in quotes
    assertEquals(radius.execute(new ArrayList<>(Arrays.asList("radius", "7", "first"))),
        "ERROR: Star name must be in quotes");

    // number format exception
    assertEquals(radius.execute(new ArrayList<>(Arrays.asList(
        "radius", "haha", "haha", "haha", "haha"))),
        "ERROR: Radius and coordinates must be numbers");

    // star not found
    assertEquals(radius.execute(new ArrayList<>(Arrays.asList("radius", "7", "\"heehee hoohoo\""))),
        "ERROR: Star not found");

    // search for star w/o name
    assertEquals(radius.execute(new ArrayList<>(Arrays.asList("radius", "7", "\"\""))),
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
    assertEquals(radius.execute(new ArrayList<>(Arrays.asList(
        "radius", "28", "1.", "27.7191", "1."))), "2");
    assertNull(radius.execute(new ArrayList<>(Arrays.asList("radius", "1", "\"second\""))));

    // 0  radius
    assertNull(radius.execute(new ArrayList<>(Arrays.asList(
        "radius", "0", "17", "157.683", "-2.3462"))));

    // negative radius
    assertEquals(radius.execute(new ArrayList<>(Arrays.asList(
        "radius", "-1", "17", "157.683", "-2.3462"))),
        "ERROR: Radius must be non-negative");

    // radius > furthest star
    assertEquals(radius.execute(new ArrayList<>(Arrays.asList(
        "radius", "100", "1.", "27.7191","1."))),
        "2");
    assertNull(radius.execute(new ArrayList<>(Arrays.asList("radius", "100", "\"second\""))));

    // wrong args
    assertEquals(radius.execute(Collections.singletonList("radius")),
        "ERROR: syntax is radius k x y z or radius k \"name\"");
    assertEquals(radius.execute(new ArrayList<>(Arrays.asList("radius", "1"))),
        "ERROR: syntax is radius k x y z or radius k \"name\"");
    assertEquals(
        radius.execute(new ArrayList<>(Arrays.asList(
            "radius", "0", "17", "157.683", "-2.3462", "5472"))),
        "ERROR: syntax is radius k x y z or radius k \"name\"");

    // name not in quotes
    assertEquals(radius.execute(new ArrayList<>(Arrays.asList("radius", "7", "second"))),
        "ERROR: Star name must be in quotes");

    //number format exception
    assertEquals(radius.execute(new ArrayList<>(
        Arrays.asList("radius", "haha", "haha", "haha", "haha"))),
        "ERROR: Radius and coordinates must be numbers");

    tearDown();

  }

  /**
   * Tests neighbors on empty KDTree.
   */
  @Test
  public void testEmpty() {

    setUpEmpty();

    // star not found
    assertNull(radius.execute(new ArrayList<>(
        Arrays.asList("radius", "1", "1.", "27.7191", "1."))));
    assertEquals(radius.execute(new ArrayList<>(Arrays.asList("radius", "1", "\"second\""))),
        "ERROR: Star not found");

    // 0  radius
    assertNull(radius.execute(new ArrayList<>(
        Arrays.asList("radius", "0", "17", "157.683", "-2.3462"))));

    // negative radius
    assertEquals(radius.execute(new ArrayList<>(
        Arrays.asList("radius", "-1", "17", "157.683", "-2.3462"))),
        "ERROR: Radius must be non-negative");

    // wrong args
    assertEquals(radius.execute(Collections.singletonList("radius")),
        "ERROR: syntax is radius k x y z or radius k \"name\"");
    assertEquals(radius.execute(new ArrayList<>(
        Arrays.asList("radius", "1"))),
        "ERROR: syntax is radius k x y z or radius k \"name\"");
    assertEquals(
        radius.execute(new ArrayList<>(
            Arrays.asList("radius", "0", "17", "157.683", "-2.3462", "5472"))),
        "ERROR: syntax is radius k x y z or radius k \"name\"");

    // name not in quotes
    assertEquals(radius.execute(new ArrayList<>(
        Arrays.asList("radius", "7", "second"))),
        "ERROR: Star name must be in quotes");

    //number format exception
    assertEquals(radius.execute(new ArrayList<>(
        Arrays.asList("radius", "haha", "haha", "haha", "haha"))),
        "ERROR: Radius and coordinates must be numbers");

    tearDown();

  }

}
