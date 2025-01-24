package org.assignment.model.dto;

import java.util.Optional;

public class Employee {
  private int id;
  private String firstName;
  private String lastName;
  private double salary;
  private Optional<Integer> managerId;

  public Employee(int id, String firstName, String lastName, double salary, Integer managerId) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.salary = salary;
    this.managerId = Optional.ofNullable(managerId);
  }

  public int getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public double getSalary() {
    return salary;
  }

  public Optional<Integer> getManagerId() {
    return managerId;
  }

  public String getFullName() {
    return firstName + " " + lastName;
  }
}
