package com.motorph.employeeapp;

import javax.swing.*;

/**
 * Main GUI launcher for the MotorPH Employee App,
 * now with modal login at startup.
 */
public class AppGUI {
    public static void main(String[] args) {
        // 1) Prompt for login before showing the main window
        JFrame dummy = new JFrame();
        dummy.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dummy.setSize(0, 0);
        dummy.setLocationRelativeTo(null);

        LoginDialog loginDlg = new LoginDialog(dummy);
        loginDlg.setVisible(true);

        // If login fails or is canceled, exit immediately
        if (!loginDlg.isSucceeded()) {
            System.exit(0);
        }

        // 2) Otherwise build & show the main application
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("MotorPH Employee App (GUI Version)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            Department hrDept = new Department("D001", "Human Resources");

            JTabbedPane tabs = new JTabbedPane();
            tabs.addTab("Employees",      new ValidatedEmployeeGUI(hrDept));
            tabs.addTab("Payroll",        new ValidatedPayrollGUI(hrDept));
            tabs.addTab("Leave Requests", new ValidatedLeaveRequestGUI(hrDept));

            frame.add(tabs);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
