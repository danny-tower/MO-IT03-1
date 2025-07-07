package com.motorph.employeeapp.gui;

import com.motorph.employeeapp.model.Employee;
import com.motorph.employeeapp.pay.SalaryCalculator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.stream.IntStream;


public class PayslipDialog extends JDialog {
    private final Employee employee;

    private final JComboBox<Integer> dayStartBox   = new JComboBox<>();
    private final JComboBox<Integer> monthStartBox = new JComboBox<>();
    private final JComboBox<Integer> yearStartBox  = new JComboBox<>();
    private final JComboBox<Integer> dayEndBox     = new JComboBox<>();
    private final JComboBox<Integer> monthEndBox   = new JComboBox<>();
    private final JComboBox<Integer> yearEndBox    = new JComboBox<>();

    private final JPanel earningsPanel   = new JPanel(new GridLayout(0,1,4,4));
    private final JPanel deductionsPanel= new JPanel(new GridLayout(0,1,4,4));

    public PayslipDialog(Frame owner, Employee employee) {
        super(owner, "Generate Payslip", true);
        this.employee = employee;

        initSelectors();
        initLayout();

        pack();
        setLocationRelativeTo(owner);
    }

    private void initSelectors() {
        int currentYear = YearMonth.now().getYear();
        IntStream.rangeClosed(1, 31).forEach(dayStartBox::addItem);
        IntStream.rangeClosed(1, 12).forEach(monthStartBox::addItem);
        IntStream.rangeClosed(currentYear - 5, currentYear + 1).forEach(yearStartBox::addItem);

        IntStream.rangeClosed(1, 31).forEach(dayEndBox::addItem);
        IntStream.rangeClosed(1, 12).forEach(monthEndBox::addItem);
        IntStream.rangeClosed(currentYear - 5, currentYear + 1).forEach(yearEndBox::addItem);

        LocalDate today = LocalDate.now();
        dayStartBox.setSelectedItem(today.getDayOfMonth());
        monthStartBox.setSelectedItem(today.getMonthValue());
        yearStartBox.setSelectedItem(today.getYear());
        dayEndBox.setSelectedItem(today.getDayOfMonth());
        monthEndBox.setSelectedItem(today.getMonthValue());
        yearEndBox.setSelectedItem(today.getYear());
    }

    private void initLayout() {
        JPanel selectorPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);

        // Start date
        c.gridx = 0; c.gridy = 0;
        selectorPanel.add(new JLabel("Start Date:"), c);
        c.gridx = 1; selectorPanel.add(monthStartBox, c);
        c.gridx = 2; selectorPanel.add(dayStartBox, c);
        c.gridx = 3; selectorPanel.add(yearStartBox, c);

        // End date
        c.gridx = 0; c.gridy = 1;
        selectorPanel.add(new JLabel("End Date:"), c);
        c.gridx = 1; selectorPanel.add(monthEndBox, c);
        c.gridx = 2; selectorPanel.add(dayEndBox, c);
        c.gridx = 3; selectorPanel.add(yearEndBox, c);

        JButton showBtn = new JButton("Show Payslip");
        showBtn.addActionListener(this::onShowPayslip);

        JPanel top = new JPanel(new BorderLayout(8,8));
        top.add(selectorPanel, BorderLayout.CENTER);
        top.add(showBtn, BorderLayout.EAST);

        JPanel output = new JPanel(new GridLayout(1,2,10,0));
        output.add(wrapPanel("Earnings", earningsPanel));
        output.add(wrapPanel("Deductions", deductionsPanel));

        getContentPane().setLayout(new BorderLayout(10,10));
        getContentPane().add(top, BorderLayout.NORTH);
        getContentPane().add(output, BorderLayout.CENTER);
    }

    private JPanel wrapPanel(String title, JPanel content) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder(title));
        p.add(content, BorderLayout.CENTER);
        return p;
    }

    private void onShowPayslip(ActionEvent ev) {
        LocalDate start = LocalDate.of(
            (Integer)yearStartBox.getSelectedItem(),
            (Integer)monthStartBox.getSelectedItem(),
            (Integer)dayStartBox.getSelectedItem()
        );
        LocalDate end = LocalDate.of(
            (Integer)yearEndBox.getSelectedItem(),
            (Integer)monthEndBox.getSelectedItem(),
            (Integer)dayEndBox.getSelectedItem()
        );

        if (end.isBefore(start)) {
            JOptionPane.showMessageDialog(
                this, "Select a valid date range.",
                "Error", JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        YearMonth ym = YearMonth.of(start.getYear(), start.getMonthValue());
        BigDecimal grossMonthly    = employee.getGrossSemiMonthlyRate().multiply(BigDecimal.valueOf(2));
        BigDecimal totalEarnings   = SalaryCalculator.computeMonthlyPay(employee, ym);

        BigDecimal sssDeduction       = SalaryCalculator.computeSssDeduction(grossMonthly);
        BigDecimal philHealthDeduction= SalaryCalculator.computePhilHealthDeduction(grossMonthly);
        BigDecimal pagIbigDeduction   = SalaryCalculator.computePagIbigDeduction(grossMonthly);
        BigDecimal taxDeduction       = SalaryCalculator.computeWithholdingTax(
                                          totalEarnings,
                                          sssDeduction,
                                          philHealthDeduction,
                                          pagIbigDeduction
                                      );

        // Earnings
        earningsPanel.removeAll();
        earningsPanel.add(new JLabel("Gross Salary: " + grossMonthly));
        earningsPanel.add(new JLabel("Rice Subsidy: " + employee.getRiceSubsidy()));
        earningsPanel.add(new JLabel("Phone Allowance: " + employee.getPhoneAllowance()));
        earningsPanel.add(new JLabel("Clothing Allowance: " + employee.getClothingAllowance()));
        earningsPanel.add(new JLabel("Total Earnings: " + totalEarnings));

        // Deductions
        BigDecimal totalDeductions = sssDeduction
            .add(philHealthDeduction)
            .add(pagIbigDeduction)
            .add(taxDeduction);

        deductionsPanel.removeAll();
        deductionsPanel.add(new JLabel("SSS: " + sssDeduction));
        deductionsPanel.add(new JLabel("PhilHealth: " + philHealthDeduction));
        deductionsPanel.add(new JLabel("Pag-IBIG: " + pagIbigDeduction));
        deductionsPanel.add(new JLabel("Withholding Tax: " + taxDeduction));
        deductionsPanel.add(new JLabel("Total Deductions: " + totalDeductions));
        deductionsPanel.add(new JLabel("Net Salary: " + totalEarnings.subtract(totalDeductions)));

        revalidate();
        repaint();
    }
}
        }
    }
}
