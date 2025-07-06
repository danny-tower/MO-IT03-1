package com.scoopmeinnn.employeeapp.console;

import com.scoopmeinnn.employeeapp.model.Employee;
import com.scoopmeinnn.employeeapp.repository.CsvEmployeeRepository;
import com.scoopmeinnn.employeeapp.repository.EmployeeRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Console-based application for interacting with Employee data.
 */
public class ConsoleApp {
    public static void main(String[] args) {
        // Initialize repository
        EmployeeRepository repo = new CsvEmployeeRepository(
            "C:\\Users\\DELL\\Downloads\\Correct MotorPH Employee Data - Employee Details.csv"
        );

        // Load employees from CSV
        List<Employee> employees;
        try {
            employees = repo.loadAll();
        } catch (IOException e) {
            System.err.println("Failed to load employees: " + e.getMessage());
            return;
        }

        // Use try-with-resources for Scanner to avoid resource leaks
        try (Scanner in = new Scanner(System.in)) {
            while (true) {
                System.out.println("=== Employee Management Console ===");
                System.out.println("1) List all employees");
                System.out.println("2) View a specific employee");
                System.out.println("3) Create new employee");
                System.out.println("4) Update an employee");
                System.out.println("5) Delete an employee");
                System.out.println("6) Exit");
                System.out.print("Choose an option: ");
                String choice = in.nextLine().trim();
                System.out.println();

                switch (choice) {
                    case "1":
                        // List all employees
                        System.out.println("-- All Employees --");
                        employees.forEach(System.out::println);
                        System.out.println();
                        break;

                    case "2":
                        // View specific employee
                        System.out.print("Enter Employee #: ");
                        String id = in.nextLine().trim();
                        employees.stream()
                                 .filter(e -> e.getId().equals(id))
                                 .findFirst()
                                 .ifPresentOrElse(
                                     System.out::println,
                                     () -> System.out.println("Employee not found.")
                                 );
                        System.out.println();
                        break;

                    case "3":
                        // Create new employee
                        System.out.print("Enter new Employee #: ");
                        String newId = in.nextLine().trim();
                        System.out.print("First name: ");
                        String fn = in.nextLine().trim();
                        System.out.print("Last name: ");
                        String ln = in.nextLine().trim();
                        System.out.print("Birthday (yyyy-MM-dd): ");
                        LocalDate bd = LocalDate.parse(in.nextLine().trim());
                        employees.add(new Employee(newId, fn, ln, bd));
                        try {
                            repo.saveAll(employees);
                            System.out.println("Employee created.\n");
                        } catch (IOException e) {
                            System.err.println("Failed to save employees: " + e.getMessage());
                        }
                        break;

                    case "4":
                        // Update an employee
                        System.out.print("Enter Employee # to update: ");
                        String updId = in.nextLine().trim();
                        employees.stream()
                                 .filter(e -> e.getId().equals(updId))
                                 .findFirst()
                                 .ifPresentOrElse(e -> {
                                     System.out.print("New first name (blank to keep): ");
                                     String ufn = in.nextLine().trim();
                                     if (!ufn.isBlank()) e.setFirstName(ufn);
                                     System.out.print("New last name (blank to keep): ");
                                     String uln = in.nextLine().trim();
                                     if (!uln.isBlank()) e.setLastName(uln);
                                     try {
                                         repo.saveAll(employees);
                                         System.out.println("Employee updated.\n");
                                     } catch (IOException ex) {
                                         System.err.println("Failed to save employees: " + ex.getMessage());
                                     }
                                 }, () -> System.out.println("Employee not found.\n"));
                        break;

                    case "5":
                        // Delete an employee
                        System.out.print("Enter Employee # to delete: ");
                        String delId = in.nextLine().trim();
                        boolean removed = employees.removeIf(e -> e.getId().equals(delId));
                        if (removed) {
                            try {
                                repo.saveAll(employees);
                                System.out.println("Employee deleted.\n");
                            } catch (IOException ex) {
                                System.err.println("Failed to save employees: " + ex.getMessage());
                            }
                        } else {
                            System.out.println("Employee not found.\n");
                        }
                        break;

                    case "6":
                        // Exit
                        System.out.println("Goodbye!");
                        return;

                    default:
                        System.out.println("Invalid option. Please enter 1-6.\n");
                        break;
                }
            }
        }
    }
}
