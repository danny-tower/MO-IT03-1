package com.motorph.employeeapp.gui;

import com.motorph.employeeapp.model.Employee;
import com.motorph.employeeapp.repository.CsvEmployeeRepository;
import com.motorph.employeeapp.repository.EmployeeRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class EmployeeManagementFrame extends JFrame {
    private final EmployeeRepository repo;
    private final JTable table;
    private final DefaultTableModel tableModel;

    public EmployeeManagementFrame(EmployeeRepository repo) {
        super("Employee Management");
        this.repo = repo;

        setLayout(new BorderLayout());

        // — 1) Table setup —
        String[] cols = {
            "Employee #", "Last Name", "First Name",
            "SSS #", "PhilHealth #", "TIN #", "Pag-IBIG #"
        };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // banded row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                JTable tbl, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

                Component c = super.getTableCellRendererComponent(
                    tbl, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    c.setBackground((row % 2 == 0)
                        ? Color.WHITE
                        : new Color(245, 245, 245));
                }
                return c;
            }
        });

        // — 2) Buttons —
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
        btnPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        btnPanel.setPreferredSize(new Dimension(200, 0));

        JButton viewBtn    = new JButton("View / Update");
        JButton addBtn     = new JButton("Add Record");
        JButton deleteBtn  = new JButton("Delete Record");
        JButton payslipBtn = new JButton("Generate Payslip");
        JButton exitBtn    = new JButton("Exit");

        // accent colors
        viewBtn   .setBackground(new Color(45, 137, 239)); viewBtn   .setForeground(Color.WHITE);
        addBtn    .setBackground(new Color( 67, 183,  89)); addBtn    .setForeground(Color.WHITE);
        deleteBtn .setBackground(new Color(229,  57,  53)); deleteBtn .setForeground(Color.WHITE);
        payslipBtn.setBackground(new Color(251, 192,  45)); payslipBtn.setForeground(Color.DARK_GRAY);
        exitBtn   .setBackground(new Color(158, 158, 158)); exitBtn   .setForeground(Color.WHITE);

        for (JButton b : List.of(viewBtn, addBtn, deleteBtn, payslipBtn, exitBtn)) {
            b.setFocusPainted(false);
            b.setBorder(new EmptyBorder(8, 16, 8, 16));
        }

        btnPanel.add(viewBtn);    btnPanel.add(Box.createVerticalStrut(8));
        btnPanel.add(addBtn);     btnPanel.add(Box.createVerticalStrut(8));
        btnPanel.add(deleteBtn);  btnPanel.add(Box.createVerticalStrut(8));
        btnPanel.add(payslipBtn); btnPanel.add(Box.createVerticalStrut(8));
        btnPanel.add(exitBtn);

        add(btnPanel, BorderLayout.WEST);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // hook actions
        viewBtn   .addActionListener(_ -> onViewUpdate());
        addBtn    .addActionListener(_ -> onAdd());
        deleteBtn .addActionListener(_ -> onDelete());
        payslipBtn.addActionListener(_ -> onPayslip());
        exitBtn   .addActionListener(_ -> System.exit(0));

        // frame settings
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);

        // load everything at startup
        loadTableData();
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        try {
            List<Employee> all = repo.loadAll();
            for (Employee e : all) {
                tableModel.addRow(new Object[]{
                    e.getId(),
                    e.getLastName(),
                    e.getFirstName(),
                    e.getSssNumber(),
                    e.getPhilHealthNumber(),
                    e.getTinNumber(),
                    e.getPagIbigNumber()
                });
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Failed to load data: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onViewUpdate() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Select a record first.");
            return;
        }
        String id = (String) tableModel.getValueAt(r, 0);
        try {
            for (Employee e : repo.loadAll()) {
                if (e.getId().equals(id)) {
                    new UpdateDialog(this, repo, e, this::loadTableData)
                        .setVisible(true);
                    return;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error opening record: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onAdd() {
        new AddRecordDialog(this, repo, this::loadTableData)
            .setVisible(true);
    }

    private void onDelete() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Select a record to delete.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this,
            "Delete this employee?", "Confirm",
            JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        String id = (String) tableModel.getValueAt(r, 0);
        try {
            List<Employee> all = repo.loadAll();
            all.removeIf(emp -> emp.getId().equals(id));
            repo.saveAll(all);
            loadTableData();
            JOptionPane.showMessageDialog(this, "Record deleted.");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error deleting record: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onPayslip() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Select a record first.");
            return;
        }
        String id = (String) tableModel.getValueAt(r, 0);
        try {
            for (Employee e : repo.loadAll()) {
                if (e.getId().equals(id)) {
                    new PayslipDialog(this, e).setVisible(true);
                    return;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error generating payslip: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // no prompt — use the checked-in CSV under data/
            String path = "data" + File.separator
                        + "MotorPH Employee Record.csv";
            EmployeeRepository repo = new CsvEmployeeRepository(path);
            new EmployeeManagementFrame(repo).setVisible(true);
        });
    }
}
