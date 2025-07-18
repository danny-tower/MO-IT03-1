package com.motorph.employeeapp;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents an employee in the application.
 */
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Employee {
    private final String id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;

    // Pay-related fields
    private BigDecimal basicSalary = BigDecimal.ZERO;
    private BigDecimal riceSubsidy = BigDecimal.ZERO;
    private BigDecimal phoneAllowance = BigDecimal.ZERO;
    private BigDecimal clothingAllowance = BigDecimal.ZERO;
    private BigDecimal grossSemiMonthlyRate = BigDecimal.ZERO;
    private BigDecimal hourlyRate = BigDecimal.ZERO;

    // For Payroll and LeaveRequest tracking
    private List<Payroll> payrolls = new ArrayList<>();
    private List<LeaveRequest> leaveRequests = new ArrayList<>();

    /**
     * Constructs a new Employee with only personal details.
     */
    public Employee(String id, String firstName, String lastName, LocalDate birthDate) {
        this.id = Objects.requireNonNull(id, "ID must not be null");
        setFirstName(firstName);
        setLastName(lastName);
        this.birthDate = Objects.requireNonNull(birthDate, "Birth date must not be null");
    }

    /**
     * Constructs a new Employee with pay details.
     */
    public Employee(String id, String firstName, String lastName, LocalDate birthDate,
                    BigDecimal basicSalary, BigDecimal riceSubsidy, BigDecimal phoneAllowance,
                    BigDecimal clothingAllowance, BigDecimal grossSemiMonthlyRate, BigDecimal hourlyRate) {
        this(id, firstName, lastName, birthDate);
        this.basicSalary = basicSalary != null ? basicSalary : BigDecimal.ZERO;
        this.riceSubsidy = riceSubsidy != null ? riceSubsidy : BigDecimal.ZERO;
        this.phoneAllowance = phoneAllowance != null ? phoneAllowance : BigDecimal.ZERO;
        this.clothingAllowance = clothingAllowance != null ? clothingAllowance : BigDecimal.ZERO;
        this.grossSemiMonthlyRate = grossSemiMonthlyRate != null ? grossSemiMonthlyRate : BigDecimal.ZERO;
        this.hourlyRate = hourlyRate != null ? hourlyRate : BigDecimal.ZERO;
    }
    // For GUI compatibility
    public String getEmployeeID() {
        return getId();
    }

    // For Payroll compatibility
    public void addPayroll(Payroll payroll) {
        if (payroll != null && !payrolls.contains(payroll)) {
            payrolls.add(payroll);
        }
    }

    // For LeaveRequest compatibility
    public void submitLeaveRequest(LeaveRequest request) {
        if (request != null && !leaveRequests.contains(request)) {
            leaveRequests.add(request);
        }
    }

    public List<Payroll> getPayrolls() {
        return payrolls;
    }

    public List<LeaveRequest> getLeaveRequests() {
        return leaveRequests;
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

    public BigDecimal getBasicSalary() { return basicSalary; }
    public void setBasicSalary(BigDecimal basicSalary) { this.basicSalary = basicSalary != null ? basicSalary : BigDecimal.ZERO; }
    public BigDecimal getRiceSubsidy() { return riceSubsidy; }
    public void setRiceSubsidy(BigDecimal riceSubsidy) { this.riceSubsidy = riceSubsidy != null ? riceSubsidy : BigDecimal.ZERO; }
    public BigDecimal getPhoneAllowance() { return phoneAllowance; }
    public void setPhoneAllowance(BigDecimal phoneAllowance) { this.phoneAllowance = phoneAllowance != null ? phoneAllowance : BigDecimal.ZERO; }
    public BigDecimal getClothingAllowance() { return clothingAllowance; }
    public void setClothingAllowance(BigDecimal clothingAllowance) { this.clothingAllowance = clothingAllowance != null ? clothingAllowance : BigDecimal.ZERO; }
    public BigDecimal getGrossSemiMonthlyRate() { return grossSemiMonthlyRate; }
    public void setGrossSemiMonthlyRate(BigDecimal grossSemiMonthlyRate) { this.grossSemiMonthlyRate = grossSemiMonthlyRate != null ? grossSemiMonthlyRate : BigDecimal.ZERO; }
    public BigDecimal getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(BigDecimal hourlyRate) { this.hourlyRate = hourlyRate != null ? hourlyRate : BigDecimal.ZERO; }

    @Override
    public String toString() {
        return String.format("Employee[id=%s, firstName=%s, lastName=%s, birthDate=%s, basicSalary=%s, grossSemiMonthly=%s]", id, firstName, lastName, birthDate, basicSalary, grossSemiMonthlyRate);
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
