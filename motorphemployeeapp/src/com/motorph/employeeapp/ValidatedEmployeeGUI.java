package com.motorph.employeeapp;

import javax.swing.*;
import java.awt.*;

public class ValidatedEmployeeGUI extends JPanel {
    public ValidatedEmployeeGUI(Department department) {
        setLayout(new BorderLayout());
        add(new JLabel("Employee Management Panel for Department: " + department.getName()), BorderLayout.CENTER);
    }
} 