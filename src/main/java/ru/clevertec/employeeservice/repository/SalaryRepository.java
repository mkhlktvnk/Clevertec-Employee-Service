package ru.clevertec.employeeservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.employeeservice.entity.Salary;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {
    List<Salary> findAllByEmployeeId(Long employeeId, Pageable pageable);

    Optional<Salary> findByEmployeeIdAndId(Long employeeId, Long salaryId);

    boolean existsByEmployeeIdAndId(Long employeeId, Long salaryId);
}
