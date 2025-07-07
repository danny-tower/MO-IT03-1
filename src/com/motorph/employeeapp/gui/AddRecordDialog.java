package com.motorph.employeeapp.gui;

import com.motorph.employeeapp.model.Employee;
import com.motorph.employeeapp.repository.EmployeeRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;

public class AddRecordDialog extends JDialog {
    private final EmployeeRepository repo;

    // form fields (same as UpdateDialog, but no idField)
    private final JTextField lastNameField         = new JTextField(15);
    private final JTextField firstNameField        = new JTextField(15);
    private final JTextField birthdayField         = new JTextField(12); // "MMMM d, yyyy"
    private final JTextField addressField          = new JTextField(20);
    private final JTextField phoneField            = new JTextField(12);
    private final JTextField sssField              = new JTextField(12);
    private final JTextField philHealthField       = new JTextField(12);
    private final JTextField tinField              = new JTextField(12);
    private final JTextField pagIbigField          = new JTextField(12);
    private final JTextField statusField           = new JTextField(10);
    private final JTextField positionField         = new JTextField(15);
    private final JTextField supervisorField       = new JTextField(15);
    private final JTextField basicSalaryField      = new JTextField(10);
    private final JTextField riceSubsidyField      = new JTextField(10);
    private final JTextField phoneAllowanceField   = new JTextField(10);
    private final JTextField clothingAllowanceField= new JTextField(10);
    private final JTextField semiMonthlyRateField  = new JTextField(10);
    private final JTextField hourlyRateField       = new JTextField(10);

    public AddRecordDialog(Frame owner, EmployeeRepository repo) {
        super(owner, "Add New Employee", true);
        this.repo = repo;

        initLayout();

        pack();
        setLocationRelativeTo(owner);
    }

    private void initLayout() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);
        c.anchor = GridBagConstraints.WEST;

        String[] labels = {
            "Last Name:", "First Name:", "Birthday:",
            "Address:", "Phone:", "SSS #:", "PhilHealth #:",
            "TIN #:", "Pag-IBIG #:", "Status:", "Position:",
            "Supervisor:", "Basic Salary:", "Rice Subsidy:",
            "Phone Allowance:", "Clothing Allowance:",
            "Semi-monthly Rate:", "Hourly Rate:"
        };
        JTextField[] fields = {
            lastNameField, firstNameField, birthdayField,
            addressField, phoneField, sssField, philHealthField,
            tinField, pagIbigField, statusField, positionField,
            supervisorField, basicSalaryField, riceSubsidyField,
            phoneAllowanceField, clothingAllowanceField,
            semiMonthlyRateField, hourlyRateField
        };

        for (int i = 0; i < labels.length; i++) {
            c.gridx = 0; c.gridy = i;
            form.add(new JLabel(labels[i]), c);
            c.gridx = 1;
            form.add(fields[i], c);
        }

        // Buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addBtn   = new JButton("Add");
        JButton closeBtn = new JButton("Close");
        buttons.add(addBtn);
        buttons.add(closeBtn);

        addBtn.addActionListener(this::onAdd);
        closeBtn.addActionListener(_ -> dispose());

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JScrollPane(form), BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.SOUTH);
    }

    private void onAdd(ActionEvent ev) {
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMMM d, yyyy");
            LocalDate bday = LocalDate.parse(birthdayField.getText(), fmt);

            // generate a random ID (or use another strategy)
            String id = UUID.randomUUID().toString();

            Employee e = new Employee(
                id,
                firstNameField.getText().trim(),
                lastNameField.getText().trim(),
                bday,
                new BigDecimal(basicSalaryField.getText().trim()),
                new BigDecimal(riceSubsidyField.getText().trim()),
                new BigDecimal(phoneAllowanceField.getText().trim()),
                new BigDecimal(clothingAllowanceField.getText().trim()),
                new BigDecimal(semiMonthlyRateField.getText().trim()),
                new BigDecimal(hourlyRateField.getText().trim())
            );

            List<Employee> all = repo.loadAll();
            all.add(e);
            repo.saveAll(all);

            JOptionPane.showMessageDialog(this, "Employee Record is saved.");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Invalid input: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
