package com.motorph.employeeapp;

import javax.swing.*;
import java.awt.*;

public class ValidatedLeaveRequestGUI extends JPanel {
    public ValidatedLeaveRequestGUI(Department department) {
        setLayout(new BorderLayout());
        add(new JLabel("Leave Request Panel for Department: " + department.getName()), BorderLayout.CENTER);
    }
} 