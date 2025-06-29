package com.motorph.employeeapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

/**
 * A panel that lets you view/create/update/delete employees
 * (backed by a simple employees.csv file).
 */
public class ValidatedEmployeeGUI extends JPanel {
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField firstNameField, middleNameField, lastNameField, birthdayField;

    public ValidatedEmployeeGUI(Department dept) {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 245, 255));

        // Title
        JLabel title = new JLabel("Employee Records", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new String[]{"First name", "Middle name", "Last name", "Birthday"}, 0);
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setPreferredSize(new Dimension(400, 200));
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.add(tableScroll, BorderLayout.CENTER);

        // Form panel
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(BorderFactory.createTitledBorder("Add / Edit Employee"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        firstNameField = new JTextField(16);
        middleNameField = new JTextField(16);
        lastNameField = new JTextField(16);
        birthdayField = new JTextField(16);

        String[] labels = {"First name:", "Middle name:", "Last name:", "Birthday:"};
        JTextField[] fields = {firstNameField, middleNameField, lastNameField, birthdayField};
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            form.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            form.add(fields[i], gbc);
        }

        // Buttons
        JButton addBtn = new JButton("Save");
        JButton updateBtn = new JButton("Update");
        JButton delBtn = new JButton("Delete Record");

        addBtn.setBackground(new Color(153, 102, 255));
        addBtn.setForeground(Color.WHITE);
        updateBtn.setBackground(new Color(153, 102, 255));
        updateBtn.setForeground(Color.WHITE);
        delBtn.setBackground(new Color(255, 51, 51));
        delBtn.setForeground(Color.WHITE);

        Font btnFont = new Font("Segoe UI", Font.BOLD, 14);
        addBtn.setFont(btnFont);
        updateBtn.setFont(btnFont);
        delBtn.setFont(btnFont);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setOpaque(false);
        btnPanel.add(updateBtn);
        btnPanel.add(delBtn);
        btnPanel.add(addBtn);

        // Right panel (form + buttons)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightPanel.add(form, BorderLayout.CENTER);
        rightPanel.add(btnPanel, BorderLayout.SOUTH);

        // Main content panel
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        contentPanel.add(tablePanel);
        contentPanel.add(rightPanel);

        add(contentPanel, BorderLayout.CENTER);

        // Load data
        loadEmployees();

        // Add button logic
        addBtn.addActionListener(e -> {
            String fn = firstNameField.getText().trim();
            String mn = middleNameField.getText().trim();
            String ln = lastNameField.getText().trim();
            String bd = birthdayField.getText().trim();
            if (fn.isEmpty() || ln.isEmpty() || bd.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "First name, Last name, and Birthday are required.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                // Update existing record
                tableModel.setValueAt(fn, selectedRow, 0);
                tableModel.setValueAt(mn, selectedRow, 1);
                tableModel.setValueAt(ln, selectedRow, 2);
                tableModel.setValueAt(bd, selectedRow, 3);
                updateCSV();
            } else {
                // Add new record
                if (appendCSV("employees.csv", new String[]{fn, mn, ln, bd})) {
                    loadEmployees();
                }
            }
            clearFields();
            table.clearSelection();
        });

        updateBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a row to update.", "No selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            firstNameField.setText(tableModel.getValueAt(selectedRow, 0).toString());
            middleNameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            lastNameField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            birthdayField.setText(tableModel.getValueAt(selectedRow, 3).toString());
        });

        delBtn.addActionListener(e -> deleteSelected());

        // Auto-populate fields when row is selected
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    firstNameField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    middleNameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    lastNameField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    birthdayField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                }
            }
        });
    }

    private void loadEmployees() {
        tableModel.setRowCount(0);
        File f = new File("employees.csv");
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String hdr = br.readLine();
            String ln;
            while ((ln = br.readLine()) != null) {
                tableModel.addRow(ln.split(",", -1));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private boolean appendCSV(String file, String[] row) {
        boolean newFile = !new File(file).exists();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            if (newFile) {
                bw.write("First name,Middle name,Last name,Birthday");
                bw.newLine();
            }
            bw.write(String.join(",", row));
            bw.newLine();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private void clearFields() {
        firstNameField.setText("");
        middleNameField.setText("");
        lastNameField.setText("");
        birthdayField.setText("");
    }

    private void updateCSV() {
        File file = new File("employees.csv");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("First name,Middle name,Last name,Birthday");
            bw.newLine();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String fn = tableModel.getValueAt(i, 0).toString();
                String mn = tableModel.getValueAt(i, 1).toString();
                String ln = tableModel.getValueAt(i, 2).toString();
                String bd = tableModel.getValueAt(i, 3).toString();
                bw.write(String.join(",", fn, mn, ln, bd));
                bw.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteSelected() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(
                    this, "Select a row to delete.", "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String fn = tableModel.getValueAt(r, 0).toString();
        String ln = tableModel.getValueAt(r, 2).toString();
        String bd = tableModel.getValueAt(r, 3).toString();
        int c = JOptionPane.showConfirmDialog(
                this, "Delete employee " + fn + " " + ln + "?",
                "Confirm", JOptionPane.YES_NO_OPTION);
        if (c != JOptionPane.YES_OPTION) return;

        File in = new File("employees.csv");
        File out = new File("employees_tmp.csv");
        try (BufferedReader rIn = new BufferedReader(new FileReader(in));
             BufferedWriter wOut = new BufferedWriter(new FileWriter(out))) {
            String hdr = rIn.readLine();
            wOut.write(hdr);
            wOut.newLine();
            String line;
            while ((line = rIn.readLine()) != null) {
                String[] cArr = line.split(",", -1);
                if (!(cArr[0].equals(fn) && cArr[2].equals(ln) && cArr[3].equals(bd))) {
                    wOut.write(line);
                    wOut.newLine();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        in.delete();
        out.renameTo(in);
        loadEmployees();
    }
}
