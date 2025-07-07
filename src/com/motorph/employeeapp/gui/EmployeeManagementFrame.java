package com.motorph.employeeapp.gui;

import com.motorph.employeeapp.model.Employee;
import com.motorph.employeeapp.repository.CsvEmployeeRepository;
import com.motorph.employeeapp.repository.EmployeeRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Swing-based GUI for managing employees and computing salary.
 */
public class EmployeeManagementFrame extends JFrame {
    private final EmployeeRepository repo;
    private final DefaultTableModel tableModel;
    private final JTable table;

    public EmployeeManagementFrame(String csvPath) {
        super("Employee Management");
        this.repo = new CsvEmployeeRepository(csvPath);

        // Table setup
        this.tableModel = new DefaultTableModel(
            new String[]{
                "Employee #", "Last Name", "First Name",
                "SSS #", "PhilHealth #", "TIN #", "Pag-IBIG #"
            },
            0
        );
        this.table = new JTable(tableModel);

        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton viewBtn    = new JButton("View and Update Record");
        JButton addBtn     = new JButton("Add Record");
        JButton deleteBtn  = new JButton("Delete Record");
        JButton payslipBtn = new JButton("Generate Payslip");
        JButton exitBtn    = new JButton("Exit");

        buttons.add(viewBtn);
        buttons.add(addBtn);
        buttons.add(deleteBtn);
        buttons.add(payslipBtn);
        buttons.add(exitBtn);
        add(buttons, BorderLayout.SOUTH);

        // Load data
        loadEmployees();

        // Hook up actions
        viewBtn.addActionListener(this::onView);
        addBtn.addActionListener(this::onAdd);
        deleteBtn.addActionListener(this::onDelete);
        payslipBtn.addActionListener(this::onGeneratePayslip);
        exitBtn.addActionListener(_ -> System.exit(0));

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
    }

    private void loadEmployees() {
        tableModel.setRowCount(0);
        try {
            List<Employee> all = repo.loadAll();
            System.out.println("Loaded " + all.size() + " employees from CSV.");
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
            JOptionPane.showMessageDialog(
                this,
                "Failed to load: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private Employee selectedEmployee() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        String id = tableModel.getValueAt(row, 0).toString();
        return findById(id);
    }

    private void onView(ActionEvent ev) {
        Employee e = selectedEmployee();
        if (e == null) {
            JOptionPane.showMessageDialog(
                this, "Please select an employee first.",
                "Warning", JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        new UpdateDialog(this, repo, e).setVisible(true);
        loadEmployees();
    }

    private void onAdd(ActionEvent ev) {
        new AddRecordDialog(this, repo).setVisible(true);
        loadEmployees();
    }

    private void onDelete(ActionEvent ev) {
        Employee e = selectedEmployee();
        if (e == null) {
            JOptionPane.showMessageDialog(
                this, "Please select an employee first.",
                "Warning", JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this record?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );
        if (choice == JOptionPane.YES_OPTION) {
            try {
                List<Employee> list = repo.loadAll();
                list.removeIf(emp -> emp.getId().equals(e.getId()));
                repo.saveAll(list);
                loadEmployees();
                JOptionPane.showMessageDialog(this, "Employee Record is deleted.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void onGeneratePayslip(ActionEvent ev) {
        Employee e = selectedEmployee();
        if (e == null) {
            JOptionPane.showMessageDialog(
                this, "Please select an employee first.",
                "Warning", JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        new PayslipDialog(this, e).setVisible(true);
    }

    private Employee findById(String id) {
        try {
            return repo.loadAll().stream()
                       .filter(emp -> emp.getId().equals(id))
                       .findFirst()
                       .orElse(null);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE
            );
            return null;
        }
    }

    public static void main(String[] args) {
        // Hard-coded path — adjust if necessary
        String path = "C:\\Users\\DELL\\Downloads\\Correct MotorPH Employee Data.csv";
        System.out.println("Loading CSV from: " + path);
        System.out.println("File exists? " + Files.exists(Paths.get(path)));

        SwingUtilities.invokeLater(() ->
            new EmployeeManagementFrame(path).setVisible(true)
        );
    }
}
