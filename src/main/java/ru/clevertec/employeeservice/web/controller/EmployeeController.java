package ru.clevertec.employeeservice.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.employeeservice.criteria.EmployeeCriteria;
import ru.clevertec.employeeservice.entity.Employee;
import ru.clevertec.employeeservice.mapper.EmployeeMapper;
import ru.clevertec.employeeservice.service.EmployeeService;
import ru.clevertec.employeeservice.web.dto.EmployeeDto;

import java.util.List;

@RestController
@RequestMapping("/api/v0")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper = Mappers.getMapper(EmployeeMapper.class);

    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeDto>> findAllByPageableAndCriteria(@PageableDefault Pageable pageable,
                                                                          EmployeeCriteria criteria) {
        List<Employee> employees = employeeService.findAllByPageableAndCriteria(pageable, criteria);

        return ResponseEntity.ok(employeeMapper.toDto(employees));
    }

    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<EmployeeDto> findById(@PathVariable Long employeeId) {
        Employee employee = employeeService.findById(employeeId);

        return ResponseEntity.ok(employeeMapper.toDto(employee));
    }

    @PostMapping("/employees")
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        Employee createdEmployee = employeeService.createEmployee(employeeMapper.toEntity(employeeDto));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeMapper.toDto(createdEmployee));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/employees/{employeeId}")
    public void updateEmployeeById(@PathVariable Long employeeId, @Valid @RequestBody EmployeeDto employeeDto) {
        employeeService.updateById(employeeId, employeeMapper.toEntity(employeeDto));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/employees/{employeeId}")
    public void deleteEmployeeById(@PathVariable Long employeeId) {
        employeeService.deleteById(employeeId);
    }
}
