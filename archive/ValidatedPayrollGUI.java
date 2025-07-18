package com.motorph.employeeapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;

/**
 * Panel for adding Payroll entries with basic validation.
 */
public class ValidatedPayrollGUI extends JPanel {
    private Department dept;
    private JTextField empIdField, amountField;
    private JButton addButton;

    /**
     * Constructs the Add Payroll panel. The dept parameter is reserved for future use and employee lookup.
     */
    public ValidatedPayrollGUI(Department dept) {
        this.dept = dept;
        setLayout(new GridLayout(3, 2, 5, 5));
        setBorder(BorderFactory.createTitledBorder("Add Payroll"));

        empIdField  = new JTextField();
        amountField = new JTextField();

        add(new JLabel("Employee ID:")); add(empIdField);
        add(new JLabel("Amount:"));      add(amountField);

        addButton = new JButton("Add");
        addButton.addActionListener(this::handleAdd);

        add(new JPanel());
        add(addButton);
    }

    /**
     * Handles the Add button click event for payroll.
     */
    private void handleAdd(ActionEvent event) {
        String employeeId = empIdField.getText().trim();
        String amountText = amountField.getText().trim();
        if (!validateInputs(employeeId, amountText)) {
            return;
        }
        Employee employee = findEmployeeById(employeeId);
        if (employee == null) {
            JOptionPane.showMessageDialog(this, "Employee not found for ID: " + employeeId, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            double amount = Double.parseDouble(amountText);
            new Payroll("P" + System.currentTimeMillis(), LocalDate.now(), amount, amount, employee);
            JOptionPane.showMessageDialog(this, "Payroll added for employee: " + employee.getFirstName() + " " + employee.getLastName());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        clearFields();
    }

    /**
     * Validates the input fields for payroll.
     */
    private boolean validateInputs(String id, String amount) {
        if (id.isEmpty() || amount.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Finds an employee by ID in the department.
     */
    private Employee findEmployeeById(String id) {
        return dept.getEmployees().stream()
                .filter(x -> x.getEmployeeID().equalsIgnoreCase(id))
                .findFirst().orElse(null);
    }

    /**
     * Clears all input fields.
     */
    private void clearFields() {
        empIdField.setText("");
        amountField.setText("");
    }
}