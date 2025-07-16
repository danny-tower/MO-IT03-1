// EmployeeManagementFrame.java
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
import java.util.List;

public class EmployeeManagementFrame extends JFrame {
    private final EmployeeRepository repo;
    private final JTable table;
    private final DefaultTableModel model;

    public EmployeeManagementFrame(EmployeeRepository repo) {
        super("Employee Management");
        this.repo = repo;

        // --- build table ---
        String[] cols = {
            "Employee #", "Last Name", "First Name",
            "SSS #", "PhilHealth #", "TIN #", "Pag-IBIG #"
        };
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // banded rows
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                JTable t, Object v, boolean sel, boolean f, int row, int col
            ) {
                Component c = super.getTableCellRendererComponent(t,v,sel,f,row,col);
                if(!sel) c.setBackground(row%2==0?Color.WHITE:new Color(245,245,245));
                return c;
            }
        });

        // --- build buttons ---
        JButton newBtn  = new JButton("New Employee");
        JButton delBtn  = new JButton("Delete Employee");
        JButton viewBtn = new JButton("View Employee");

        for(JButton b: List.of(newBtn, delBtn, viewBtn)) {
            b.setBackground(new Color(45,137,239));
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.setBorder(new EmptyBorder(8,16,8,16));
        }

        newBtn .addActionListener(_ -> onAdd());
        delBtn .addActionListener(_ -> onDelete());
        viewBtn.addActionListener(_ -> onView());

        JPanel btns = new JPanel();
        btns.setLayout(new BoxLayout(btns,BoxLayout.Y_AXIS));
        btns.setBorder(new EmptyBorder(10,10,10,10));
        btns.add(newBtn);   btns.add(Box.createVerticalStrut(8));
        btns.add(delBtn);   btns.add(Box.createVerticalStrut(8));
        btns.add(viewBtn);

        setLayout(new BorderLayout());
        add(btns, BorderLayout.WEST);
        add(new JScrollPane(table), BorderLayout.CENTER);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900,550);
        setLocationRelativeTo(null);

        loadTable();
    }

    private void loadTable() {
        model.setRowCount(0);
        try {
            for(var e: repo.loadAll()) {
                model.addRow(new Object[]{
                    e.getId(), e.getLastName(), e.getFirstName(),
                    e.getSssNumber(), e.getPhilHealthNumber(),
                    e.getTinNumber(), e.getPagIbigNumber()
                });
            }
        } catch(IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Load failed: "+ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onAdd() {
        new AddRecordDialog(this, repo, this::loadTable).setVisible(true);
    }

    private void onDelete() {
        int r = table.getSelectedRow();
        if(r<0) { JOptionPane.showMessageDialog(this,"Select one"); return; }
        if(JOptionPane.showConfirmDialog(this,
            "Delete this employee?", "Confirm", JOptionPane.YES_NO_OPTION)
            != JOptionPane.YES_OPTION) return;

        String id = (String)model.getValueAt(r,0);
        try {
            var all = repo.loadAll();
            all.removeIf(e->e.getId().equals(id));
            repo.saveAll(all);
            loadTable();
        } catch(IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Delete failed: "+ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onView() {
        int r = table.getSelectedRow();
        if(r<0) { JOptionPane.showMessageDialog(this,"Select one"); return; }
        String id = (String)model.getValueAt(r,0);
        try {
            for(var e: repo.loadAll()) {
                if(e.getId().equals(id)) {
                    new PayslipSplitDialog(this,e).setVisible(true);
                    return;
                }
            }
        } catch(IOException ex){
            JOptionPane.showMessageDialog(this,
                "Cannot open: "+ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var path = "data"+File.separator+"MotorPH Employee Record.csv";
            EmployeeRepository repo = new CsvEmployeeRepository(path);
            new EmployeeManagementFrame(repo).setVisible(true);
        });
    }
}
