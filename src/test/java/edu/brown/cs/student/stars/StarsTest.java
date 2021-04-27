package edu.brown.cs.student.stars;

import edu.brown.cs.student.stars.abstractions.ICoordinate;
import edu.brown.cs.student.stars.commands.Stars;
import edu.brown.cs.student.stars.components.StarObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * StarsMethodTest class.
 */
public class StarsTest<T extends ICoordinate> {
  private Stars starsMethod = new Stars();
  private static List<StarObject> starsList;
  List<String> args;

  /**
   * sets up arguments array.
   */
  @Before
  public void setUp() {
    this.args = new ArrayList<>();
  }

  /**
   * resets all fieds.
   */
  @After
  public void tearDown() {
    starsMethod = null;
    starsList = null;
    args = null;
  }

  /**
   * tests execute method.
   */
  @Test
  public void testExecute() {
    setUp();

    // test that starsList is cleared on a new iteration of stars & csv w one star, multiple stars
    args.add("stars");
    args.add("data/stars/one-star.csv");
    assertEquals(starsMethod.execute(args), "Read 1 stars from data/stars/one-star.csv");

    args.clear();
    args.add("stars");
    args.add("data/stars/stardata.csv");
    assertEquals(starsMethod.execute(args), "Read 119617 stars from data/stars/stardata.csv");

    // tests empty csv
    args.clear();
    args.add("stars");
    args.add("data/stars/empty.csv");
    assertEquals(starsMethod.execute(args), "");

    // tests csv with invalid rows & parsing errors
    args.clear();
    args.add("stars");
    args.add("data/stars/blank-stars.csv");
    assertEquals(starsMethod.execute(args),
        "ERROR: ID and coordinates must be numbers\n" +
        "ERROR: ID and coordinates must be numbers\n" +
        "Read 1 stars from data/stars/blank-stars.csv");

    // tests file that doesn't exist
    args.clear();
    args.add("stars");
    args.add("data/stars/haha.csv");
    assertEquals(starsMethod.execute(args), "ERROR: File does not exist");

    // tests no file
    args.clear();
    args.add("stars");
    assertEquals(starsMethod.execute(args), "ERROR: syntax is stars [filepath]");

    tearDown();
  }
}