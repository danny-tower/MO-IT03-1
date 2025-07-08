package com.motorph.employeeapp.gui;

import com.motorph.employeeapp.model.Employee;
import com.motorph.employeeapp.pay.SalaryCalculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class PayslipDialog extends JDialog {

    private final Employee employee;

    // Tab 1 fields
    private final JTextField empIdField = new JTextField();
    private final JTextField lastNameField = new JTextField();
    private final JTextField firstNameField = new JTextField();
    private final JTextField bdayField = new JTextField();
    private final JTextField addressField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JTextField sssField = new JTextField();
    private final JTextField philHealthField = new JTextField();
    private final JTextField tinField = new JTextField();
    private final JTextField pagIbigField = new JTextField();

    // Tab 2 fields
    private final JComboBox<Integer> y1cb = new JComboBox<>();
    private final JComboBox<Integer> m1cb = new JComboBox<>();
    private final JComboBox<Integer> d1cb = new JComboBox<>();
    private final JComboBox<Integer> y2cb = new JComboBox<>();
    private final JComboBox<Integer> m2cb = new JComboBox<>();
    private final JComboBox<Integer> d2cb = new JComboBox<>();

    private final JTextField salaryPeriodField = new JTextField();
    private final JTextField salaryEarnedField = new JTextField();
    private final JTextField riceField = new JTextField();
    private final JTextField phoneAllowField = new JTextField();
    private final JTextField clothingAllowField = new JTextField();
    private final JTextField grossField = new JTextField();
    private final JTextField sssDedField = new JTextField();
    private final JTextField philHealthDedField = new JTextField();
    private final JTextField pagIbigDedField = new JTextField();
    private final JTextField taxDedField = new JTextField();
    private final JTextField totalDedField = new JTextField();
    private final JTextField netField = new JTextField();

    public PayslipDialog(Frame owner, Employee employee) {
        super(owner, "Payslip", true);
        this.employee = employee;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(owner);

        JTabbedPane tabs = new JTabbedPane();

        // --- Tab 1: Employee Details ---
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 8, 4, 8);
        c.gridx = 0; c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_END;

        addDetailField(detailsPanel, c, "Employee No.:", empIdField);
        addDetailField(detailsPanel, c, "Last Name:", lastNameField);
        addDetailField(detailsPanel, c, "First Name:", firstNameField);
        addDetailField(detailsPanel, c, "Birthday:", bdayField);
        addDetailField(detailsPanel, c, "Address:", addressField);
        addDetailField(detailsPanel, c, "Phone:", phoneField);
        addDetailField(detailsPanel, c, "SSS #:", sssField);
        addDetailField(detailsPanel, c, "PhilHealth #:", philHealthField);
        addDetailField(detailsPanel, c, "TIN #:", tinField);
        addDetailField(detailsPanel, c, "Pag-IBIG #:", pagIbigField);

        setEmployeeDetails();

        tabs.addTab("Employee Details", detailsPanel);

        // --- Tab 2: Payslip ---
        JPanel payslipPanel = new JPanel(new GridBagLayout());
        c.gridx = 0; c.gridy = 0;

        // Salary Period selectors
        payslipPanel.add(new JLabel("Start Date:"), c); c.gridx++;
        payslipPanel.add(y1cb, c); c.gridx++;
        payslipPanel.add(m1cb, c); c.gridx++;
        payslipPanel.add(d1cb, c);
        c.gridy++; c.gridx = 0;

        payslipPanel.add(new JLabel("End Date:"), c); c.gridx++;
        payslipPanel.add(y2cb, c); c.gridx++;
        payslipPanel.add(m2cb, c); c.gridx++;
        payslipPanel.add(d2cb, c);
        c.gridy++; c.gridx = 0;

        // Button
        JButton showBtn = new JButton("Show Payslip");
        showBtn.addActionListener(this::onShowPayslip);
        payslipPanel.add(showBtn, c); c.gridy++;

        // Salary info fields
        addPayslipField(payslipPanel, c, "Salary Period:", salaryPeriodField);
        addPayslipField(payslipPanel, c, "Salary Earned:", salaryEarnedField);
        addPayslipField(payslipPanel, c, "Rice Allowance:", riceField);
        addPayslipField(payslipPanel, c, "Phone Allowance:", phoneAllowField);
        addPayslipField(payslipPanel, c, "Clothing Allowance:", clothingAllowField);
        addPayslipField(payslipPanel, c, "Gross:", grossField);
        addPayslipField(payslipPanel, c, "SSS:", sssDedField);
        addPayslipField(payslipPanel, c, "PhilHealth:", philHealthDedField);
        addPayslipField(payslipPanel, c, "Pag-IBIG:", pagIbigDedField);
        addPayslipField(payslipPanel, c, "Withholding Tax:", taxDedField);
        addPayslipField(payslipPanel, c, "Total Deductions:", totalDedField);
        addPayslipField(payslipPanel, c, "Net Salary:", netField);

        // Close button
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(_ -> dispose());
        c.gridx = 1; c.gridy++;
        payslipPanel.add(closeBtn, c);

        tabs.addTab("Payslip", payslipPanel);

        // Fill date selectors
        initDateSelectors();

        setContentPane(tabs);
    }

    private void setEmployeeDetails() {
        empIdField.setText(employee.getId());
        lastNameField.setText(employee.getLastName());
        firstNameField.setText(employee.getFirstName());
        bdayField.setText(employee.getBirthDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        addressField.setText(employee.getAddress());
        phoneField.setText(employee.getPhone());
        sssField.setText(employee.getSssNumber());
        philHealthField.setText(employee.getPhilHealthNumber());
        tinField.setText(employee.getTinNumber());
        pagIbigField.setText(employee.getPagIbigNumber());
        for (JTextField f : new JTextField[]{empIdField, lastNameField, firstNameField, bdayField, addressField, phoneField, sssField, philHealthField, tinField, pagIbigField}) {
            f.setEditable(false);
        }
    }

    private void initDateSelectors() {
        int thisYear = LocalDate.now().getYear();
        for (int y = thisYear - 1; y <= thisYear + 1; y++) {
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
        y1cb.setSelectedItem(thisYear);
        m1cb.setSelectedItem(LocalDate.now().getMonthValue());
        d1cb.setSelectedItem(1);
        y2cb.setSelectedItem(thisYear);
        m2cb.setSelectedItem(LocalDate.now().getMonthValue());
        d2cb.setSelectedItem(LocalDate.now().getDayOfMonth());
    }

    private void addDetailField(JPanel panel, GridBagConstraints c, String label, JTextField field) {
        c.gridx = 0; panel.add(new JLabel(label), c);
        c.gridx = 1; panel.add(field, c);
        c.gridy++;
    }

    private void addPayslipField(JPanel panel, GridBagConstraints c, String label, JTextField field) {
        c.gridx = 0; panel.add(new JLabel(label), c);
        c.gridx = 1; panel.add(field, c);
        field.setEditable(false);
        c.gridy++;
    }

    private void onShowPayslip(ActionEvent ev) {
        try {
            LocalDate start = LocalDate.of((int)y1cb.getSelectedItem(), (int)m1cb.getSelectedItem(), (int)d1cb.getSelectedItem());
            LocalDate end   = LocalDate.of((int)y2cb.getSelectedItem(), (int)m2cb.getSelectedItem(), (int)d2cb.getSelectedItem());

            if (end.isBefore(start)) {
                JOptionPane.showMessageDialog(this, "End date must be after start date.", "Invalid Range", JOptionPane.ERROR_MESSAGE);
                return;
            }

            YearMonth ym = YearMonth.of(end.getYear(), end.getMonthValue());

            // Calculations
            BigDecimal grossMonthly = employee.getGrossSemiMonthlyRate().multiply(BigDecimal.valueOf(2));
            BigDecimal earnings = SalaryCalculator.computeMonthlyPay(employee, ym);
            BigDecimal rice = employee.getRiceSubsidy();
            BigDecimal phone = employee.getPhoneAllowance();
            BigDecimal clothing = employee.getClothingAllowance();
            BigDecimal sssDed = SalaryCalculator.computeSssDeduction(grossMonthly);
            BigDecimal philDed = SalaryCalculator.computePhilHealthDeduction(grossMonthly);
            BigDecimal pagIbigDed = SalaryCalculator.computePagIbigDeduction(grossMonthly);
            BigDecimal taxDed = SalaryCalculator.computeWithholdingTax(earnings, sssDed, philDed, pagIbigDed);

            BigDecimal totalDed = sssDed.add(philDed).add(pagIbigDed).add(taxDed);
            BigDecimal netSalary = earnings.subtract(totalDed);

            // Set fields
            salaryPeriodField.setText(start + " - " + end);
            salaryEarnedField.setText(earnings.toPlainString());
            riceField.setText(rice.toPlainString());
            phoneAllowField.setText(phone.toPlainString());
            clothingAllowField.setText(clothing.toPlainString());
            grossField.setText(grossMonthly.toPlainString());
            sssDedField.setText(sssDed.toPlainString());
            philHealthDedField.setText(philDed.toPlainString());
            pagIbigDedField.setText(pagIbigDed.toPlainString());
            taxDedField.setText(taxDed.toPlainString());
            totalDedField.setText(totalDed.toPlainString());
            netField.setText(netSalary.toPlainString());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date selection.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
