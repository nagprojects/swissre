package org.assignment;

import java.io.IOException;
import org.assignment.service.EmployeeService;

public class AssignmentMain {
  public static void main(String[] args) {

    EmployeeService employeeService = new EmployeeService();

    try {
      employeeService.loadEmployeesFromCSV("src/main/resources/employees.csv");
      employeeService.analyzeSalariesAndReportingLines();
    } catch (IOException e) {
      System.err.println("Error reading file: " + e.getMessage());
    }
  }
}
