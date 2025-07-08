package com.motorph.employeeapp.gui;

import com.motorph.employeeapp.model.Employee;
import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class ViewRecordDialog extends JDialog {
    public ViewRecordDialog(Frame owner, Employee employee) {
        super(owner, "View Employee Record", true);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);
        c.anchor = GridBagConstraints.WEST;

        // Same field order as Charvilla's
        String[] labels = {
            "Employee #:", "Last Name:", "First Name:", "Birthday:", "Address:", "Phone:", "SSS #:",
            "PhilHealth #:", "TIN #:", "Pag-IBIG #:", "Status:", "Position:", "Supervisor:",
            "Basic Salary:", "Rice Subsidy:", "Phone Allowance:", "Clothing Allowance:",
            "Semi-monthly Rate:", "Hourly Rate:"
        };
        String[] values = {
            employee.getId(),
            employee.getLastName(),
            employee.getFirstName(),
            employee.getBirthDate().format(DateTimeFormatter.ofPattern("MMMM d, yyyy")),
            employee.getAddress(),
            employee.getPhone(),
            employee.getSssNumber(),
            employee.getPhilHealthNumber(),
            employee.getTinNumber(),
            employee.getPagIbigNumber(),
            employee.getStatus(),
            employee.getPosition(),
            employee.getSupervisor(),
            employee.getBasicSalary().toPlainString(),
            employee.getRiceSubsidy().toPlainString(),
            employee.getPhoneAllowance().toPlainString(),
            employee.getClothingAllowance().toPlainString(),
            employee.getGrossSemiMonthlyRate().toPlainString(),
            employee.getHourlyRate().toPlainString()
        };

        for (int i = 0; i < labels.length; i++) {
            c.gridx = 0; c.gridy = i;
            form.add(new JLabel(labels[i]), c);
            c.gridx = 1;
            JTextField field = new JTextField(values[i], 18);
            field.setEditable(false);
            form.add(field, c);
        }

        // Close button
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(_ -> dispose());
        buttons.add(closeBtn);

        add(new JScrollPane(form), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }
}
