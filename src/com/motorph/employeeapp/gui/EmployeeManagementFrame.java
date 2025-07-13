package com.motorph.employeeapp.gui;

import com.motorph.employeeapp.model.Employee;
import com.motorph.employeeapp.repository.CsvEmployeeRepository;
import com.motorph.employeeapp.repository.EmployeeRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class EmployeeManagementFrame extends JFrame {
    private final EmployeeRepository repo;
    private final JTable table;
    private final DefaultTableModel tableModel;

    public EmployeeManagementFrame(EmployeeRepository repo) {
        super("Employee Management");
        this.repo = repo;

        // --- build table model ---
        String[] columns = {
            "Employee #", "Last Name", "First Name",
            "SSS #", "PhilHealth #", "TIN #", "Pag-IBIG #"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // --- layout ---
        initLayout();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);

        // --- load data ---
        loadTableData();
    }

    private void initLayout() {
        setLayout(new BorderLayout());

        // left‐side buttons
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        btnPanel.setPreferredSize(new Dimension(200, 0));

        JButton viewBtn    = new JButton("View / Update");
        JButton addBtn     = new JButton("Add Record");
        JButton deleteBtn  = new JButton("Delete Record");
        JButton payslipBtn = new JButton("Generate Payslip");
        JButton exitBtn    = new JButton("Exit");

        viewBtn   .addActionListener(_ -> onViewUpdate());
        addBtn    .addActionListener(_ -> onAdd());
        deleteBtn .addActionListener(_ -> onDelete());
        payslipBtn.addActionListener(_ -> onPayslip());
        exitBtn   .addActionListener(_ -> System.exit(0));

        btnPanel.add(viewBtn);    btnPanel.add(Box.createVerticalStrut(8));
        btnPanel.add(addBtn);     btnPanel.add(Box.createVerticalStrut(8));
        btnPanel.add(deleteBtn);  btnPanel.add(Box.createVerticalStrut(8));
        btnPanel.add(payslipBtn); btnPanel.add(Box.createVerticalStrut(8));
        btnPanel.add(exitBtn);

        add(btnPanel, BorderLayout.WEST);
        add(new JScrollPane(table), BorderLayout.CENTER);
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
            JOptionPane.showMessageDialog(
                this,
                "Failed to load data:\n" + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void onViewUpdate() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a record first.");
            return;
        }
        String id = (String) tableModel.getValueAt(row, 0);
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
            JOptionPane.showMessageDialog(
                this,
                "Error opening record:\n" + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void onAdd() {
        new AddRecordDialog(this, repo, this::loadTableData)
            .setVisible(true);
    }

    private void onDelete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a record to delete.");
            return;
        }
        if (JOptionPane.showConfirmDialog(
            this,
            "Delete this employee?",
            "Confirm",
            JOptionPane.YES_NO_OPTION
        ) != JOptionPane.YES_OPTION) {
            return;
        }
        String id = (String) tableModel.getValueAt(row, 0);
        try {
            List<Employee> all = repo.loadAll();
            all.removeIf(e -> e.getId().equals(id));
            repo.saveAll(all);
            loadTableData();
            JOptionPane.showMessageDialog(this, "Record deleted.");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Error deleting record:\n" + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void onPayslip() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a record first.");
            return;
        }
        String id = (String) tableModel.getValueAt(row, 0);
        try {
            for (Employee e : repo.loadAll()) {
                if (e.getId().equals(id)) {
                    new PayslipDialog(this, e).setVisible(true);
                    return;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Error generating payslip:\n" + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**  
     * Now we load from the checked‐in CSV at /data/... so cloning the repo just works.  
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String path = "data/MotorPH Employee Record.csv";
            EmployeeRepository repo = new CsvEmployeeRepository(path);
            new EmployeeManagementFrame(repo).setVisible(true);
        });
    }
}
