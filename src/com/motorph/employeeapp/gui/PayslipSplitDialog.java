// PayslipSplitDialog.java
package com.motorph.employeeapp.gui;

import com.motorph.employeeapp.model.Employee;
import com.motorph.employeeapp.pay.SalaryCalculator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class PayslipSplitDialog extends JDialog {
    private final Employee emp;

    // left fields...
    private final JTextField idF  = new JTextField(15);
    private final JTextField lnF  = new JTextField(15);
    private final JTextField fnF  = new JTextField(15);
    private final JTextField bdF  = new JTextField(15);
    private final JTextField addr = new JTextField(20);
    private final JTextField phF  = new JTextField(15);
    private final JTextField sssF = new JTextField(15);
    private final JTextField philF= new JTextField(15);
    private final JTextField tinF = new JTextField(15);
    private final JTextField pagF = new JTextField(15);
    private final JTextField stF  = new JTextField(15);
    private final JTextField posF = new JTextField(15);
    private final JTextField supF = new JTextField(15);

    // right controls
    private final JComboBox<String> monthCB = new JComboBox<>();
    private final JTextField periodF   = new JTextField(25);
    private final JTextField earnedF   = new JTextField(25);
    private final JTextField riceF     = new JTextField(25);
    private final JTextField phAllF    = new JTextField(25);
    private final JTextField clAllF    = new JTextField(25);
    private final JTextField grossF    = new JTextField(25);
    private final JTextField sssDedF   = new JTextField(25);
    private final JTextField philDedF  = new JTextField(25);
    private final JTextField pagDedF   = new JTextField(25);
    private final JTextField taxDedF   = new JTextField(25);
    private final JTextField totDedF   = new JTextField(25);
    private final JTextField netF      = new JTextField(25);

    public PayslipSplitDialog(Frame owner, Employee e) {
        super(owner, "Employee Payslip", true);
        this.emp = e;

        // fill left
        idF .setText(e.getId());
        lnF .setText(e.getLastName());
        fnF .setText(e.getFirstName());
        bdF .setText(e.getBirthDate().format(
            DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        addr.setText(e.getAddress());
        phF .setText(e.getPhone());
        sssF.setText(e.getSssNumber());
        philF.setText(e.getPhilHealthNumber());
        tinF.setText(e.getTinNumber());
        pagF.setText(e.getPagIbigNumber());
        stF .setText(e.getStatus());
        posF.setText(e.getPosition());
        supF.setText(e.getSupervisor());

        for(var f: new JTextField[]{idF,lnF,fnF,bdF,addr,phF,sssF,philF,tinF,pagF,stF,posF,supF})
            f.setEditable(false);

        // fill months dropdown
        String[] m = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        for(int i=0;i<12;i++) monthCB.addItem(m[i]);
        monthCB.setSelectedIndex(LocalDate.now().getMonthValue()-1);

        // all outputs non‐editable
        for(var f: new JTextField[]{
            periodF,earnedF,riceF,phAllF,clAllF,grossF,
            sssDedF,philDedF,pagDedF,taxDedF,totDedF,netF
        }) {
            f.setEditable(false);
            f.setPreferredSize(new Dimension(200,24));
        }

        JSplitPane split = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            buildDetailPane(),
            buildCalculatorPane()
        );
        split.setResizeWeight(0.5);
        split.setDividerLocation(350);

        getContentPane().add(split);
        pack();
        setLocationRelativeTo(owner);
    }

    private JPanel buildDetailPane() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(new EmptyBorder(12,12,12,12));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.anchor = GridBagConstraints.LINE_END;

        String[] labels = {
            "Employee No.:","Last Name:","First Name:","Birthday:",
            "Address:","Phone:","SSS #:","PhilHealth #:","TIN #:","Pag-IBIG #:",
            "Status:","Position:","Supervisor:"
        };
        JTextField[] fields = {
            idF,lnF,fnF,bdF,addr,phF,sssF,philF,tinF,pagF,stF,posF,supF
        };

        for(int i=0;i<labels.length;i++){
            c.gridy=i; c.gridx=0; p.add(new JLabel(labels[i]),c);
            c.gridx=1; p.add(fields[i],c);
        }
        return p;
    }

    private JPanel buildCalculatorPane() {
    JPanel p = new JPanel(new GridBagLayout());
    p.setBorder(new EmptyBorder(12,12,12,12));
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(6,6,6,6);
    c.anchor = GridBagConstraints.WEST;

    // month chooser
    c.gridy = 0; c.gridx = 0;
    p.add(new JLabel("Month:"), c);
    c.gridx = 1;
    p.add(monthCB, c);

    // compute button
    c.gridx = 2;
    JButton compute = new JButton("Compute");
    compute.setBackground(new Color(45,137,239));
    compute.setForeground(Color.WHITE);
    compute.setFocusPainted(false);
    compute.setBorder(new EmptyBorder(8,16,8,16));
    compute.addActionListener(_ -> doCompute());
    p.add(compute, c);

    // prepare labels & fields
    String[] lbls = {
        "Salary Period:", "Salary Earned:", "Rice Allowance:",
        "Phone Allowance:", "Clothing Allowance:", "Gross:",
        "SSS Deduction:", "PhilHealth Deduction:", "Pag-IBIG Deduction:",
        "Withholding Tax:", "Total Deductions:", "Net Salary:"
    };
    JTextField[] flds = {
        periodF, earnedF, riceF, phAllF, clAllF, grossF,
        sssDedF, philDedF, pagDedF, taxDedF, totDedF, netF
    };

    // make them wide and resizable
    for (JTextField f : flds) {
        f.setColumns(20);
        f.setEditable(false);
    }

    // now lay them out
    for (int i = 0; i < lbls.length; i++) {
        c.gridy = i + 1;
        c.gridx = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        p.add(new JLabel(lbls[i]), c);

        c.gridx = 1;
        c.gridwidth = 2;                 // span two columns
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;                 // let it stretch
        p.add(flds[i], c);

        // reset so next row starts clean
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
    }

    return p;
}

    private void doCompute() {
        // pick the chosen month (1–12)
        int m = monthCB.getSelectedIndex()+1;
        YearMonth ym = YearMonth.of(LocalDate.now().getYear(), m);

        // 1) Gross monthly
        BigDecimal gross = emp.getGrossSemiMonthlyRate()
                              .multiply(BigDecimal.valueOf(2));
        grossF.setText(gross.toPlainString());

        // 2) Salary earned = gross + allowances
        BigDecimal earned = SalaryCalculator.computeMonthlyPay(emp, ym);
        earnedF.setText(earned.toPlainString());

        // 3) allowances
        riceF.setText(emp.getRiceSubsidy().toPlainString());
        phAllF.setText(emp.getPhoneAllowance().toPlainString());
        clAllF.setText(emp.getClothingAllowance().toPlainString());

        // 4) deductions
        BigDecimal sssDed   = SalaryCalculator.computeSssDeduction(gross);
        BigDecimal philDed  = SalaryCalculator.computePhilHealthDeduction(gross);
        BigDecimal pagDed   = SalaryCalculator.computePagIbigDeduction(gross);
        sssDedF.setText(sssDed.toPlainString());
        philDedF.setText(philDed.toPlainString());
        pagDedF.setText(pagDed.toPlainString());

        // 5) tax
        BigDecimal tax      = SalaryCalculator.computeWithholdingTax(
                                  earned, sssDed, philDed, pagDed);
        taxDedF.setText(tax.toPlainString());

        // 6) totals & net
        BigDecimal tot = sssDed.add(philDed).add(pagDed).add(tax);
        totDedF.setText(tot.toPlainString());
        netF.setText(earned.subtract(tot).toPlainString());

        // 7) period label: full month name
        String monName = ym.getMonth().name().substring(0,1)
                         + ym.getMonth().name().substring(1).toLowerCase();
        periodF.setText(monName + " " + ym.getYear());
    }
}
