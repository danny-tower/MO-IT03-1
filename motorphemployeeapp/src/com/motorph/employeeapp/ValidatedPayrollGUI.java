package com.motorph.employeeapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

/**
 * A panel that lets you view/create/update/delete payroll entries
 * (backed by a simple payroll.csv file).
 */
public class ValidatedPayrollGUI extends JPanel {
    private final DefaultTableModel model;
    private final JTable table;
    private final JTextField empField, amtField;

    public ValidatedPayrollGUI(Department dept) {
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{"Employee ID","Amount"}, 0);
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setPreferredSize(new Dimension(350, 200));
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.add(tableScroll, BorderLayout.CENTER);

        // Form panel
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(BorderFactory.createTitledBorder("Add / Edit Payroll"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        empField = new JTextField(16);
        amtField = new JTextField(16);

        String[] labels = {"Employee ID:", "Amount:"};
        JTextField[] fields = {empField, amtField};
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
        loadPayroll();

        // Add button logic
        addBtn.addActionListener(e -> {
            String id = empField.getText().trim();
            String amt = amtField.getText().trim();
            if (id.isEmpty() || amt.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Both fields required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                Double.parseDouble(amt);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Amount must be numeric.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                // Update existing record
                model.setValueAt(id, selectedRow, 0);
                model.setValueAt(amt, selectedRow, 1);
                updateCSV();
            } else {
                appendCSV("payroll.csv", new String[]{id, amt});
                loadPayroll();
            }
            empField.setText("");
            amtField.setText("");
            table.clearSelection();
        });

        updateBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a row to update.", "No selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            empField.setText(model.getValueAt(selectedRow, 0).toString());
            amtField.setText(model.getValueAt(selectedRow, 1).toString());
        });

        delBtn.addActionListener(e -> openDelete());

        // Auto-populate fields when row is selected
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    empField.setText(model.getValueAt(selectedRow, 0).toString());
                    amtField.setText(model.getValueAt(selectedRow, 1).toString());
                }
            }
        });
    }

    private void loadPayroll() {
        model.setRowCount(0);
        File f = new File("payroll.csv");
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            br.readLine();
            String ln;
            while ((ln=br.readLine())!=null) {
                model.addRow(ln.split(",",-1));
            }
        } catch(IOException ex){ex.printStackTrace();}
    }

    private void appendCSV(String file, String[] row) {
        boolean nf = !new File(file).exists();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file,true))) {
            if (nf) {
                bw.write("EmployeeID,Amount"); bw.newLine();
            }
            bw.write(row[0] + "," + row[1]); bw.newLine();
        } catch(IOException ex){ex.printStackTrace();}
    }

    private void openUpdate() {
        int r = table.getSelectedRow();
        if (r<0) {
            JOptionPane.showMessageDialog(this,
                "Select an entry to update.","No selection",JOptionPane.WARNING_MESSAGE);
            return;
        }
        String oldId = model.getValueAt(r,0).toString();
        String oldAmt= model.getValueAt(r,1).toString();

        JTextField amtF = new JTextField(oldAmt);
        JPanel p = new JPanel(new GridLayout(1,2,5,5));
        p.add(new JLabel("Amount:")); p.add(amtF);
        int res = JOptionPane.showConfirmDialog(this,p,"Update Payroll",JOptionPane.OK_CANCEL_OPTION);
        if (res!=JOptionPane.OK_OPTION) return;
        String newAmt = amtF.getText().trim();
        try { Double.parseDouble(newAmt); }
        catch(NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Amount must be numeric.","Validation Error",JOptionPane.WARNING_MESSAGE);
            return;
        }

        // rewrite CSV
        File in  = new File("payroll.csv");
        File out = new File("payroll_tmp.csv");
        try (BufferedReader rIn = new BufferedReader(new FileReader(in));
             BufferedWriter wOut= new BufferedWriter(new FileWriter(out))) {

            String hdr = rIn.readLine();
            wOut.write(hdr); wOut.newLine();
            String ln;
            while ((ln=rIn.readLine())!=null) {
                String[] c = ln.split(",",-1);
                if (c[0].equals(oldId) && c[1].equals(oldAmt)) {
                    wOut.write(oldId + "," + newAmt);
                } else {
                    wOut.write(ln);
                }
                wOut.newLine();
            }
        } catch(IOException ex){ex.printStackTrace();}
        in.delete();
        out.renameTo(in);
        loadPayroll();
    }

    private void openDelete() {
        int r = table.getSelectedRow();
        if (r<0) {
            JOptionPane.showMessageDialog(this,
                "Select an entry to delete.","No selection",JOptionPane.WARNING_MESSAGE);
            return;
        }
        String id  = model.getValueAt(r,0).toString();
        String amt = model.getValueAt(r,1).toString();
        int c = JOptionPane.showConfirmDialog(this,
            "Delete payroll for " + id + " amount " + amt + "?",
            "Confirm",JOptionPane.YES_NO_OPTION);
        if (c!=JOptionPane.YES_OPTION) return;

        File in  = new File("payroll.csv");
        File out = new File("payroll_tmp.csv");
        try (BufferedReader rIn = new BufferedReader(new FileReader(in));
             BufferedWriter wOut= new BufferedWriter(new FileWriter(out))) {

            String hdr = rIn.readLine();
            wOut.write(hdr); wOut.newLine();
            String ln;
            while ((ln=rIn.readLine())!=null) {
                if (!ln.equals(id + "," + amt)) {
                    wOut.write(ln);
                    wOut.newLine();
                }
            }
        } catch(IOException ex){ex.printStackTrace();}
        in.delete();
        out.renameTo(in);
        loadPayroll();
    }

    private void updateCSV() {
        File in  = new File("payroll.csv");
        File out = new File("payroll_tmp.csv");
        try (BufferedReader rIn = new BufferedReader(new FileReader(in));
             BufferedWriter wOut= new BufferedWriter(new FileWriter(out))) {

            String hdr = rIn.readLine();
            wOut.write(hdr); wOut.newLine();
            String ln;
            while ((ln=rIn.readLine())!=null) {
                String[] c = ln.split(",",-1);
                if (c[0].equals(empField.getText().trim())) {
                    wOut.write(empField.getText().trim() + "," + amtField.getText().trim());
                } else {
                    wOut.write(ln);
                }
                wOut.newLine();
            }
        } catch(IOException ex){ex.printStackTrace();}
        in.delete();
        out.renameTo(in);
        loadPayroll();
    }
}
