package com.scoopmeinnn.employeeapp.repository;

import com.scoopmeinnn.employeeapp.model.Employee;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV-backed implementation of the EmployeeRepository.
 */
public class CsvEmployeeRepository implements EmployeeRepository {
    private final Path csvPath;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    /**
     * @param csvFilePath relative or absolute path to the CSV file
     */
    public CsvEmployeeRepository(String csvFilePath) {
        this.csvPath = Paths.get(csvFilePath);
    }

    @Override
    public List<Employee> loadAll() throws IOException {
        List<Employee> employees = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            // Skip header line
            reader.readLine();
            String line;
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] cols = line.split(",", -1);
                if (cols.length < 4) {
                    System.err.println("Skipping incomplete line " + lineNum);
                    continue;
                }

                try {
                    String id = cols[0].trim();
                    String lastName = cols[1].trim();
                    String firstName = cols[2].trim();
                    LocalDate bd = LocalDate.parse(cols[3].trim(), DATE_FMT);
                    employees.add(new Employee(id, firstName, lastName, bd));
                } catch (DateTimeParseException | IllegalArgumentException ex) {
                    System.err.println("Error parsing line " + lineNum + ": " + ex.getMessage());
                }
            }
        }
        return employees;
    }

    @Override
    public void saveAll(List<Employee> employees) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(csvPath)) {
            // Write header
            writer.write("Employee #,Last Name,First Name,Birthday");
            writer.newLine();

            for (Employee e : employees) {
                String line = String.join(",",
                    e.getId(),
                    e.getLastName(),
                    e.getFirstName(),
                    e.getBirthDate().format(DATE_FMT)
                );
                writer.write(line);
                writer.newLine();
            }
        }
    }
}
