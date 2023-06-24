package ru.clevertec.employeeservice.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.employeeservice.entity.Salary;

import java.util.List;

public interface SalaryService {
    List<Salary> findAllByEmployeeIdAndPageable(Long employeeId, Pageable pageable);

    Salary findByEmployeeAndSalaryIds(Long employeeId, Long salaryId);

    boolean existsByEmployeeAndSalaryIds(Long employeeId, Long salaryId);

    Salary addSalaryToEmployee(Long employeeId, Salary salary);

    void updateSalaryByEmployeeAndSalaryIds(Long employeeId, Long salaryId, Salary updatedSalary);

    void deleteSalaryByEmployeeAndSalaryIds(Long employeeId, Long salaryId);
}
