package edu.brown.cs.student.maps;

import edu.brown.cs.student.maps.abstractions.Pair;
import edu.brown.cs.student.maps.components.Node;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests Equals and Hashcode for Node and Pair class
 */
public class EqualsHashcodeTest {
  /**
   * Tests the Node class hashcode and equals
   */
  @Test
  public void nodeTest() {
    Node n0 = new Node("0", 15., 15.);
    Node n1 = new Node("0", 15., 15.);
    Node n2 = new Node("0", 17., 15.);
    Node n3 = new Node("1", 12., 17.);
    Node n4 = new Node("2", 12., 17.);

    assertEquals(n0, n1);
    // same id, diff coords
    assertNotEquals(n0, n2);
    // diff id, same coords
    assertNotEquals(n3, n4);

    assertEquals(n0.hashCode(), n1.hashCode());
    assertNotEquals(n0.hashCode(), n2.hashCode());
    assertNotEquals(n3.hashCode(), n4.hashCode());

  }

  /**
   * Tests the pair class equals and hashcode
   */
  @Test
  public void pairTest() {
    Pair<Integer, Integer> p0 = new Pair<>(1, 1);
    Pair<Integer, Integer> p1 = new Pair<>(1, 1);
    Pair<Integer, Integer> p2 = new Pair<>(2, 2);
    Pair<String, Integer> p3 = new Pair<>("assdf", 2);
    Pair<String, Integer> p4 = new Pair<>("assdf", 2);
    Pair<String, Integer> p5 = new Pair<>("assdf", 7);

    // same
    assertEquals(p0, p1);
    // same
    assertEquals(p3, p4);
    // not same
    assertNotEquals(p0, p2);
    // different parameters
    assertNotEquals(p0, p3);
    // not same
    assertNotEquals(p4, p5);

    assertEquals(p0.hashCode(), p1.hashCode());
    assertEquals(p3.hashCode(), p4.hashCode());
    assertNotEquals(p0.hashCode(), p2.hashCode());
    assertNotEquals(p0.hashCode(), p3.hashCode());
    assertNotEquals(p4.hashCode(), p5.hashCode());

  }
}
