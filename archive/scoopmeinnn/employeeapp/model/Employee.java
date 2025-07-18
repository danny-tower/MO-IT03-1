package com.scoopmeinnn.employeeapp.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents an employee, including personal and pay details.
 */
public class Employee {
    private final String id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;

    // Pay-related fields (Feature 2)
    private BigDecimal basicSalary;
    private BigDecimal riceSubsidy;
    private BigDecimal phoneAllowance;
    private BigDecimal clothingAllowance;
    private BigDecimal grossSemiMonthlyRate;
    private BigDecimal hourlyRate;

    /**
     * Constructs a new Employee with essential and pay fields.
     */
    public Employee(
        String id,
        String firstName,
        String lastName,
        LocalDate birthDate,
        BigDecimal basicSalary,
        BigDecimal riceSubsidy,
        BigDecimal phoneAllowance,
        BigDecimal clothingAllowance,
        BigDecimal grossSemiMonthlyRate,
        BigDecimal hourlyRate
    ) {
        this.id = Objects.requireNonNull(id, "ID must not be null");
        setFirstName(firstName);
        setLastName(lastName);
        this.birthDate = Objects.requireNonNull(birthDate, "Birth date must not be null");
        this.basicSalary = Objects.requireNonNull(basicSalary, "Basic salary must not be null");
        this.riceSubsidy = Objects.requireNonNull(riceSubsidy, "Rice subsidy must not be null");
        this.phoneAllowance = Objects.requireNonNull(phoneAllowance, "Phone allowance must not be null");
        this.clothingAllowance = Objects.requireNonNull(clothingAllowance, "Clothing allowance must not be null");
        this.grossSemiMonthlyRate = Objects.requireNonNull(grossSemiMonthlyRate, "Gross semi-monthly rate must not be null");
        this.hourlyRate = Objects.requireNonNull(hourlyRate, "Hourly rate must not be null");
    }

    // Getters and setters for all fields
    public String getId() { return id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) {
        if (firstName == null || firstName.isBlank()) throw new IllegalArgumentException("First name cannot be empty");
        this.firstName = firstName.trim();
    }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) throw new IllegalArgumentException("Last name cannot be empty");
        this.lastName = lastName.trim();
    }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = Objects.requireNonNull(birthDate); }

    public BigDecimal getBasicSalary() { return basicSalary; }
    public void setBasicSalary(BigDecimal basicSalary) { this.basicSalary = Objects.requireNonNull(basicSalary); }
    public BigDecimal getRiceSubsidy() { return riceSubsidy; }
    public void setRiceSubsidy(BigDecimal riceSubsidy) { this.riceSubsidy = Objects.requireNonNull(riceSubsidy); }
    public BigDecimal getPhoneAllowance() { return phoneAllowance; }
    public void setPhoneAllowance(BigDecimal phoneAllowance) { this.phoneAllowance = Objects.requireNonNull(phoneAllowance); }
    public BigDecimal getClothingAllowance() { return clothingAllowance; }
    public void setClothingAllowance(BigDecimal clothingAllowance) { this.clothingAllowance = Objects.requireNonNull(clothingAllowance); }
    public BigDecimal getGrossSemiMonthlyRate() { return grossSemiMonthlyRate; }
    public void setGrossSemiMonthlyRate(BigDecimal grossSemiMonthlyRate) { this.grossSemiMonthlyRate = Objects.requireNonNull(grossSemiMonthlyRate); }
    public BigDecimal getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(BigDecimal hourlyRate) { this.hourlyRate = Objects.requireNonNull(hourlyRate); }

    @Override
    public String toString() {
        return String.format(
            "Employee[id=%s, name=%s %s, birthDate=%s, basicSalary=%s, grossSemiMonthly=%s]",  
            id, firstName, lastName, birthDate, basicSalary, grossSemiMonthlyRate
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee other = (Employee) o;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
