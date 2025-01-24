package org.assignment.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.assignment.model.dto.Employee;

public class EmployeeService {

  private final Map<Integer, Employee> employees = new HashMap<>();
  private final Map<Integer, List<Employee>> managerToSubordinates = new HashMap<>();

  public void loadEmployeesFromCSV(String fileName) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String line;
      reader.readLine();

      while ((line = reader.readLine()) != null) {
        String[] data = line.split(",");
        int id = Integer.parseInt(data[0]);
        String firstName = data[1];
        String lastName = data[2];
        double salary = Double.parseDouble(data[3]);
        Integer managerId =
            data.length > 4 && !data[4].isEmpty() ? Integer.parseInt(data[4]) : null;

        Employee employee = new Employee(id, firstName, lastName, salary, managerId);
        employees.put(id, employee);

        Optional.ofNullable(managerId)
            .ifPresent(
                mgrId ->
                    managerToSubordinates
                        .computeIfAbsent(mgrId, k -> new ArrayList<>())
                        .add(employee));
      }
    }
  }

  public void analyzeSalariesAndReportingLines() {
    employees.values().stream()
        .filter(manager -> manager.getManagerId().isPresent()) // Only managers
        .forEach(
            manager -> {
              List<Employee> subordinates = managerToSubordinates.get(manager.getId());
              if (subordinates != null && !subordinates.isEmpty()) {
                double averageSalary =
                    subordinates.stream().mapToDouble(Employee::getSalary).average().orElse(0);

                double minSalary = averageSalary * 1.2;
                double maxSalary = averageSalary * 1.5;

                if (manager.getSalary() < minSalary) {
                  System.out.printf(
                      "%s earns less than they should by: %.2f%n",
                      manager.getFullName(), minSalary - manager.getSalary());
                }
                if (manager.getSalary() > maxSalary) {
                  System.out.printf(
                      "%s earns more than they should by: %.2f%n",
                      manager.getFullName(), manager.getSalary() - maxSalary);
                }
              }
            });

    employees
        .values()
        .forEach(
            employee -> {
              long reportingLineLength = getReportingLineLength(employee);
              if (reportingLineLength > 4) {
                System.out.printf(
                    "%s has too long a reporting line: %d%n",
                    employee.getFullName(), reportingLineLength);
              }
            });
  }

  public Map<Integer, Employee> getEmployees() {
    return employees;
  }

  public Map<Integer, List<Employee>> getManagerToSubordinates() {
    return managerToSubordinates;
  }

  private long getReportingLineLength(Employee employee) {
    long length = 0;
    Optional<Integer> managerId = employee.getManagerId();

    while (managerId.isPresent()) {
      length++;
      Employee manager = employees.get(managerId.get());
      managerId = manager.getManagerId();
    }

    return length;
  }
}
