package com.motorph.employeeapp.gui;

import com.motorph.employeeapp.model.Employee;
import com.motorph.employeeapp.pay.SalaryCalculator;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class PayslipDialog extends JDialog {
    private final Employee employee;

    // Date selectors
    private final JComboBox<Integer> y1cb = new JComboBox<>();
    private final JComboBox<Integer> m1cb = new JComboBox<>();
    private final JComboBox<Integer> d1cb = new JComboBox<>();
    private final JComboBox<Integer> y2cb = new JComboBox<>();
    private final JComboBox<Integer> m2cb = new JComboBox<>();
    private final JComboBox<Integer> d2cb = new JComboBox<>();

    // Output fields
    private final JTextField salaryPeriodField   = new JTextField(20);
    private final JTextField salaryEarnedField   = new JTextField(20);
    private final JTextField riceField           = new JTextField(20);
    private final JTextField phoneAllowField     = new JTextField(20);
    private final JTextField clothingAllowField  = new JTextField(20);
    private final JTextField grossField          = new JTextField(20);
    private final JTextField sssDedField         = new JTextField(20);
    private final JTextField philHealthDedField  = new JTextField(20);
    private final JTextField pagIbigDedField     = new JTextField(20);
    private final JTextField taxDedField         = new JTextField(20);
    private final JTextField totalDedField       = new JTextField(20);
    private final JTextField netField            = new JTextField(20);

    public PayslipDialog(Frame owner, Employee employee) {
        super(owner, "Payslip", true);
        this.employee = employee;
        initDateSelectors();
        buildUI();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initDateSelectors() {
        int now = LocalDate.now().getYear();
        for (int y = now - 1; y <= now + 1; y++) {
            y1cb.addItem(y);
            y2cb.addItem(y);
        }
        for (int m = 1; m <= 12; m++) {
            m1cb.addItem(m);
            m2cb.addItem(m);
        }
        for (int d = 1; d <= 31; d++) {
            d1cb.addItem(d);
            d2cb.addItem(d);
        }
        y1cb.setSelectedItem(now);
        m1cb.setSelectedItem(LocalDate.now().getMonthValue());
        d1cb.setSelectedItem(1);
        y2cb.setSelectedItem(now);
        m2cb.setSelectedItem(LocalDate.now().getMonthValue());
        d2cb.setSelectedItem(LocalDate.now().getDayOfMonth());
    }

    private void buildUI() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Employee Details", createDetailsPanel());
        tabs.addTab("Payslip", buildPayslipPanel());
        setContentPane(tabs);
    }

    private JPanel createDetailsPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);
        c.anchor = GridBagConstraints.WEST;

        String[] labels = {
            "Employee #:", "Last Name:", "First Name:",
            "Birthday:", "Address:", "Phone:",
            "SSS #:", "PhilHealth #:", "TIN #:", "Pag-IBIG #:"
        };
        String[] values = {
            employee.getId(),
            employee.getLastName(),
            employee.getFirstName(),
            employee.getBirthDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
            employee.getAddress(),
            employee.getPhone(),
            employee.getSssNumber(),
            employee.getPhilHealthNumber(),
            employee.getTinNumber(),
            employee.getPagIbigNumber()
        };

        for (int i = 0; i < labels.length; i++) {
            c.gridx = 0; c.gridy = i;
            form.add(new JLabel(labels[i]), c);
            c.gridx = 1;
            JTextField tf = new JTextField(values[i], 20);
            tf.setEditable(false);
            form.add(tf, c);
        }
        return form;
    }

    private JPanel buildPayslipPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.anchor = GridBagConstraints.WEST;
        int row = 0;

        // Start Date
        c.gridx = 0; c.gridy = row; p.add(new JLabel("Start Date:"), c);
        c.gridx = 1; p.add(y1cb, c);
        c.gridx = 2; p.add(m1cb, c);
        c.gridx = 3; p.add(d1cb, c);

        // End Date
        row++; c.gridy = row; c.gridx = 0; p.add(new JLabel("End Date:"), c);
        c.gridx = 1; p.add(y2cb, c);
        c.gridx = 2; p.add(m2cb, c);
        c.gridx = 3; p.add(d2cb, c);

        // Show button
        row++; c.gridy = row; c.gridx = 0; c.gridwidth = 4;
        JButton show = new JButton("Show Payslip");
        show.addActionListener(_ -> onShowPayslip());
        p.add(show, c);
        c.gridwidth = 1;

        // Output labels & fields
        String[] labels = {
            "Salary Period:", "Salary Earned:", "Rice Allowance:",
            "Phone Allowance:", "Clothing Allowance:", "Gross:",
            "SSS Deduction:", "PhilHealth Deduction:", "Pag-IBIG Deduction:",
            "Withholding Tax:", "Total Deductions:", "Net Salary:"
        };
        JTextField[] fields = {
            salaryPeriodField, salaryEarnedField, riceField,
            phoneAllowField, clothingAllowField, grossField,
            sssDedField, philHealthDedField, pagIbigDedField,
            taxDedField, totalDedField, netField
        };

        for (int i = 0; i < labels.length; i++) {
            row++;
            c.gridy = row; c.gridx = 0;
            p.add(new JLabel(labels[i]), c);
            c.gridx = 1; c.gridwidth = 3;
            fields[i].setEditable(false);
            p.add(fields[i], c);
            c.gridwidth = 1;
        }
        return p;
    }

    private void onShowPayslip() {
        // 1) Gather dates
        LocalDate start = LocalDate.of(
            (int) y1cb.getSelectedItem(),
            (int) m1cb.getSelectedItem(),
            (int) d1cb.getSelectedItem()
        );
        LocalDate end = LocalDate.of(
            (int) y2cb.getSelectedItem(),
            (int) m2cb.getSelectedItem(),
            (int) d2cb.getSelectedItem()
        );
        if (end.isBefore(start)) {
            JOptionPane.showMessageDialog(this,
                "End date must be on or after start.",
                "Invalid Range", JOptionPane.ERROR_MESSAGE);
            return;
        }
        YearMonth ym = YearMonth.of(end.getYear(), end.getMonth());

        // 2) Salary Period
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d, yyyy");
        salaryPeriodField.setText(start.format(fmt) + " – " + end.format(fmt));

        // 3) Gross monthly = semi × 2
        BigDecimal grossMonthly = employee.getGrossSemiMonthlyRate()
                                          .multiply(BigDecimal.valueOf(2));
        grossField.setText(grossMonthly.toPlainString());

        // 4) Earned = gross + allowances
        BigDecimal earned = SalaryCalculator.computeMonthlyPay(employee, ym);
        salaryEarnedField.setText(earned.toPlainString());

        // 5) Allowances
        riceField.setText(employee.getRiceSubsidy().toPlainString());
        phoneAllowField.setText(employee.getPhoneAllowance().toPlainString());
        clothingAllowField.setText(employee.getClothingAllowance().toPlainString());

        // 6) Deductions on gross
        BigDecimal sssDed   = SalaryCalculator.computeSssDeduction(grossMonthly);
        BigDecimal philDed  = SalaryCalculator.computePhilHealthDeduction(grossMonthly);
        BigDecimal pgbDed   = SalaryCalculator.computePagIbigDeduction(grossMonthly);
        sssDedField.setText(sssDed.toPlainString());
        philHealthDedField.setText(philDed.toPlainString());
        pagIbigDedField.setText(pgbDed.toPlainString());

        // 7) Withholding tax
        BigDecimal taxDed   = SalaryCalculator.computeWithholdingTax(
            earned, sssDed, philDed, pgbDed
        );
        taxDedField.setText(taxDed.toPlainString());

        // 8) Total & Net
        BigDecimal totalDed = sssDed.add(philDed).add(pgbDed).add(taxDed);
        totalDedField.setText(totalDed.toPlainString());
        BigDecimal netSalary = earned.subtract(totalDed);
        netField.setText(netSalary.toPlainString());
    }
}
