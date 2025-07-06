package com.scoopmeinnn.employeeapp.repository;

import com.scoopmeinnn.employeeapp.model.Employee;
import java.io.IOException;
import java.util.List;

public interface EmployeeRepository {
    /** Load all employees from the data source. */
    List<Employee> loadAll() throws IOException;

    /** Save the given list of employees back to the data source. */
    void saveAll(List<Employee> employees) throws IOException;
}
    