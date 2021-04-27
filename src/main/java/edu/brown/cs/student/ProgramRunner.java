package edu.brown.cs.student;

import edu.brown.cs.student.maps.commands.MapCommand;
import edu.brown.cs.student.maps.commands.Nearest;
import edu.brown.cs.student.maps.commands.RouteCommand;
import edu.brown.cs.student.maps.commands.StartCheckin;
import edu.brown.cs.student.maps.commands.Ways;
import edu.brown.cs.student.maps.components.CheckinThread;
import edu.brown.cs.student.maps.components.Node;
import edu.brown.cs.student.maps.components.Database;
import edu.brown.cs.student.stars.components.KDTree;
import edu.brown.cs.student.stars.commands.NeighborsKD;
import edu.brown.cs.student.stars.commands.NeighborsNaive;
import edu.brown.cs.student.stars.commands.RadiusKD;
import edu.brown.cs.student.stars.commands.RadiusNaive;
import edu.brown.cs.student.stars.components.StarObject;
import edu.brown.cs.student.stars.commands.Stars;


import java.util.List;

/**
 * ProgramRunner class.
 */
public class ProgramRunner {
  /**
   * ProgramRunner fields.
   */
  private static List<StarObject> objList = null;
  private static KDTree<StarObject> kd = null;
  private static Database db;
  private REPL repl;
  private static KDTree<Node> dataKD = new KDTree<>();
  private static CheckinThread thread  = new CheckinThread();

  /**
   * ProgramRunner constructor.
   */
  public ProgramRunner() {
    this.repl = new REPL();
    db = new Database();
    this.repl.registerCommand("stars", new Stars());
    this.repl.registerCommand("neighbors", new NeighborsKD());
    this.repl.registerCommand("radius", new RadiusKD());
    this.repl.registerCommand("naive_radius", new RadiusNaive());
    this.repl.registerCommand("naive_neighbors", new NeighborsNaive());
    this.repl.registerCommand("map", new MapCommand());
    this.repl.registerCommand("nearest", new Nearest());
    this.repl.registerCommand("route", new RouteCommand());
    this.repl.registerCommand("ways", new Ways());
    this.repl.registerCommand("checkin", new StartCheckin());
  }

  /**
   * runs entire program.
   */
  public void run() {
    this.repl.startRepl();
  }

  /**
   * getter for objList.
   * @return - objList.
   */
  public static List<StarObject> getObjList() {
    return objList;
  }

  /**
   * setter for objList.
   * @param objList - an updated version of objList to set to.
   */
  public static void setObjList(List<StarObject> objList) {
    ProgramRunner.objList = objList;
  }

  /**
   * getter for kd.
   * @return - kd.
   */
  public static KDTree<StarObject> getKd() {
    return kd;
  }

  /**
   * setter for kd.
   * @param kd - an updated version of kd to set to.
   */
  public static void setKd(KDTree<StarObject> kd) {
    ProgramRunner.kd = kd;
  }

  /**
   * Gets the Database.
   * @return the current database.
   */
  public static Database getDb() {
    return db;
  }

  /**
   * Sets the database to the one passed in.
   * @param db the database to be passed in
   */
  public static void setDb(Database db) {
    ProgramRunner.db = db;
  }

  /**
   * Gets the kd tree for maps.
   * @return a kd tree
   */
  public static KDTree<Node> getDataKD() {
    return dataKD;
  }

  /**
   * Sets the kdtree for maps.
   * @param dataKD the kdtree to be set to.
   */
  public static void setDataKD(KDTree<Node> dataKD) {
    ProgramRunner.dataKD = dataKD;
  }

  /**
   * Gets the global checkin server thread.
   * @return a global thread
   */
  public static CheckinThread getThread() {
    return thread;
  }

  /**
   * Sets the global thread.
   * @param thread the thread to be set to.
   */
  public static void setThread(CheckinThread thread) {
    ProgramRunner.thread = thread;
  }
}
