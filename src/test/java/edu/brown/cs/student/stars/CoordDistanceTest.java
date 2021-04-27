package edu.brown.cs.student.stars;

import edu.brown.cs.student.stars.abstractions.CoordDistanceComparator;
import edu.brown.cs.student.stars.components.StarObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test class for CoordDistanceComparator.
 */
public class CoordDistanceTest {

  CoordDistanceComparator comp;

  /**
   * Sets up a CoordDistanceComparator for 3D coordinates.
   */
  @Before
  public void setUp3D() {
    List<Double> coords = new ArrayList<>();
    coords.add(0.);
    coords.add(0.);
    coords.add(0.);
    this.comp = new CoordDistanceComparator(coords);
  }

  /**
   * Sets up a CoordDistanceComparator for 2D coordinates.
   */
  @Before
  public void setUp2D() {
    List<Double> coords = new ArrayList<>();
    coords.add(0.);
    coords.add(0.);
    this.comp = new CoordDistanceComparator(coords);

  }

  /**
   * Resets the CoordDistanceComparator.
   */
  @After
  public void tearDown() {
    this.comp = null;
  }

  /**
   * Tests compare function of 3D CoordDistanceComparator.
   */
  @Test
  public void test3D() {
    setUp3D();
    Double[] star1Coords = {1., -3., 5.};
    Double[] star2Coords = {1., 27.7191, 1.};
    Double[] star3Coords = {1., 27.7191, 1.};
    StarObject star1 = new StarObject(1, "first",
        new ArrayList<>(Arrays.asList(star1Coords)), 3);
    StarObject star2 = new StarObject(2, "second",
        new ArrayList<>(Arrays.asList(star2Coords)), 3);
    StarObject star3 = new StarObject(3, "third",
        new ArrayList<>(Arrays.asList(star3Coords)), 3);

    assertEquals(this.comp.compare(star1, star2), -1);
    assertEquals(this.comp.compare(star1, star3), -1);
    assertEquals(this.comp.compare(star2, star3), 0);
    assertEquals(this.comp.compare(star2, star1), 1);
    assertEquals(this.comp.compare(star3, star1), 1);

    tearDown();

  }

  /**
   * Tests compare function of 2D CoordDistanceComparator.
   */
  @Test
  public void test2D() {
    setUp2D();

    Double[] star1Coords = {1., -3.};
    Double[] star2Coords = {1., 27.7191};
    Double[] star3Coords = {1., 27.7191};
    StarObject star1 = new StarObject(1, "first",
        new ArrayList<>(Arrays.asList(star1Coords)), 2);
    StarObject star2 = new StarObject(2, "second",
        new ArrayList<>(Arrays.asList(star2Coords)), 2);
    StarObject star3 = new StarObject(3, "third",
        new ArrayList<>(Arrays.asList(star3Coords)), 2);

    assertEquals(this.comp.compare(star1, star2), -1);
    assertEquals(this.comp.compare(star1, star3), -1);
    assertEquals(this.comp.compare(star2, star3), 0);
    assertEquals(this.comp.compare(star2, star1), 1);
    assertEquals(this.comp.compare(star3, star1), 1);

    tearDown();
  }
}
