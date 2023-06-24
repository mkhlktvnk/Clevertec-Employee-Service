package ru.clevertec.employeeservice.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.employeeservice.criteria.EmployeeCriteria;
import ru.clevertec.employeeservice.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> findAllByPageableAndCriteria(Pageable pageable, EmployeeCriteria criteria);

    Employee findById(Long employeeId);

    boolean existsById(Long employeeId);

    Employee createEmployee(Employee employee);

    void updateById(Long employeeId, Employee updateEmployee);

    void deleteById(Long employeeId);
}
