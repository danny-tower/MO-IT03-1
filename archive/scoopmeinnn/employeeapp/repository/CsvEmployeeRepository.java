package com.scoopmeinnn.employeeapp.repository;



import com.scoopmeinnn.employeeapp.model.Employee;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV-backed implementation of the EmployeeRepository,
 * supporting both personal-only and full pay-detail CSVs.
 */
public class CsvEmployeeRepository implements EmployeeRepository {
    private final Path csvPath;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("M/d/yyyy");

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
                if (line.trim().isEmpty()) continue;
                // Split on commas that are not inside quotes
                                // Split on commas not inside quoted fields
                String[] cols = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                try {
                    // Mandatory fields
                    // Strip surrounding quotes and trim
                    String id = cols[0].trim().replaceAll("^\"|\"$", "");
                    String lastName = cols[1].trim();
                    String firstName = cols[2].trim();
                    LocalDate bd = LocalDate.parse(cols[3].trim(), DATE_FMT);

                    if (cols.length >= 7) {
                        // Full record: last 6 columns are pay fields
                        int n = cols.length;
                        String rawBasic = cols[n - 6].replaceAll("[\",\s]", "");
                        BigDecimal basicSalary = new BigDecimal(rawBasic);
                        String rawRice = cols[n - 5].replaceAll("[\",\s]", "");
                        BigDecimal riceSubsidy = new BigDecimal(rawRice);
                        String rawPhone = cols[n - 4].replaceAll("[\",\s]", "");
                        BigDecimal phoneAllowance = new BigDecimal(rawPhone);
                        String rawCloth = cols[n - 3].replaceAll("[\",\s]", "");
                        BigDecimal clothingAllowance = new BigDecimal(rawCloth);
                        String rawGross = cols[n - 2].replaceAll("[\",\s]", "");
                        BigDecimal grossSemiMonthly = new BigDecimal(rawGross);
                        String rawHourly = cols[n - 1].replaceAll("[\",\s]", "");
                        BigDecimal hourlyRate = new BigDecimal(rawHourly);

                        employees.add(new Employee(
                            id, firstName, lastName, bd,
                            basicSalary, riceSubsidy,
                            phoneAllowance, clothingAllowance,
                            grossSemiMonthly, hourlyRate
                        ));
                    } else if (cols.length >= 4) {
                    // Partial record: default all pay fields to zero
                    employees.add(new Employee(
                        id, firstName, lastName, bd,
                        BigDecimal.ZERO, BigDecimal.ZERO,
                        BigDecimal.ZERO, BigDecimal.ZERO,
                        BigDecimal.ZERO, BigDecimal.ZERO
                    ));
                } else {
                        System.err.println("Skipping incomplete line " + lineNum);
                    }
                } catch (DateTimeParseException | IllegalArgumentException ex) {
                    System.err.println("Error parsing line " + lineNum + ": " + ex.getMessage());
                }
            }
        }
        return employees;
    }

    @Override
    public void saveAll(List<Employee> employees) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(csvPath, StandardCharsets.UTF_8)) {
            // Write header
            writer.write("Employee #,Last Name,First Name,Birthday,Basic Salary,Rice Subsidy,Phone Allowance,Clothing Allowance,Gross Semi-monthly Rate,Hourly Rate");
            writer.newLine();

            for (Employee e : employees) {
                // Always write full record
                String line = String.join(",",
                    e.getId(),
                    e.getLastName(),
                    e.getFirstName(),
                    e.getBirthDate().format(DATE_FMT),
                    e.getBasicSalary().toString(),
                    e.getRiceSubsidy().toString(),
                    e.getPhoneAllowance().toString(),
                    e.getClothingAllowance().toString(),
                    e.getGrossSemiMonthlyRate().toString(),
                    e.getHourlyRate().toString()
                );
                writer.write(line);
                writer.newLine();
            }
        }
    }
}
