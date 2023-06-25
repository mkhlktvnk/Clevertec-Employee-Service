package ru.clevertec.employeeservice.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.employeeservice.entity.Salary;
import ru.clevertec.employeeservice.mapper.SalaryMapper;
import ru.clevertec.employeeservice.service.SalaryService;
import ru.clevertec.employeeservice.web.dto.SalaryDto;

import java.util.List;

@RestController
@RequestMapping("/api/v0")
@RequiredArgsConstructor
public class SalaryController {
    private final SalaryService salaryService;
    private final SalaryMapper salaryMapper = Mappers.getMapper(SalaryMapper.class);

    @GetMapping("/employees/{employeeId}/salaries")
    public ResponseEntity<List<SalaryDto>> findAllBySalaryIdAndPageable(@PathVariable Long employeeId,
                                                                         @PageableDefault Pageable pageable) {
        List<Salary> salaries = salaryService.findAllByEmployeeIdAndPageable(employeeId, pageable);

        return ResponseEntity.ok(salaryMapper.toDto(salaries));
    }

    @GetMapping("/employees/{employeeId}/salaries/{salaryId}")
    public ResponseEntity<SalaryDto> findByEmployeeAndSalariesIds(@PathVariable Long employeeId,
                                                                  @PathVariable Long salaryId) {
        Salary salary = salaryService.findByEmployeeAndSalaryIds(employeeId, salaryId);

        return ResponseEntity.ok(salaryMapper.toDto(salary));
    }

    @PostMapping("/employees/{employeeId}/salaries")
    public ResponseEntity<SalaryDto> addSalaryToEmployee(@PathVariable Long employeeId,
                                                         @Valid @RequestBody SalaryDto salaryDto) {
        Salary createdSalary = salaryService.addSalaryToEmployee(employeeId, salaryMapper.toEntity(salaryDto));

        return ResponseEntity.status(HttpStatus.CREATED).body(salaryMapper.toDto(createdSalary));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/employees/{employeeId}/salaries/{salaryId}")
    public void updateSalaryByEmployeeAndSalaryIds(@PathVariable Long employeeId, @PathVariable Long salaryId,
                                                   @Valid @RequestBody SalaryDto salaryDto) {
        salaryService.updateSalaryByEmployeeAndSalaryIds(employeeId, salaryId, salaryMapper.toEntity(salaryDto));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/employees/{employeeId}/salaries/{salaryId}")
    public void deleteSalaryByEmployeeAndSalaryIds(@PathVariable Long employeeId, @PathVariable Long salaryId) {
        salaryService.deleteSalaryByEmployeeAndSalaryIds(employeeId, salaryId);
    }

}
