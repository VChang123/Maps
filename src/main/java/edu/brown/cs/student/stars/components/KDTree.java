package edu.brown.cs.student.stars.components;


import edu.brown.cs.student.stars.abstractions.ICoordinate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Class that creates a KDTree.
 *
 * @param <T> type T to be extensible
 */
public class KDTree<T extends ICoordinate> {
  private KDNode<T> root;

  /**
   * Constructor of the KDTree.
   */
  public KDTree() {
    root = null;
  }

  /**
   * Creates the KDtree.
   * @param list a list to create the kdtree of.
   * @param k the number of dimensions.
   * @param depth the starting depth
   * @return a KDNode
   */
  public KDNode<T> createTree(List<T> list, int k, int depth) {
    this.root = null;
    return createTreeHelper(list, k, depth);
  }

  /**
   * Creates a KDTree recursively.
   *
   * @param list  a list of Type T
   * @param k the number of dimensions to make the KDTree
   * @param depth the starting depth of the KDTree.
   * @return a KDNode.
   */
  private KDNode<T> createTreeHelper(List<T> list, int k, int depth) {
    //create a new list with the list passed
    list = new ArrayList<T>(list);
    KDNode<T> node = null;
    //if the list is not null
    if (list.size() != 0) {
      //calculate the current axis based on the depth
      int level = (depth % k);
      //sort the list based on the prevalent axis.
      List<T> sorted = this.sortList(list, level);
      //find the index of the median
      int index = this.findMedian(sorted, level);
      //create a new node and initialize its fields
      node = new KDNode<T>(null, null, level, sorted.get(index));
      //creats the root node of a KDTree
      if (root == null) {
        root = node;
      }
      //Create the left subtree list
      List<T> left = sorted.subList(0, index);
      //Create the right subtree list
      List<T> right = sorted.subList(index + 1, sorted.size());
      ArrayList<T> rightTree = new ArrayList<T>(right);
      ArrayList<T> leftTree = new ArrayList<T>(left);
      //recurse on both the right and left children with the respective lists
      node.setRight(createTreeHelper(rightTree, k, depth++));
      node.setLeft(createTreeHelper(leftTree, k, depth++));
    }
    return node;
  }

  /**
   * Sorts the list by axis given.
   *
   * @param list the list to be sorted.
   * @param axis the axis to sort by.
   * @return the sorted list
   */
  public List<T> sortList(List<T> list, int axis) {
    Collections.sort(list, new SortbyAxis(axis));
    return list;
  }

  /**
   * Finds the median of the list given the axis.
   *
   * @param sorted the list to be sorted
   * @param axis   the axis to find median by
   * @return the index of the median in the list.
   */
  public int findMedian(List<T> sorted, int axis) {
    int length = sorted.size();
    int index;
    index = length / 2;

    //checks for repeated values in the list
    index = this.checkDouble(sorted, axis, sorted.get(index).getCoords().get(axis));
    return index;
  }

  /**
   * Checks for repeated values of the median.
   *
   * @param list   the list passed in to check
   * @param axis   the axis to check by
   * @param median the current median
   * @return the index of the first instance of the median
   */
  public int checkDouble(List<T> list, int axis, double median) {
    int index = 0;
    for (int i = 0; i < list.size(); i++) {
      if (Double.compare(list.get(i).getCoords().get(axis), median) == 0) {
        index = i;
        break;
      }
    }
    return index;

  }

  /**
   * Gets the roots of the KDTree.
   *
   * @return a KDNode that is the root of the tree.
   */
  public KDNode<T> getRoot() {
    return root;
  }

  /**
   * sets the root of the tree.
   * @param node the node to set the root to.
   */
  public void setRoot(KDNode<T> node) {
    this.root = node;
  }

  /**
   * KDNode class that makes up a KDTree.
   *
   * @param <T> generic type of a KDNode
   */
  public static class KDNode<T extends ICoordinate> {
    /**
     * Instance variables.
     */
    private KDNode<T> right;
    private KDNode<T> left;
    private int depth;
    private T dimobj;

    /**
     * Constructor for KDNode.
     *
     * @param right  takes a right child
     * @param left   takes a left child
     * @param depth  stores the depth of a node
     * @param object stores an object
     */
    public KDNode(KDNode<T> right, KDNode<T> left, int depth, T object) {
      this.right = right;
      this.left = left;
      this.depth = depth;
      this.dimobj = object;

    }

    /**
     * sets the right child of a node.
     *
     * @param child a child node of the parent
     */
    public void setRight(KDNode<T> child) {
      this.right = child;
    }

    /**
     * Sets the left child of a node.
     *
     * @param child the child to be set to the left of a parent.
     */
    public void setLeft(KDNode<T> child) {
      this.left = child;
    }

    /**
     * gets the right child of a KDNode.
     *
     * @return returns a KDNode
     */
    public KDNode<T> getRight() {
      return right;
    }

    /**
     * gets the left child of a KNOde.
     *
     * @return returns a KDNode.
     */
    public KDNode<T> getLeft() {
      return left;
    }

    /**
     * Sets the depth of a KDNode.
     *
     * @param level the level to which the KDNode should be set to.
     */
    public void setDepth(int level) {
      this.depth = level;
    }

    /**
     * Gets the depth of a KDNode.
     *
     * @return returns an int depth.
     */
    public int getDepth() {
      return depth;
    }

    /**
     * sets the object of the KNode.
     *
     * @param obj the object to be stored
     */
    public void setObject(T obj) {
      this.dimobj = obj;
    }

    /**
     * Gets the object stored in the KNode.
     *
     * @return type T object.
     */
    public T getObject() {
      return this.dimobj;
    }
  }

  /**
   * Comparator that sorts by axis.
   */
  class SortbyAxis implements Comparator<T> {
    private int level;

    /**
     * Constructor of SortbyAxis.
     *
     * @param axis the axis to sort by
     */
    SortbyAxis(int axis) {
      level = axis;
    }

    /**
     * @param obj1 first star
     * @param obj2 second star
     * @return returns an int depending on the output
     */
    public int compare(T obj1, T obj2) {
      //compares the stars distance
      return Double.compare(obj1.getCoords().get(level), obj2.getCoords().get(level));

    }
  }
}
