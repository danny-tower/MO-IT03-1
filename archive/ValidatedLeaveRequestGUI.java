package com.motorph.employeeapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;

/**
 * Panel for submitting leave requests. Validates input and shows feedback.
 */
public class ValidatedLeaveRequestGUI extends JPanel {
    private Department dept;
    private JTextField empIdField, startDateField, endDateField, reasonField;
    private JButton submitButton;

    public ValidatedLeaveRequestGUI(Department dept) {
        this.dept = dept;
        setLayout(new GridLayout(5, 2, 10, 10));
        setBorder(BorderFactory.createTitledBorder("Submit Leave Request"));

        empIdField      = new JTextField();
        startDateField  = new JTextField();
        endDateField    = new JTextField();
        reasonField     = new JTextField();

        add(new JLabel("Employee ID:"));            add(empIdField);
        add(new JLabel("Start Date (YYYY-MM-DD):"));add(startDateField);
        add(new JLabel("End Date (YYYY-MM-DD):"));  add(endDateField);
        add(new JLabel("Reason:"));                 add(reasonField);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(this::handleSubmit);
        add(new JPanel()); add(submitButton);
    }

    /**
     * Handles the Submit button click event for leave requests.
     */
    private void handleSubmit(ActionEvent event) {
        String employeeId = empIdField.getText().trim();
        String startDateText = startDateField.getText().trim();
        String endDateText = endDateField.getText().trim();
        String reason = reasonField.getText().trim();
        if (!validateInputs(employeeId, startDateText, endDateText, reason)) {
            return;
        }
        Employee employee = findEmployeeById(employeeId);
        if (employee == null) {
            JOptionPane.showMessageDialog(this, "Employee not found for ID: " + employeeId, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            LocalDate start = LocalDate.parse(startDateText);
            LocalDate end   = LocalDate.parse(endDateText);
            new LeaveRequest("L" + System.currentTimeMillis(), start, end, reason, employee);
            JOptionPane.showMessageDialog(this, "Leave request submitted for: " + employee.getFirstName() + " " + employee.getLastName(), "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Validates the input fields for leave request.
     */
    private boolean validateInputs(String id, String start, String end, String reason) {
        if (id.isEmpty() || start.isEmpty() || end.isEmpty() || reason.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
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
        startDateField.setText("");
        endDateField.setText("");
        reasonField.setText("");
    }
}