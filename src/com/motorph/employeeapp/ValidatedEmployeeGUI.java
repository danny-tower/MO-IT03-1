package com.motorph.employeeapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ValidatedEmployeeGUI extends JPanel {
    private JTextField idField, nameField, positionField;
    private JButton addButton;

    /**
     * Constructs the Add Employee panel. The department parameter is reserved for future use.
     */
    public ValidatedEmployeeGUI(Department department) {
        // You can use the department parameter in the future to add employees to a department list
        setLayout(new GridLayout(4, 2, 5, 5));
        setBorder(BorderFactory.createTitledBorder("Add Employee"));

        idField       = new JTextField();
        nameField     = new JTextField();
        positionField = new JTextField();

        add(new JLabel("ID:"));       add(idField);
        add(new JLabel("Name:"));     add(nameField);
        add(new JLabel("Position:")); add(positionField);

        addButton = new JButton("Add");
        addButton.addActionListener(this::handleAdd);

        add(new JPanel());
        add(addButton);
    }

    /**
     * Handles the Add button click event.
     */
    private void handleAdd(ActionEvent event) {
        String employeeId   = idField.getText().trim();
        String employeeName = nameField.getText().trim();
        String employeePosition  = positionField.getText().trim();
        if (!validateInputs(employeeId, employeeName, employeePosition)) {
            return;
        }
        Employee employee = createEmployee(employeeId, employeeName, employeePosition);
        // In a real app, you would add the employee to a list or database here
        JOptionPane.showMessageDialog(this, "Employee added: " + employee.getFirstName() + " " + employee.getLastName());
        clearFields();
    }

    /**
     * Creates a new Employee instance. Currently uses LocalDate.now() as a placeholder for birth date.
     */
    private Employee createEmployee(String id, String name, String position) {
        // You may want to split name into first/last or add a birth date picker in the future
        return new Employee(id, name, position, java.time.LocalDate.now());
    }

    /**
     * Clears all input fields.
     */
    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        positionField.setText("");
    }

    private boolean validateInputs(String id, String name, String position) {
        if (id.isEmpty() || name.isEmpty() || position.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
