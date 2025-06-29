package com.motorph.employeeapp;

import javax.swing.*;
import java.awt.*;

/**
 * A simple modal login dialog.
 * Hard‐coded to accept "admin" / "admin123" but you can change those credentials here.
 */
public class LoginDialog extends JDialog {
    private JTextField userField;
    private JPasswordField passField;
    private boolean succeeded;

    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "admin123";

    public LoginDialog(Frame parent) {
        super(parent, "Login", true);

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Username:"));
        userField = new JTextField(20);
        panel.add(userField);
        panel.add(new JLabel("Password:"));
        passField = new JPasswordField(20);
        panel.add(passField);

        JButton loginBtn  = new JButton("Login");
        JButton cancelBtn = new JButton("Cancel");
        JPanel buttons = new JPanel();
        buttons.add(loginBtn);
        buttons.add(cancelBtn);

        loginBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());
            if (VALID_USERNAME.equals(username) && VALID_PASSWORD.equals(password)) {
                succeeded = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(
                    LoginDialog.this,
                    "Invalid username or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
                );
                userField.setText("");
                passField.setText("");
                succeeded = false;
            }
        });

        cancelBtn.addActionListener(e -> {
            succeeded = false;
            dispose();
        });

        getContentPane().add(panel,    BorderLayout.CENTER);
        getContentPane().add(buttons,  BorderLayout.SOUTH);
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    public boolean isSucceeded() {
        return succeeded;
    }
}
