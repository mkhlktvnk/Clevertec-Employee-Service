package ru.clevertec.employeeservice.builder.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.employeeservice.builder.TestBuilder;
import ru.clevertec.employeeservice.entity.Payroll;
import ru.clevertec.employeeservice.entity.Salary;

import java.sql.Date;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aPayroll")
public class PayrollTestBuilder implements TestBuilder<Payroll> {

    private Long id;

    private Date paymentDate;

    private Salary salary;

    @Override
    public Payroll build() {
        Payroll payroll = new Payroll();

        payroll.setId(id);
        payroll.setPaymentDate(paymentDate);
        payroll.setSalary(salary);

        return payroll;
    }
}
