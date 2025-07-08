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

public class UpdateDialog extends JDialog {
    private final EmployeeRepository repo;
    private final Employee employee;
    private final Runnable onUpdate;

    private final JTextField idField             = new JTextField(10);
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

    public UpdateDialog(Frame owner, EmployeeRepository repo, Employee employee, Runnable onUpdate) {
        super(owner, "Employee Details", true);
        this.repo = repo;
        this.employee = employee;
        this.onUpdate = onUpdate;

        buildForm();
        loadEmployee();

        pack();
        setLocationRelativeTo(owner);
    }

    private void buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);
        c.anchor = GridBagConstraints.WEST;

        String[] labels = {
            "Employee #:", "Last Name:", "First Name:", "Birthday:",
            "Address:", "Phone:", "SSS #:", "PhilHealth #:", "TIN #:",
            "Pag-IBIG #:", "Status:", "Position:", "Supervisor:",
            "Basic Salary:", "Rice Subsidy:", "Phone Allowance:",
            "Clothing Allowance:", "Semi-monthly Rate:", "Hourly Rate:"
        };
        JComponent[] fields = {
            idField, lastNameField, firstNameField, birthdayChooser,
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
        idField.setEditable(false);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton updateBtn = new JButton("Update");
        JButton closeBtn  = new JButton("Close");
        updateBtn.addActionListener(this::onUpdate);
        closeBtn.addActionListener(_ -> dispose());
        buttons.add(updateBtn);
        buttons.add(closeBtn);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(form, BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.SOUTH);
    }

    private void loadEmployee() {
        idField.setText(employee.getId());
        lastNameField.setText(employee.getLastName());
        firstNameField.setText(employee.getFirstName());
        birthdayChooser.setDate(java.sql.Date.valueOf(employee.getBirthDate()));
        addressField.setText(employee.getAddress());
        phoneField.setText(employee.getPhone());
        sssField.setText(employee.getSssNumber());
        philHealthField.setText(employee.getPhilHealthNumber());
        tinField.setText(employee.getTinNumber());
        pagIbigField.setText(employee.getPagIbigNumber());
        statusField.setText(employee.getStatus());
        positionField.setText(employee.getPosition());
        supervisorField.setText(employee.getSupervisor());
        basicSalaryField.setText(employee.getBasicSalary().toString());
        riceSubsidyField.setText(employee.getRiceSubsidy().toString());
        phoneAllowanceField.setText(employee.getPhoneAllowance().toString());
        clothingAllowanceField.setText(employee.getClothingAllowance().toString());
        semiMonthlyRateField.setText(employee.getGrossSemiMonthlyRate().toString());
        hourlyRateField.setText(employee.getHourlyRate().toString());
    }

    private void onUpdate(ActionEvent ev) {
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

            employee.setLastName(lastNameField.getText().trim());
            employee.setFirstName(firstNameField.getText().trim());
            employee.setBirthDate(bday);
            employee.setAddress(addressField.getText().trim());
            employee.setPhone(phoneField.getText().trim());
            employee.setSssNumber(sssField.getText().trim());
            employee.setPhilHealthNumber(philHealthField.getText().trim());
            employee.setTinNumber(tinField.getText().trim());
            employee.setPagIbigNumber(pagIbigField.getText().trim());
            employee.setStatus(statusField.getText().trim());
            employee.setPosition(positionField.getText().trim());
            employee.setSupervisor(supervisorField.getText().trim());
            employee.setBasicSalary(parseBigDecimal(basicSalaryField.getText().trim()));
            employee.setRiceSubsidy(parseBigDecimal(riceSubsidyField.getText().trim()));
            employee.setPhoneAllowance(parseBigDecimal(phoneAllowanceField.getText().trim()));
            employee.setClothingAllowance(parseBigDecimal(clothingAllowanceField.getText().trim()));
            employee.setGrossSemiMonthlyRate(parseBigDecimal(semiMonthlyRateField.getText().trim()));
            employee.setHourlyRate(parseBigDecimal(hourlyRateField.getText().trim()));

            List<Employee> all = repo.loadAll();
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getId().equals(employee.getId())) {
                    all.set(i, employee);
                    break;
                }
            }
            repo.saveAll(all);
            JOptionPane.showMessageDialog(this, "Record Updated!");
            dispose();
            if (onUpdate != null) onUpdate.run();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Invalid input: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private BigDecimal parseBigDecimal(String val) {
        if (val.isEmpty()) return BigDecimal.ZERO;
        return new BigDecimal(val);
    }
}
