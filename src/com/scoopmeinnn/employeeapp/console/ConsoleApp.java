package com.scoopmeinnn.employeeapp.console;

import com.scoopmeinnn.employeeapp.model.Employee;
import com.scoopmeinnn.employeeapp.pay.SalaryCalculator;
import com.scoopmeinnn.employeeapp.repository.CsvEmployeeRepository;
import com.scoopmeinnn.employeeapp.repository.EmployeeRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Scanner;

/**
 * Console-based application for interacting with Employee data and computing salary.
 */
public class ConsoleApp {
    public static void main(String[] args) {
        // Initialize repository with the correct CSV path
        String csvPath = "C:/Users/DELL/Downloads/Correct MotorPH Employee Data.csv";
        EmployeeRepository repo = new CsvEmployeeRepository(csvPath);

        List<Employee> employees;
        try {
            employees = repo.loadAll();
        } catch (IOException e) {
            System.err.println("Failed to load employees: " + e.getMessage());
            return;
        }

        try (Scanner in = new Scanner(System.in)) {
            while (true) {
                System.out.println("=== Employee Management Console ===");
                System.out.println("1) List all employees");
                System.out.println("2) View a specific employee");
                System.out.println("3) Create new employee");
                System.out.println("4) Update an employee");
                System.out.println("5) Delete an employee");
                System.out.println("6) Compute employee salary");
                System.out.println("7) Exit");
                System.out.print("Choose an option: ");
                String choice = in.nextLine().trim();
                System.out.println();

                switch (choice) {
                    case "1":
                        // List all employees
                        employees.forEach(System.out::println);
                        System.out.println();
                        break;

                    case "2":
                        // View specific employee
                        System.out.print("Enter Employee #: ");
                        String id = in.nextLine().trim();
                        System.out.println();
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
                        System.out.print("Basic Salary: ");
                        BigDecimal basic = new BigDecimal(in.nextLine().trim());
                        System.out.print("Rice Subsidy: ");
                        BigDecimal rice = new BigDecimal(in.nextLine().trim());
                        System.out.print("Phone Allowance: ");
                        BigDecimal phone = new BigDecimal(in.nextLine().trim());
                        System.out.print("Clothing Allowance: ");
                        BigDecimal cloth = new BigDecimal(in.nextLine().trim());
                        System.out.print("Gross Semi-monthly Rate: ");
                        BigDecimal gross = new BigDecimal(in.nextLine().trim());
                        System.out.print("Hourly Rate: ");
                        BigDecimal hourly = new BigDecimal(in.nextLine().trim());
                        
                        Employee newEmp = new Employee(
                                newId, fn, ln, bd,
                                basic, rice, phone, cloth, gross, hourly
                        );
                        employees.add(newEmp);
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
                        System.out.println();
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
                        System.out.println();
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
                        // Compute employee salary
                        System.out.print("Enter Employee #: ");
                        String sid = in.nextLine().trim();
                        System.out.println();
                        Employee emp = employees.stream()
                                .filter(e -> e.getId().equals(sid))
                                .findFirst()
                                .orElse(null);
                        if (emp == null) {
                            System.out.println("Employee not found.\n");
                            break;
                        }
                        System.out.print("Enter month to compute (YYYY-MM): ");
                        YearMonth ym = YearMonth.parse(in.nextLine().trim());
                        BigDecimal pay = SalaryCalculator.computeMonthlyPay(emp, ym);
                        System.out.println();
                        System.out.println("=== Pay Details for " + ym + " ===");
                        System.out.println("Name: " + emp.getFirstName() + " " + emp.getLastName());
                        System.out.println("Gross Monthly: " + emp.getGrossSemiMonthlyRate().multiply(BigDecimal.valueOf(2)));
                        System.out.println("Rice Subsidy:   " + emp.getRiceSubsidy());
                        System.out.println("Phone Allowance: " + emp.getPhoneAllowance());
                        System.out.println("Clothing Allowance: " + emp.getClothingAllowance());
                        System.out.println("Total Pay:      " + pay + "\n");
                        break;

                    case "7":
                        // Exit
                        System.out.println("Goodbye!");
                        return;

                    default:
                        System.out.println("Invalid option. Please enter 1-7.\n");
                        break;
                }
            }
        }
    }
}
