package com.motorph.employeeapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class ValidatedLeaveRequestGUI extends JPanel {
    private final DefaultTableModel model;
    private final JTable table;
    private final JTextField empField, startField, endField, reasonField;

    public ValidatedLeaveRequestGUI(Department dept) {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 245, 255));

        // Title
        JLabel title = new JLabel("Leave Requests", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(
            new String[]{"Employee ID","Start Date","End Date","Reason"}, 0
        );
        table = new JTable(model);
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
        form.setBorder(BorderFactory.createTitledBorder("Submit / Edit Leave Request"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        empField = new JTextField(16);
        startField = new JTextField(16);
        endField = new JTextField(16);
        reasonField = new JTextField(16);

        String[] labels = {"Employee ID:", "Start Date (YYYY-MM-DD):", "End Date (YYYY-MM-DD):", "Reason:"};
        JTextField[] fields = {empField, startField, endField, reasonField};
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            form.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            form.add(fields[i], gbc);
        }

        // Buttons
        JButton addBtn    = new JButton("Submit");
        JButton updateBtn = new JButton("Update");
        JButton delBtn    = new JButton("Delete");

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

        loadRequests();

        addBtn.addActionListener(e -> {
            String id = empField.getText().trim();
            String sd = startField.getText().trim();
            String ed = endField.getText().trim();
            String rs = reasonField.getText().trim();
            if (id.isEmpty()||sd.isEmpty()||ed.isEmpty()||rs.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "All fields required.","Validation Error",JOptionPane.WARNING_MESSAGE);
                return;
            }
            appendCSV("leaverequests.csv", new String[]{id,sd,ed,rs});
            loadRequests();
            empField.setText(""); startField.setText("");
            endField.setText(""); reasonField.setText("");
        });

        updateBtn.addActionListener(e -> openUpdate());
        delBtn.addActionListener(e -> openDelete());

        // Auto-populate fields when row is selected
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    empField.setText(model.getValueAt(selectedRow, 0).toString());
                    startField.setText(model.getValueAt(selectedRow, 1).toString());
                    endField.setText(model.getValueAt(selectedRow, 2).toString());
                    reasonField.setText(model.getValueAt(selectedRow, 3).toString());
                }
            }
        });
    }

    private void loadRequests() {
        model.setRowCount(0);
        File f = new File("leaverequests.csv");
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
                bw.write("EmployeeID,StartDate,EndDate,Reason");
                bw.newLine();
            }
            bw.write(String.join(",",row)); bw.newLine();
        } catch(IOException ex){ex.printStackTrace();}
    }

    private void openUpdate() {
        int r = table.getSelectedRow();
        if (r<0) {
            JOptionPane.showMessageDialog(this,
                "Select a request to update.","No selection",JOptionPane.WARNING_MESSAGE);
            return;
        }
        String oldId = model.getValueAt(r,0).toString();
        String oldSd = model.getValueAt(r,1).toString();
        String oldEd = model.getValueAt(r,2).toString();
        String oldRs = model.getValueAt(r,3).toString();

        JTextField sdF = new JTextField(oldSd);
        JTextField edF = new JTextField(oldEd);
        JTextField rsF = new JTextField(oldRs);
        JPanel p = new JPanel(new GridLayout(3,2,5,5));
        p.add(new JLabel("Start Date:")); p.add(sdF);
        p.add(new JLabel("End Date:"));   p.add(edF);
        p.add(new JLabel("Reason:"));     p.add(rsF);
        int res = JOptionPane.showConfirmDialog(this,p,
            "Update Leave Request",JOptionPane.OK_CANCEL_OPTION);
        if (res!=JOptionPane.OK_OPTION) return;

        String newSd = sdF.getText().trim();
        String newEd = edF.getText().trim();
        String newRs = rsF.getText().trim();
        if (newSd.isEmpty()||newEd.isEmpty()||newRs.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "All fields required.","Validation Error",JOptionPane.WARNING_MESSAGE);
            return;
        }

        File in  = new File("leaverequests.csv");
        File out = new File("leaverequests_tmp.csv");
        try (BufferedReader rIn = new BufferedReader(new FileReader(in));
             BufferedWriter wOut= new BufferedWriter(new FileWriter(out))) {

            String hdr = rIn.readLine();
            wOut.write(hdr); wOut.newLine();
            String ln;
            while ((ln=rIn.readLine())!=null) {
                String[] c = ln.split(",",-1);
                if (c[0].equals(oldId)&&c[1].equals(oldSd)&&
                    c[2].equals(oldEd)&&c[3].equals(oldRs)) {

                    wOut.write(oldId + "," + newSd + "," + newEd + "," + newRs);
                } else {
                    wOut.write(ln);
                }
                wOut.newLine();
            }
        } catch(IOException ex){ex.printStackTrace();}
        in.delete();
        out.renameTo(in);
        loadRequests();
    }

    private void openDelete() {
        int r = table.getSelectedRow();
        if (r<0) {
            JOptionPane.showMessageDialog(this,
                "Select a request to delete.","No selection",JOptionPane.WARNING_MESSAGE);
            return;
        }
        String id  = model.getValueAt(r,0).toString();
        String sd  = model.getValueAt(r,1).toString();
        String ed  = model.getValueAt(r,2).toString();
        String rs  = model.getValueAt(r,3).toString();
        int c = JOptionPane.showConfirmDialog(this,
            "Delete leave request for " + id + " from " + sd + " to " + ed + "?",
            "Confirm",JOptionPane.YES_NO_OPTION);
        if (c!=JOptionPane.YES_OPTION) return;

        File in  = new File("leaverequests.csv");
        File out = new File("leaverequests_tmp.csv");
        try (BufferedReader rIn = new BufferedReader(new FileReader(in));
             BufferedWriter wOut= new BufferedWriter(new FileWriter(out))) {

            String hdr = rIn.readLine();
            wOut.write(hdr); wOut.newLine();
            String ln;
            while ((ln=rIn.readLine())!=null) {
                if (!ln.equals(id + "," + sd + "," + ed + "," + rs)) {
                    wOut.write(ln);
                    wOut.newLine();
                }
            }
        } catch(IOException ex){ex.printStackTrace();}
        in.delete();
        out.renameTo(in);
        loadRequests();

        // Clear the form fields after deletion
        empField.setText("");
        startField.setText("");
        endField.setText("");
        reasonField.setText("");
    }
}