package ru.clevertec.employeeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.clevertec.employeeservice.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Long, Employee>, JpaSpecificationExecutor<Employee> {
}
