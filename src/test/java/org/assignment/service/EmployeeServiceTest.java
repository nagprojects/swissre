package org.assignment.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.assignment.model.dto.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EmployeeServiceTest {
  private EmployeeService employeeService;

  @BeforeEach
  void setup() {
    employeeService = new EmployeeService();
  }

  @Test
  public void testLoadEmployeesFromCSV() throws IOException {

    employeeService.loadEmployeesFromCSV("src/main/resources/employees.csv");

    Employee joe = employeeService.getEmployees().get(123);
    assertNotNull(joe);
    assertEquals("Joe", joe.getFirstName());
    assertEquals("Doe", joe.getLastName());

    assertTrue(employeeService.getManagerToSubordinates().containsKey(123));
    assertEquals(2, employeeService.getManagerToSubordinates().get(123).size());
    assertFalse(employeeService.getManagerToSubordinates().get(123).contains(joe));
  }

  @Test
  public void testMissingManagerId() throws IOException {

    employeeService.loadEmployeesFromCSV("src/main/resources/employees.csv");

    Employee joe = employeeService.getEmployees().get(123);
    assertFalse(joe.getManagerId().isPresent());

    Employee martin = employeeService.getEmployees().get(124);
    assertTrue(employeeService.getManagerToSubordinates().get(123).contains(martin));
  }

  @Test
  public void testAnalyzeSalariesAndReportingLines() throws IOException {
    EmployeeService service = new EmployeeService();
    service.loadEmployeesFromCSV("src/main/resources/employees.csv");

    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    service.analyzeSalariesAndReportingLines();

    assertTrue(
        outContent.toString().contains("Goranti Nagaraju earns less than they should by: 2000.00"));

    assertTrue(outContent.toString().contains("Goranti Shiva has too long a reporting line: 5"));

    assertTrue(
        outContent.toString().contains("Goranti Ravi earns more than they should by: 5000.00"));
  }
}
