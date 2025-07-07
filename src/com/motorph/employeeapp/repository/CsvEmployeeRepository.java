package com.motorph.employeeapp.repository;

import com.motorph.employeeapp.model.Employee;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV loader that picks up both ID fields and salary columns,
 * defaulting any non‐numeric or missing cell to zero.
 */
public class CsvEmployeeRepository implements EmployeeRepository {
    private final Path csvPath;
    private static final DateTimeFormatter DATE_FMT =
        DateTimeFormatter.ofPattern("M/d/yyyy");

    public CsvEmployeeRepository(String path) {
        this.csvPath = Paths.get(path);
    }

    @Override
    public List<Employee> loadAll() throws IOException {
        List<Employee> employees = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String headerLine = reader.readLine();
            if (headerLine == null) return employees;

            String[] headers = headerLine.split(",", -1);
            int idxId    = findIndex(headers, "employee");
            int idxLast  = findIndex(headers, "last name");
            int idxFirst = findIndex(headers, "first name");
            int idxSSS   = findIndex(headers, "sss");
            int idxPhil  = findIndex(headers, "philhealth");
            int idxTIN   = findIndex(headers, "tin");
            int idxPag   = findIndex(headers, "pag-ibig");
            int idxBasic = findIndex(headers, "basic salary");
            int idxRice  = findIndex(headers, "rice subsidy");
            int idxPhoneA= findIndex(headers, "phone allowance");
            int idxCloth = findIndex(headers, "clothing allowance");
            int idxSemi  = findIndex(headers, "semi-monthly rate");
            int idxHour  = findIndex(headers, "hourly rate");
            int idxBirth = findIndex(headers, "birthday");

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);

                // skip if ID or name columns missing
                if (parts.length <= idxFirst) continue;

                String id       = safeGet(parts, idxId);
                String lastName = safeGet(parts, idxLast);
                String firstName= safeGet(parts, idxFirst);
                LocalDate birth = parseDateOrNow(safeGet(parts, idxBirth));

                // parse numeric allowances or default to zero
                BigDecimal basic    = parseDecimalOrZero(safeGet(parts, idxBasic));
                BigDecimal rice     = parseDecimalOrZero(safeGet(parts, idxRice));
                BigDecimal phoneAll = parseDecimalOrZero(safeGet(parts, idxPhoneA));
                BigDecimal clothAll = parseDecimalOrZero(safeGet(parts, idxCloth));
                BigDecimal semi     = parseDecimalOrZero(safeGet(parts, idxSemi));
                BigDecimal hour     = parseDecimalOrZero(safeGet(parts, idxHour));

                Employee e = new Employee(
                    id, firstName, lastName, birth,
                    basic, rice, phoneAll, clothAll, semi, hour
                );

                // populate gov’t IDs
                e.setSssNumber( safeGet(parts, idxSSS) );
                e.setPhilHealthNumber( safeGet(parts, idxPhil) );
                e.setTinNumber( safeGet(parts, idxTIN) );
                e.setPagIbigNumber( safeGet(parts, idxPag) );

                employees.add(e);
            }
        }

        return employees;
    }

    @Override
    public void saveAll(List<Employee> list) throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    // Helpers

    /** Returns parts[index] or "" if out of bounds */
    private String safeGet(String[] parts, int index) {
        if (index >= 0 && index < parts.length) {
            return parts[index].trim().replaceAll("^\"|\"$", "");
        } else {
            return "";
        }
    }

    /** Parses a date in M/d/yyyy or returns today on error */
    private LocalDate parseDateOrNow(String text) {
        try {
            return LocalDate.parse(text, DATE_FMT);
        } catch (Exception ex) {
            return LocalDate.now();
        }
    }

    /** Parses a BigDecimal or returns zero for any invalid input */
    private BigDecimal parseDecimalOrZero(String text) {
        try {
            return new BigDecimal(text);
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }

    /** Finds first header containing keyword (case-insensitive) or -1 */
    private int findIndex(String[] headers, String keyword) {
        if (keyword == null) return -1;
        String key = keyword.toLowerCase();
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].toLowerCase().contains(key)) {
                return i;
            }
        }
        return -1;
    }
}
