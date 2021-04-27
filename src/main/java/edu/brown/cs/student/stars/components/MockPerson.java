package edu.brown.cs.student.stars.components;

/**
 * MockPerson class.
 */
public class MockPerson {
  private String firstName;
  private String lastName;
  private String dateTime;
  private String email;
  private String gender;
  private String streetAddress;

  /**
   * MockPerson constructor.
   * @param firstName - first name, a String
   * @param lastName - last name, a String
   * @param dateTime - date, a String
   * @param email - email, a String
   * @param gender - gender, a String
   * @param streetAddress - street address, a String
   */
  public MockPerson(String firstName, String lastName, String dateTime, String email, String gender,
                    String streetAddress) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.dateTime = dateTime;
    this.email = email;
    this.gender = gender;
    this.streetAddress = streetAddress;
  }

  /**
   * firstName getter.
   * @return - firstName
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * lastName getter.
   * @return - lastName
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * dateTime getter.
   * @return - dateTime.
   */
  public String getDateTime() {
    return dateTime;
  }

  /**
   * email getter.
   * @return - email.
   */
  public String getEmail() {
    return email;
  }

  /**
   * gender getter.
   * @return - gender.
   */
  public String getGender() {
    return gender;
  }

  /**
   * street address getter.
   * @return street address.
   */
  public String getStreetAddress() {
    return streetAddress;
  }

  /**
   * Converts all of a MockPerson's information into a single string.
   * @return - MockPerson's fields as a String, separated by commas.
   */
  public String toString() {
    String str = String.join(",", firstName, lastName, dateTime, email, gender,
        streetAddress);
    return str;
  }
}
