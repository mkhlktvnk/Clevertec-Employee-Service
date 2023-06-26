package ru.clevertec.employeeservice.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.employeeservice.entity.Payroll;

import java.util.List;
import java.util.Optional;

public interface PayrollService {
    List<Payroll> findAllOfEmployeeSalaryWithPageable(Long employeeId, Long salaryId, Pageable pageable);

    Payroll findByEmployeeSalaryAndPayrollIds(Long employeeId, Long salaryId, Long payrollId);

    Payroll addPayrollToEmployeeSalary(Long employeeId, Long salaryId, Payroll payroll);

    void updatePayrollOfEmployeeSalary(Long employeeId, Long salaryId, Long payroll, Payroll updatedPayroll);

    void deletePayrollOfEmployeeSalary(Long employeeId, Long salaryId, Long payrollId);
}
