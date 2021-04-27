package edu.brown.cs.student.stars;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;

import com.google.gson.Gson;
import edu.brown.cs.student.GUI;
import edu.brown.cs.student.ProgramRunner;
//import edu.brown.cs.student.maps.commands.Nearest;
//import edu.brown.cs.student.maps.commands.RouteCommand;
//import edu.brown.cs.student.maps.commands.Ways;
//import edu.brown.cs.student.maps.components.Node;
//import edu.brown.cs.student.maps.components.UserCheckin;
//import edu.brown.cs.student.maps.components.Way;
//import edu.brown.cs.student.stars.commands.NeighborsKD;
//import edu.brown.cs.student.stars.commands.NeighborsNaive;
//import edu.brown.cs.student.stars.commands.RadiusKD;
//import edu.brown.cs.student.stars.commands.RadiusNaive;
//import edu.brown.cs.student.stars.components.StarObject;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
//import spark.ExceptionHandler;
//import spark.ModelAndView;
//import spark.QueryParamsMap;
//import spark.Request;
//import spark.Response;
//import spark.Route;
//import spark.Spark;
//import spark.TemplateViewRoute;
//import spark.template.freemarker.FreeMarkerEngine;
//import org.json.JSONObject;
//
//import com.google.common.collect.ImmutableMap;
//
//import freemarker.template.Configuration;

/**
 * The Main class of our project. This is where execution begins.
 *
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;
  private static final Gson GSON = new Gson();
  /**
   * The initial method called when execution begins.
   * @param args
   *          An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
    .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      //mapSparkServer((int) options.valueOf("port"));
      GUI visual = new GUI((int) options.valueOf("port"));
    }
    ProgramRunner s = new ProgramRunner();
    s.run();
  }
}
