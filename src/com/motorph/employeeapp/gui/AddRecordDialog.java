package com.motorph.employeeapp.gui;

import com.motorph.employeeapp.model.Employee;
import com.motorph.employeeapp.repository.EmployeeRepository;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

public class AddRecordDialog extends JDialog {
    private final EmployeeRepository repo;
    private final Runnable onSave;

    // Fields
    private final JTextField lastNameField       = new JTextField(15);
    private final JTextField firstNameField      = new JTextField(15);
    private final JDateChooser birthdayChooser   = new JDateChooser();
    private final JTextField addressField        = new JTextField(15);
    private final JTextField phoneField          = new JTextField(12);
    private final JTextField sssField            = new JTextField(12);
    private final JTextField philHealthField     = new JTextField(12);
    private final JTextField tinField            = new JTextField(12);
    private final JTextField pagIbigField        = new JTextField(12);
    private final JTextField statusField         = new JTextField(10);
    private final JTextField positionField       = new JTextField(15);
    private final JTextField supervisorField     = new JTextField(15);
    private final JTextField basicSalaryField    = new JTextField(10);
    private final JTextField riceSubsidyField    = new JTextField(10);
    private final JTextField phoneAllowanceField = new JTextField(10);
    private final JTextField clothingAllowanceField = new JTextField(10);
    private final JTextField semiMonthlyRateField   = new JTextField(10);
    private final JTextField hourlyRateField        = new JTextField(10);

    public AddRecordDialog(Frame owner, EmployeeRepository repo, Runnable onSave) {
        super(owner, "Add New Employee", true);
        this.repo = repo;
        this.onSave = onSave;

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
        JComponent[] fields = {
            lastNameField, firstNameField, birthdayChooser,
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
        JButton saveBtn   = new JButton("Save");
        JButton closeBtn = new JButton("Close");
        buttons.add(saveBtn);
        buttons.add(closeBtn);

        saveBtn.addActionListener(this::onSave);
        closeBtn.addActionListener(_ -> dispose());

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(form, BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.SOUTH);
    }

    private void onSave(ActionEvent ev) {
        try {
            if (lastNameField.getText().trim().isEmpty() ||
                firstNameField.getText().trim().isEmpty() ||
                birthdayChooser.getDate() == null ||
                sssField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Please fill in all required fields (Last Name, First Name, Birthday, SSS #).");
            }

            LocalDate bday = birthdayChooser.getDate()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

            // generate a random ID (or use another strategy)
            String id = UUID.randomUUID().toString();

            Employee e = new Employee(
                id,
                firstNameField.getText().trim(),
                lastNameField.getText().trim(),
                bday,
                parseBigDecimal(basicSalaryField.getText().trim()),
                parseBigDecimal(riceSubsidyField.getText().trim()),
                parseBigDecimal(phoneAllowanceField.getText().trim()),
                parseBigDecimal(clothingAllowanceField.getText().trim()),
                parseBigDecimal(semiMonthlyRateField.getText().trim()),
                parseBigDecimal(hourlyRateField.getText().trim())
            );

            e.setAddress(addressField.getText().trim());
            e.setPhone(phoneField.getText().trim());
            e.setSssNumber(sssField.getText().trim());
            e.setPhilHealthNumber(philHealthField.getText().trim());
            e.setTinNumber(tinField.getText().trim());
            e.setPagIbigNumber(pagIbigField.getText().trim());
            e.setStatus(statusField.getText().trim());
            e.setPosition(positionField.getText().trim());
            e.setSupervisor(supervisorField.getText().trim());

            List<Employee> all = repo.loadAll();
            all.add(e);
            repo.saveAll(all);

            JOptionPane.showMessageDialog(this, "Employee Record is saved.");
            dispose();
            if (onSave != null) onSave.run();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Invalid input: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private BigDecimal parseBigDecimal(String val) {
        if (val.isEmpty()) return BigDecimal.ZERO;
        return new BigDecimal(val);
    }
}
