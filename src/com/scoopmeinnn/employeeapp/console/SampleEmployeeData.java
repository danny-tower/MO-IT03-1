package com.scoopmeinnn.employeeapp.console;

import com.scoopmeinnn.employeeapp.model.Employee;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Stub data for prototyping the console application.
 */
public class SampleEmployeeData {
    public static final List<Employee> EMPLOYEES;

    static {
        List<Employee> list = new ArrayList<>();
        // Embedded stub data (first 5 records) as reference
        list.add(new Employee("10001", "Manuel III", "Garcia", LocalDate.of(1983, 10, 11)));
        list.add(new Employee("10002", "Antonio",   "Lim",    LocalDate.of(1988, 6, 19)));
        list.add(new Employee("10003", "Bianca Sofia", "Aquino", LocalDate.of(1989, 8, 4)));
        list.add(new Employee("10004", "Isabella",  "Reyes",  LocalDate.of(1994, 6, 16)));
        list.add(new Employee("10005", "Eduard",     "Hernandez", LocalDate.of(1989, 9, 23)));
        
        EMPLOYEES = Collections.unmodifiableList(list);
    }
}
