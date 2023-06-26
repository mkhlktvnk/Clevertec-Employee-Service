package ru.clevertec.employeeservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.employeeservice.criteria.EmployeeCriteria;
import ru.clevertec.employeeservice.entity.Employee;
import ru.clevertec.employeeservice.mapper.EmployeeMapper;
import ru.clevertec.employeeservice.repository.EmployeeRepository;
import ru.clevertec.employeeservice.message.code.EmployeeMessageCode;
import ru.clevertec.employeeservice.service.EmployeeService;
import ru.clevertec.employeeservice.service.exception.InvalidDataException;
import ru.clevertec.employeeservice.service.exception.ResourceNotFoundException;
import ru.clevertec.employeeservice.message.Messages;
import ru.clevertec.employeeservice.specification.EmployeeSpecifications;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper = Mappers.getMapper(EmployeeMapper.class);
    private final Messages messages;


    @Override
    public List<Employee> findAllByPageableAndCriteria(Pageable pageable, EmployeeCriteria criteria) {
        Specification<Employee> criteriaSpec = EmployeeSpecifications.hasMatchWithCriteria(criteria);

        return employeeRepository.findAll(criteriaSpec, pageable).getContent();
    }

    @Override
    public Employee findById(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messages.get(EmployeeMessageCode.NOT_FOUND_BY_ID, employeeId)
                ));
    }

    @Override
    public boolean existsById(Long employeeId) {
        return employeeRepository.existsById(employeeId);
    }

    @Override
    @Transactional
    public Employee createEmployee(Employee employee) {
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new InvalidDataException(
                    messages.get(EmployeeMessageCode.EMAIL_IS_NOT_UNIQUE, employee.getEmail())
            );
        }
        if (employeeRepository.existsByPhoneNumber(employee.getPhoneNumber())) {
            throw new InvalidDataException(
                    messages.get(EmployeeMessageCode.PHONE_NUMBER_IS_NOT_UNIQUE, employee.getPhoneNumber())
            );
        }

        return employeeRepository.save(employee);
    }

    @Override
    public void updateById(Long employeeId, Employee updateEmployee) {
        if (employeeRepository.existsByEmail(updateEmployee.getEmail())) {
            throw new InvalidDataException(
                    messages.get(EmployeeMessageCode.EMAIL_IS_NOT_UNIQUE, updateEmployee.getEmail())
            );
        }
        if (employeeRepository.existsByPhoneNumber(updateEmployee.getPhoneNumber())) {
            throw new InvalidDataException(
                    messages.get(EmployeeMessageCode.PHONE_NUMBER_IS_NOT_UNIQUE, updateEmployee.getPhoneNumber())
            );
        }
        Employee employeeToUpdate = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messages.get(EmployeeMessageCode.NOT_FOUND_BY_ID, employeeId)
                ));
        employeeMapper.copyAllFields(employeeToUpdate, updateEmployee);
        employeeRepository.save(employeeToUpdate);
    }

    @Override
    public void deleteById(Long employeeId) {
        Employee employeeToDelete = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messages.get(EmployeeMessageCode.NOT_FOUND_BY_ID, employeeId)
                ));
        employeeRepository.delete(employeeToDelete);
    }
}
