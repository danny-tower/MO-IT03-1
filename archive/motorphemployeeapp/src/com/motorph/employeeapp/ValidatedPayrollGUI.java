package com.motorph.employeeapp;

import javax.swing.*;
import java.awt.*;

public class ValidatedPayrollGUI extends JPanel {
    public ValidatedPayrollGUI(Department department) {
        setLayout(new BorderLayout());
        add(new JLabel("Payroll Management Panel for Department: " + department.getName()), BorderLayout.CENTER);
    }
} 