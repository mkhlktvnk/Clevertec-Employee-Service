package ru.clevertec.employeeservice.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.employeeservice.entity.Payroll;
import ru.clevertec.employeeservice.mapper.PayrollMapper;
import ru.clevertec.employeeservice.service.PayrollService;
import ru.clevertec.employeeservice.web.dto.PayrollDto;

import java.util.List;

@RestController
@RequestMapping("/api/v0")
@RequiredArgsConstructor
public class PayrollController {
    private final PayrollService payrollService;
    private final PayrollMapper payrollMapper = Mappers.getMapper(PayrollMapper.class);

    @GetMapping("/employees/{employeeId}/salaries/{salaryId}/payrolls")
    public ResponseEntity<List<PayrollDto>> findAllByEmployeeAndSalaryIdsAndPageable(
            @PathVariable Long employeeId, @PathVariable Long salaryId, @PageableDefault Pageable pageable) {
        List<Payroll> payrolls = payrollService.findAllOfEmployeeSalaryWithPageable(employeeId, salaryId, pageable);

        return ResponseEntity.ok(payrollMapper.toDto(payrolls));
    }

    @GetMapping("/employees/{employeeId}/salaries/{salaryId}/payrolls/{payrollId}")
    public ResponseEntity<PayrollDto> findByEmployeeSalaryAndPayrollIds(
            @PathVariable Long employeeId, @PathVariable Long salaryId, @PathVariable Long payrollId) {
        Payroll payroll = payrollService.findByEmployeeSalaryAndPayrollIds(employeeId, salaryId, payrollId);

        return ResponseEntity.ok(payrollMapper.toDto(payroll));
    }

    @PostMapping("/employees/{employeeId}/salaries/{salaryId}/payrolls")
    public ResponseEntity<PayrollDto> addPayrollToEmployeeSalary(@PathVariable Long employeeId,
                                                                 @PathVariable Long salaryId,
                                                                 @Valid @RequestBody PayrollDto payrollDto) {
        Payroll createdPayroll = payrollService
                .addPayrollToEmployeeSalary(employeeId, salaryId, payrollMapper.toEntity(payrollDto));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(payrollMapper.toDto(createdPayroll));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/employees/{employeeId}/salaries/{salaryId}/payrolls/{payrollId}")
    public void updatePayrollOfEmployeeSalary(@PathVariable Long employeeId, @PathVariable Long salaryId,
                                          @PathVariable Long payrollId, @Valid @RequestBody PayrollDto payrollDto) {
        payrollService.updatePayrollOfEmployeeSalary(employeeId, salaryId,
                payrollId, payrollMapper.toEntity(payrollDto));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/employees/{employeeId}/salaries/{salaryId}/payrolls/{payrollId}")
    public void deletePayrollOfEmployeeSalary(@PathVariable Long employeeId, @PathVariable Long salaryId,
                                              @PathVariable Long payrollId) {
        payrollService.deletePayrollOfEmployeeSalary(employeeId, salaryId, payrollId);
    }
}
