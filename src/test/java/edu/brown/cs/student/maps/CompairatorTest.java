package edu.brown.cs.student.maps;

import edu.brown.cs.student.maps.abstractions.Compairator;
import edu.brown.cs.student.maps.abstractions.Pair;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Compairator test class
 */
public class CompairatorTest {

  /**
   * Tests the compairator in Integers and Doubles
    */
  @Test
  public void compareTestInteger() {
    Compairator<Integer> comp = new Compairator<>();
    Pair<Integer, Double> p0 = new Pair<>(1, 0.);
    Pair<Integer, Double> p1 = new Pair<>(1, 0.);
    Pair<Integer, Double> p2 = new Pair<>(2, 0.);
    Pair<Integer, Double> p3 = new Pair<>(1, 1.);
    Pair<Integer, Double> p4 = new Pair<>(1, -1.);

    assertEquals(0, comp.compare(p0, p1));
    assertEquals(0, comp.compare(p0, p2));
    assertEquals(1, comp.compare(p3, p1));
    assertEquals(-1, comp.compare(p4, p1));
  }

  /**
   * Compares on Strings and Doubles
   */
  @Test
  public void compareTestString() {
    Compairator<String> comp = new Compairator<>();
    Pair<String, Double> p0 = new Pair<>("asdf", 0.);
    Pair<String, Double> p1 = new Pair<>("asdf", 0.);
    Pair<String, Double> p2 = new Pair<>("jkl;", 0.);
    Pair<String, Double> p3 = new Pair<>("asdf", 1.);
    Pair<String, Double> p4 = new Pair<>("asdf", -1.);

    assertEquals(0, comp.compare(p0, p1));
    assertEquals(0, comp.compare(p0, p2));
    assertEquals(1, comp.compare(p3, p1));
    assertEquals(-1, comp.compare(p4, p1));
  }
}
