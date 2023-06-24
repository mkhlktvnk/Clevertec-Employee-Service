package ru.clevertec.employeeservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.employeeservice.entity.Payroll;
import ru.clevertec.employeeservice.entity.Salary;
import ru.clevertec.employeeservice.message.Messages;
import ru.clevertec.employeeservice.message.code.PayrollMessageCode;
import ru.clevertec.employeeservice.message.code.SalaryMessageCode;
import ru.clevertec.employeeservice.repository.PayrollRepository;
import ru.clevertec.employeeservice.service.PayrollService;
import ru.clevertec.employeeservice.service.SalaryService;
import ru.clevertec.employeeservice.service.exception.ResourceNotFoundException;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollService {
    private final PayrollRepository payrollRepository;
    private final SalaryService salaryService;
    private final Messages messages;

    @Override
    public List<Payroll> findAllOfSalaryWithPageable(Long employeeId, Long salaryId, Long payrollId, Pageable pageable) {
        if (!salaryService.existsByEmployeeAndSalaryIds(employeeId, salaryId)) {
            throw new ResourceNotFoundException(
                    messages.get(SalaryMessageCode.NOT_FOUND_BY_EMPLOYEE_AND_SALARY_IDS, employeeId, salaryId)
            );
        }

        return payrollRepository.findAllByEmployeeAndSalaryIds(employeeId, salaryId, pageable);
    }

    @Override
    public Payroll findByEmployeeSalaryAndPayrollIds(Long employeeId, Long salaryId, Long payrollId) {
        return payrollRepository.findByEmployeeSalaryAndPayrollIds(employeeId, salaryId, payrollId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messages.get(PayrollMessageCode.NOT_FOUND_BY_EMPLOYEE_SALARY_AND_PAYROLL_IDS,
                                employeeId, salaryId, payrollId)
                ));
    }

    @Override
    @Transactional
    public Payroll addPayrollToEmployeeSalary(Long employeeId, Long salaryId, Payroll payroll) {
        Salary salaryToAddPayroll = salaryService.findByEmployeeAndSalaryIds(employeeId, salaryId);
        payroll.setSalary(salaryToAddPayroll);

        return payrollRepository.save(payroll);
    }

    @Override
    @Transactional
    public void updatePayrollOfEmployeeSalary(Long employeeId, Long salaryId, Long payrollId, Payroll updatedPayroll) {
        Payroll payrollToUpdate = payrollRepository.findByEmployeeSalaryAndPayrollIds(employeeId, salaryId, payrollId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messages.get(PayrollMessageCode.NOT_FOUND_BY_EMPLOYEE_SALARY_AND_PAYROLL_IDS,
                                employeeId, salaryId, payrollId)
                ));
        payrollRepository.save(payrollToUpdate);
    }

    @Override
    @Transactional
    public void deletePayrollOfEmployeeSalary(Long employeeId, Long salaryId, Long payrollId) {
        Payroll payrollToDelete = payrollRepository.findByEmployeeSalaryAndPayrollIds(employeeId, salaryId, payrollId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messages.get(PayrollMessageCode.NOT_FOUND_BY_EMPLOYEE_SALARY_AND_PAYROLL_IDS,
                                employeeId, salaryId, payrollId)
                ));
        payrollRepository.delete(payrollToDelete);
    }
}
