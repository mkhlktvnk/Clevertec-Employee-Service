package ru.clevertec.employeeservice.builder.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.employeeservice.builder.TestBuilder;
import ru.clevertec.employeeservice.entity.Employee;
import ru.clevertec.employeeservice.entity.Leave;

import java.sql.Date;
import java.time.LocalDate;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "aLeave")
public class LeaveTestBuilder implements TestBuilder<Leave> {

    private Long id = 0L;

    private Date startDate = Date.valueOf(LocalDate.now());

    private Date endDate = Date.valueOf(LocalDate.now());;

    private Employee employee = EmployeeTestBuilder.anEmployee().build();

    @Override
    public Leave build() {
        Leave leave = new Leave();

        leave.setId(id);
        leave.setStartDate(startDate);
        leave.setEndDate(endDate);
        leave.setEmployee(employee);

        return leave;
    }
}
