package ru.clevertec.employeeservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.employeeservice.entity.Employee;
import ru.clevertec.employeeservice.entity.Salary;
import ru.clevertec.employeeservice.mapper.SalaryMapper;
import ru.clevertec.employeeservice.message.Messages;
import ru.clevertec.employeeservice.message.code.EmployeeMessageCode;
import ru.clevertec.employeeservice.message.code.SalaryMessageCode;
import ru.clevertec.employeeservice.repository.SalaryRepository;
import ru.clevertec.employeeservice.service.EmployeeService;
import ru.clevertec.employeeservice.service.SalaryService;
import ru.clevertec.employeeservice.service.exception.ResourceNotFoundException;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SalaryServiceImpl implements SalaryService {
    private final SalaryRepository salaryRepository;
    private final EmployeeService employeeService;
    private final SalaryMapper salaryMapper = Mappers.getMapper(SalaryMapper.class);
    private final Messages messages;

    @Override
    public List<Salary> findAllByEmployeeIdAndPageable(Long employeeId, Pageable pageable) {
        if (!employeeService.existsById(employeeId)) {
            throw new ResourceNotFoundException(
                    messages.get(EmployeeMessageCode.NOT_FOUND_BY_ID, employeeId)
            );
        }
        return salaryRepository.findAllByEmployeeId(employeeId, pageable);
    }

    @Override
    public Salary findByEmployeeAndSalaryIds(Long employeeId, Long salaryId) {
        return salaryRepository.findByEmployeeIdAndId(employeeId, salaryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messages.get(SalaryMessageCode.NOT_FOUND_BY_EMPLOYEE_AND_SALARY_IDS, employeeId, salaryId)
                ));
    }

    @Override
    public boolean existsByEmployeeAndSalaryIds(Long employeeId, Long salaryId) {
        return salaryRepository.existsByEmployeeIdAndId(employeeId, salaryId);
    };

    @Override
    @Transactional
    public Salary addSalaryToEmployee(Long employeeId, Salary salary) {
        Employee employeeToAddSalary = employeeService.findById(employeeId);
        salary.setEmployee(employeeToAddSalary);

        return salaryRepository.save(salary);
    }

    @Override
    @Transactional
    public void updateSalaryByEmployeeAndSalaryIds(Long employeeId, Long salaryId, Salary updatedSalary) {
        Salary salaryToUpdate = salaryRepository.findByEmployeeIdAndId(employeeId, salaryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messages.get(SalaryMessageCode.NOT_FOUND_BY_EMPLOYEE_AND_SALARY_IDS, employeeId, salaryId)
                ));
        salaryMapper.copyAllFields(salaryToUpdate, updatedSalary);
        salaryRepository.save(salaryToUpdate);
    }

    @Override
    @Transactional
    public void deleteSalaryByEmployeeAndSalaryIds(Long employeeId, Long salaryId) {
        Salary salaryToDelete = salaryRepository.findByEmployeeIdAndId(employeeId, salaryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messages.get(SalaryMessageCode.NOT_FOUND_BY_EMPLOYEE_AND_SALARY_IDS, employeeId, salaryId)
                ));
        salaryRepository.delete(salaryToDelete);
    }
}

