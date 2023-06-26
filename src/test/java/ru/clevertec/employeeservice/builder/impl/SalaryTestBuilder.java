package ru.clevertec.employeeservice.builder.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.employeeservice.builder.TestBuilder;
import ru.clevertec.employeeservice.entity.Employee;
import ru.clevertec.employeeservice.entity.Payroll;
import ru.clevertec.employeeservice.entity.Salary;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aSalary")
public class SalaryTestBuilder implements TestBuilder<Salary> {

    private Long id = 0L;

    private BigDecimal amount = BigDecimal.ZERO;

    private Date startDate = Date.valueOf(LocalDate.now());

    private Date endDate = null;

    private boolean currentSalary = true;

    private Employee employee = EmployeeTestBuilder.anEmployee().build();

    private List<Payroll> payrolls = new ArrayList<>();

    @Override
    public Salary build() {
        Salary salary = new Salary();

        salary.setId(id);
        salary.setAmount(amount);
        salary.setStartDate(startDate);
        salary.setEndDate(endDate);
        salary.setCurrentSalary(currentSalary);
        salary.setEmployee(employee);
        salary.setPayrolls(payrolls);

        return salary;
    }
}

