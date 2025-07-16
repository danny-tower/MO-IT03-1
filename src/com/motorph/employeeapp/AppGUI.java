package com.motorph.employeeapp;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 * Main GUI launcher for the MotorPH Employee App.
 */
public class AppGUI {
    public static void main(String[] args) {
        // Show login dialog before launching the main window
        JFrame dummy = new JFrame();
        dummy.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dummy.setSize(0, 0);
        dummy.setLocationRelativeTo(null);

        LoginDialog loginDlg = new LoginDialog(dummy);
        loginDlg.setVisible(true);
        if (!loginDlg.isSucceeded()) {
            System.exit(0);
        }
        dummy.dispose();

        SwingUtilities.invokeLater(() -> {
            // create the main window
            JFrame frame = new JFrame("MotorPH Employee App (GUI Version)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // shared domain model
            Department hrDept = new Department("D001", "Human Resources");

            // build the tabbed pane
            JTabbedPane tabs = new JTabbedPane();
            tabs.addTab("Employees",      new ValidatedEmployeeGUI(hrDept));
            tabs.addTab("Payroll",        new ValidatedPayrollGUI(hrDept));
            tabs.addTab("Leave Requests", new ValidatedLeaveRequestGUI(hrDept));

            // add to frame and display
            frame.add(tabs);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
