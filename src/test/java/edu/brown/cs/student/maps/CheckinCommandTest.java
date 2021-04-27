package edu.brown.cs.student.maps;

import edu.brown.cs.student.ProgramRunner;
import edu.brown.cs.student.maps.commands.MapCommand;
import edu.brown.cs.student.maps.commands.StartCheckin;
import edu.brown.cs.student.maps.components.Database;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CheckinCommandTest {
  private MapCommand mapComm = new MapCommand();
  public CheckinCommandTest(){

  }
  /**
   * Sets up the database in the beginning
   */
  @Before
  public void setUp(){
    ProgramRunner.setDb(new Database());
  }

  @Test
  public void noDBTest() {
    List<String> input = new ArrayList<>();
    input.add("checkin");
    StartCheckin thread = new StartCheckin();
    String output = thread.execute(input);
    assertEquals(output, "ERROR: No database loaded in");
  }
}
