package edu.brown.cs.student.maps.abstractions;

import java.util.Objects;

/**
 * Pair Class.
 * @param <X> first element of the pair
 * @param <Y> Second element of the pair
 */
public class Pair<X, Y> {
  private X fst;
  private Y scnd;

  /**
   * The Pair constructor.
   * @param f the first element of the pair
   * @param s the second element of the pair
   */
  public Pair(X f, Y s) {
    this.fst = f;
    this.scnd = s;
  }

  /**
   * Gets the first element of the pair.
   * @return the first element of the pair.
   */
  public X getFst() {
    return fst;
  }
  /**
   * Gets the second element of the pair.
   * @return the second element of the pair.
   */
  public Y getScnd() {
    return scnd;
  }

  /**
   * Overrides the to string method.
   * @return  a String with the first and second element of the pair
   */
  @Override
  public String toString() {
    return "Pair(" + "fst=" + fst + ", scnd=" + scnd + ')';
  }

  /**
   * Overrides Equals.
   * @param o  the object to be checked
   * @return  boolean
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Pair<?, ?> pair = (Pair<?, ?>) o;
    return Objects.equals(fst, pair.fst) && Objects.equals(scnd, pair.scnd);
  }

  /**
   * Override Hashcode.
   * @return  an int Hashcode
   */
  @Override
  public int hashCode() {
    return Objects.hash(fst, scnd);
  }
}
