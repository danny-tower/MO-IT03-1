package com.scoopmeinnn.employeeapp.pay;

import com.scoopmeinnn.employeeapp.model.Employee;
import java.math.BigDecimal;
import java.time.YearMonth;

public class SalaryCalculator {
    /**
     * Compute the total pay for the given employee in the specified month.
     * Here we simply sum:
     *   = (grossSemiMonthlyRate * 2)
     *   + riceSubsidy
     *   + phoneAllowance
     *   + clothingAllowance
     *
     * You can extend this to pro‐rate for partial months, apply taxes, etc.
     */
    public static BigDecimal computeMonthlyPay(Employee e, YearMonth ym) {
        BigDecimal monthlyGross = e.getGrossSemiMonthlyRate()
                                   .multiply(BigDecimal.valueOf(2));
        return monthlyGross
            .add(e.getRiceSubsidy())
            .add(e.getPhoneAllowance())
            .add(e.getClothingAllowance());
    }
}
