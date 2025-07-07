package com.motorph.employeeapp.gui;

import com.motorph.employeeapp.model.Employee;
import com.motorph.employeeapp.repository.EmployeeRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.math.BigDecimal;


public class UpdateDialog extends JDialog {
    private final EmployeeRepository repo;
    private final Employee employee;

    private final JTextField idField             = new JTextField(10);
    private final JTextField lastNameField       = new JTextField(15);
    private final JTextField firstNameField      = new JTextField(15);
    private final JTextField birthdayField       = new JTextField(12);
    private final JTextField addressField        = new JTextField(20);
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

    public UpdateDialog(Frame owner, EmployeeRepository repo, Employee employee) {
        super(owner, "Employee Details", true);
        this.repo = repo;
        this.employee = employee;

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
        JTextField[] fields = {
            idField, lastNameField, firstNameField, birthdayField,
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
        getContentPane().add(new JScrollPane(form), BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.SOUTH);
    }

    private void loadEmployee() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        idField.setText(employee.getId());
        lastNameField.setText(employee.getLastName());
        firstNameField.setText(employee.getFirstName());
        birthdayField.setText(employee.getBirthDate().format(fmt));
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
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMMM d, yyyy");
            employee.setLastName(lastNameField.getText());
            employee.setFirstName(firstNameField.getText());
            employee.setBirthDate(LocalDate.parse(birthdayField.getText(), fmt));
            employee.setAddress(addressField.getText());
            employee.setPhone(phoneField.getText());
            employee.setSssNumber(sssField.getText());
            employee.setPhilHealthNumber(philHealthField.getText());
            employee.setTinNumber(tinField.getText());
            employee.setPagIbigNumber(pagIbigField.getText());
            employee.setStatus(statusField.getText());
            employee.setPosition(positionField.getText());
            employee.setSupervisor(supervisorField.getText());
            employee.setBasicSalary(new BigDecimal(basicSalaryField.getText()));
            employee.setRiceSubsidy(new BigDecimal(riceSubsidyField.getText()));
            employee.setPhoneAllowance(new BigDecimal(phoneAllowanceField.getText()));
            employee.setClothingAllowance(new BigDecimal(clothingAllowanceField.getText()));
            employee.setGrossSemiMonthlyRate(new BigDecimal(semiMonthlyRateField.getText()));
            employee.setHourlyRate(new BigDecimal(hourlyRateField.getText()));

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
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Invalid input: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
