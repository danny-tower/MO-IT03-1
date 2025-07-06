package com.scoopmeinnn.employeeapp.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents an employee in the application.
 */
public class Employee {
    private final String id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;

    /**
     * Constructs a new Employee instance.
     *
     * @param id         unique identifier (non-null)
     * @param firstName  first name (non-empty)
     * @param lastName   last name (non-empty)
     * @param birthDate  birth date (non-null)
     */
    public Employee(String id, String firstName, String lastName, LocalDate birthDate) {
        this.id = Objects.requireNonNull(id, "ID must not be null");
        setFirstName(firstName);
        setLastName(lastName);
        this.birthDate = Objects.requireNonNull(birthDate, "Birth date must not be null");
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        this.firstName = firstName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        this.lastName = lastName.trim();
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = Objects.requireNonNull(birthDate, "Birth date must not be null");
    }

    @Override
    public String toString() {
        return String.format("Employee[id=%s, firstName=%s, lastName=%s, birthDate=%s]", id, firstName, lastName, birthDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee other = (Employee) o;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
