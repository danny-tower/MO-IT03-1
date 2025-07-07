package com.motorph.employeeapp.pay;

import com.motorph.employeeapp.model.Employee;
import java.math.BigDecimal;
import java.time.YearMonth;

/**
 * Utility for computing employee earnings and deductions.
 */
public class SalaryCalculator {
    /** Compute total earnings for the month. */
    public static BigDecimal computeMonthlyPay(Employee emp, YearMonth month) {
        BigDecimal grossMonthly = emp.getGrossSemiMonthlyRate()
                                     .multiply(BigDecimal.valueOf(2L));
        return grossMonthly
            .add(emp.getRiceSubsidy())
            .add(emp.getPhoneAllowance())
            .add(emp.getClothingAllowance());
    }

    /** Placeholder: 4% SSS deduction on gross monthly. */
    public static BigDecimal computeSssDeduction(BigDecimal grossMonthly) {
        return grossMonthly.multiply(new BigDecimal("0.04"));
    }

    /** Placeholder: 2.75% PhilHealth deduction on gross monthly. */
    public static BigDecimal computePhilHealthDeduction(BigDecimal grossMonthly) {
        return grossMonthly.multiply(new BigDecimal("0.0275"));
    }

    /** Placeholder: 2% Pag-IBIG deduction on gross monthly. */
    public static BigDecimal computePagIbigDeduction(BigDecimal grossMonthly) {
        return grossMonthly.multiply(new BigDecimal("0.02"));
    }

    /** Placeholder: 10% withholding tax on (earnings − mandatory deductions). */
    public static BigDecimal computeWithholdingTax(
        BigDecimal earnings,
        BigDecimal sss,
        BigDecimal philHealth,
        BigDecimal pagIbig
    ) {
        BigDecimal taxable = earnings.subtract(sss).subtract(philHealth).subtract(pagIbig);
        return taxable.multiply(new BigDecimal("0.10"));
    }
}
