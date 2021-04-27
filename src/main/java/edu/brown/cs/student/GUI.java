package edu.brown.cs.student;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import edu.brown.cs.student.maps.commands.Nearest;
import edu.brown.cs.student.maps.commands.RouteCommand;
import edu.brown.cs.student.maps.commands.Ways;
import edu.brown.cs.student.maps.components.Node;
import edu.brown.cs.student.maps.components.UserCheckin;
import edu.brown.cs.student.maps.components.Way;
import freemarker.template.Configuration;
import org.json.JSONObject;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class handles all the backend handlers.
 */
public class GUI {
  private static final Gson GSON = new Gson();

  /**
   * Constructor for this class.
   * @param port the port to build the gui on.
   */
  public GUI(int port) {
    this.mapSparkServer(port);
  }
  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
              templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  /**
   * Spark server for the map GUI.
   * @param port, takes in a port.
   */
  private void mapSparkServer(int port) {

    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");

    Spark.options("/*", (request, response) -> {
      String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
      if (accessControlRequestHeaders != null) {
        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
      }

      String accessControlRequestMethod = request.headers("Access-Control-Request-Method");

      if (accessControlRequestMethod != null) {
        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
      }

      return "OK";
    });

    Spark.before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
    Spark.exception(Exception.class, new ExceptionPrinter());
    Spark.post("/maps", new RouteHandler());
    Spark.post("/ways", new WaysHandler());
    Spark.post("/nearest", new NearestHandler());
    Spark.post("/intersection", new IntersectionsHandler());
    Spark.post("/userdata", new ServerHandler());
    Spark.post("/singleuser", new UserDataHandler());
  }

  /**
   * The Routehandler post request.
   */
  private static class RouteHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String sLat = data.getString("srclat");
      String sLon = data.getString("srclong");
      String dLat = data.getString("destlat");
      String dLon = data.getString("destlong");
      List<String> args = new ArrayList<>();
      List<List<String>> result = new ArrayList<>();
      args.add("route");
      args.add(sLat);
      args.add(sLon);
      args.add(dLat);
      args.add(dLon);
      RouteCommand route = new RouteCommand();
      route.execute(args);
      List<Way> output = route.getFinalPathway();
      if (output.size() != 0) {
        for (Way e : output) {
          List<String> info = new ArrayList<>();
          info.add(e.getId());
          info.add(Double.toString(e.from().getLatitude()));
          info.add(Double.toString(e.from().getLongitude()));
          info.add(Double.toString(e.to().getLatitude()));
          info.add(Double.toString(e.to().getLongitude()));
          info.add(e.getClassify());
          result.add(info);
        }
      }
      Map<String, Object> variables = ImmutableMap.of("maps", result);
      return GSON.toJson(variables);
    }
  }

  /**
   * The Way Handler post request.
   */
  private static class WaysHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      double sLat = data.getDouble("srclat");
      double sLon = data.getDouble("srclong");
      double dLat = data.getDouble("destlat");
      double dLon = data.getDouble("destlong");
      List<String> args = new ArrayList<>();
      Map<String, List<String>> result = new HashMap<>();
      args.add("ways");
      args.add(Double.toString(sLat));
      args.add((Double.toString(sLon)));
      args.add(Double.toString(dLat));
      args.add(Double.toString(dLon));
      Ways ways = new Ways();
      ways.execute(args);
      List<Way> output = ways.getListofWays();
      if (output.size() != 0) {
        for (Way e : output) {
          List<String> info = new ArrayList<>();
          info.add(Double.toString(e.from().getLatitude()));
          info.add(Double.toString(e.from().getLongitude()));
          info.add(Double.toString(e.to().getLatitude()));
          info.add(Double.toString(e.to().getLongitude()));
          info.add(e.getClassify());
          result.put(e.getId(), info);
        }
      }
      Map<String, Object> variables = ImmutableMap.of("ways", result);
      return GSON.toJson(variables);
    }
  }

  /**
   * The Nearest Handler Post request.
   */
  private static class NearestHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      String sLat = data.getString("srclat");
      String sLon = data.getString("srclong");
      List<String> args = new ArrayList<>();
      args.add("nearest");
      args.add(sLat);
      args.add(sLon);
      Nearest near = new Nearest();
      near.execute(args);
      Node output = near.getNearestNode();
      List<String> result = new ArrayList<>();
      if (output != null) {
        result.add(Double.toString(output.getLatitude()));
        result.add(Double.toString(output.getLongitude()));
      }
      Map<String, Object> variables = ImmutableMap.of("nearest", result);
      return GSON.toJson(variables);
    }
  }

  /**
   * Intersection Handler post request.
   */
  private static class IntersectionsHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      List<String> result = new ArrayList<>();
      String street1 = data.getString("st1");
      String street2 = data.getString("st2");
      List<Node> output = ProgramRunner.getDb().getIntersection(street1, street2);
      if (output.size() != 0) {
        result.add(Double.toString(output.get(0).getLatitude()));
        result.add(Double.toString(output.get(0).getLongitude()));
      }
      Map<String, Object> variables = ImmutableMap.of("intersection", result);
      return GSON.toJson(variables);
    }
  }

  /**
   * Handles the server updates from the backend.
   */
  private static class ServerHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
      List<Map<String, String>> output = new ArrayList<>();
      Map<Double, UserCheckin> userData = ProgramRunner.getThread().getLatestCheckins();
      List<Double> timestamp = new ArrayList<>(userData.keySet());
      timestamp.sort(Collections.reverseOrder());
      if (timestamp.size() > 0) {
        for (Double time :  timestamp) {
          Map<String, String> data = new HashMap<>();
          UserCheckin user = userData.get(time);
          String id = Double.toString(user.getId());
          String name =  user.getName();
          String time2 = Double.toString(user.getTimestamp());
          String lat = Double.toString(user.getLat());
          String lon = Double.toString(user.getLon());
          data.put("id", id);
          data.put("name", name);
          data.put("timestamp", time2);
          data.put("latitude", lat);
          data.put("longitude", lon);
          output.add(data);
        }
      }

      Map<String, Object> variables = ImmutableMap.of("userdata", output);
      return GSON.toJson(variables);
    }
  }

  /**
   * Gets all the data for a single user.
   */
  private static class UserDataHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
      List<List<String>> result = new ArrayList<>();
      Integer id = data.getInt("id");
      List<UserCheckin> userdata = ProgramRunner.getDb().getAllUserData(id);
      for (UserCheckin user : userdata) {
        List<String> info = new ArrayList<>();
        String id1 = Integer.toString(user.getId());
        String name = user.getName();
        String time = Double.toString(user.getTimestamp());
        String lat = Double.toString(user.getLat());
        String lon = Double.toString(user.getLon());
        info.add(id1);
        info.add(name);
        info.add(time);
        info.add(lat);
        info.add(lon);
        result.add(info);
      }
      Map<String, Object> variables = ImmutableMap.of("singleuser", result);
      return GSON.toJson(variables);
    }
  }
  /**
   * Handle requests to the front page of our Stars website.
   *
   */
  private static class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
              "Stars: Query the database", "results", "");
      return new ModelAndView(variables, "query.ftl");
    }
  }



  /**
   * Display an error page when an exception occurs in the server.
   *
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

}
