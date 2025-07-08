package com.motorph.employeeapp.gui;

import com.motorph.employeeapp.model.Employee;
import com.motorph.employeeapp.repository.CsvEmployeeRepository;
import com.motorph.employeeapp.repository.EmployeeRepository;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Date;
import java.io.IOException;

public class EmployeeManagementFrame extends JFrame {
    private EmployeeRepository repo;

    // Table and model
    private JTable table;
    private DefaultTableModel tableModel;

    public EmployeeManagementFrame(EmployeeRepository repo) {
        super("Employee Management");
        this.repo = repo;

        initTable();
        initLayout();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);
    }

    private void initTable() {
        String[] columns = {
            "Employee #", "Last Name", "First Name", "SSS #", "Philhealth #", "TIN #", "Pag-ibig #"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        loadTableData();

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        try {
            List<Employee> all = repo.loadAll();
            for (Employee emp : all) {
                tableModel.addRow(new Object[]{
                    emp.getId(),
                    emp.getLastName(),
                    emp.getFirstName(),
                    emp.getSssNumber(),
                    emp.getPhilHealthNumber(),
                    emp.getTinNumber(),
                    emp.getPagIbigNumber()
                });
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load employees: " + e.getMessage());
        }
    }

    private void initLayout() {
        setLayout(new BorderLayout());

        // Table with scroll
        JScrollPane tableScroll = new JScrollPane(table);

        // Button panel on the left
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setPreferredSize(new Dimension(240, 0));

        JButton viewBtn = new JButton("View and Update Record");
        JButton addBtn = new JButton("Add Record");
        JButton deleteBtn = new JButton("Delete Record");
        JButton payslipBtn = new JButton("Generate Payslip");
        JButton exitBtn = new JButton("Exit");

        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(viewBtn);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(addBtn);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(deleteBtn);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(payslipBtn);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(exitBtn);

        // Action Listeners
        viewBtn.addActionListener(_ -> onViewUpdate());
        addBtn.addActionListener(_-> onAdd());
        deleteBtn.addActionListener(_ -> onDelete());
        payslipBtn.addActionListener(_ -> onPayslip());
        exitBtn.addActionListener(_ -> System.exit(0));

        add(buttonPanel, BorderLayout.WEST);
        add(tableScroll, BorderLayout.CENTER);
    }

    private void onViewUpdate() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a record first.");
            return;
        }
        String id = (String) tableModel.getValueAt(row, 0);
        try {
            List<Employee> all = repo.loadAll();
            for (Employee emp : all) {
                if (emp.getId().equals(id)) {
                    new UpdateDialog(this, repo, emp, this::loadTableData).setVisible(true);
                    break;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed: " + e.getMessage());
        }
    }

    private void onAdd() {
        new AddRecordDialog(this, repo, this::loadTableData).setVisible(true);
    }

    private void onDelete() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a record to delete.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this employee?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        String id = (String) tableModel.getValueAt(row, 0);
        try {
            List<Employee> all = repo.loadAll();
            all.removeIf(emp -> emp.getId().equals(id));
            repo.saveAll(all);
            loadTableData();
            JOptionPane.showMessageDialog(this, "Record deleted.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed: " + e.getMessage());
        }
    }

    private void onPayslip() {
        JOptionPane.showMessageDialog(this, "Generate Payslip feature coming soon!", "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                CsvEmployeeRepository repo = new CsvEmployeeRepository("C:\\Users\\DELL\\Downloads\\Correct MotorPH Employee Data.csv");
                new EmployeeManagementFrame(repo).setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to start application: " + ex.getMessage());
            }
        });
    }
}
