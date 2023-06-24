package ru.clevertec.employeeservice.builder.impl;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.employeeservice.builder.TestBuilder;
import ru.clevertec.employeeservice.entity.Bonus;
import ru.clevertec.employeeservice.entity.Employee;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aBonus")
public class BonusTestBuilder implements TestBuilder<Bonus> {

    private Long id = 1L;

    private BigDecimal amount = BigDecimal.ZERO;

    private String description = "";

    private Date paymentDate = Date.valueOf(LocalDate.now());

    private Employee employee = EmployeeTestBuilder.anEmployee().build();

    @Override
    public Bonus build() {
        Bonus bonus = new Bonus();

        bonus.setId(id);
        bonus.setAmount(amount);
        bonus.setDescription(description);
        bonus.setPaymentDate(paymentDate);
        bonus.setEmployee(employee);

        return bonus;
    }
}
