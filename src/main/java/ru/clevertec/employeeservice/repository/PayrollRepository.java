package ru.clevertec.employeeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.employeeservice.entity.Payroll;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    List<Payroll> findAllBySalaryId(Long salaryId);

    Optional<Payroll> findBySalaryId(Long salaryId);

    Optional<Payroll> findBySalaryIdAndId(Long salaryId, Long id);
}
