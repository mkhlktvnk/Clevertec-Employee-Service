package ru.clevertec.employeeservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.clevertec.employeeservice.entity.Payroll;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {

    @Query("SELECT p FROM Payroll p " +
            "JOIN p.salary s " +
            "JOIN s.employee e " +
            "WHERE e.id = :employeeId " +
            "AND s.id = :salaryId " +
            "AND p.id = :payrollId")
    Optional<Payroll> findByEmployeeSalaryAndPayrollIds(@Param("employeeId") Long employeeId,
                                                        @Param("salaryId") Long salaryId,
                                                        @Param("payrollId") Long payrollId);

    @Query("SELECT p FROM Payroll p " +
            "JOIN p.salary s " +
            "JOIN s.employee e " +
            "WHERE e.id = :employeeId " +
            "AND s.id = :salaryId")
    List<Payroll> findAllByEmployeeAndSalaryIds(@Param("employeeId") Long employeeId,
                                                       @Param("salaryId") Long salaryId,
                                                       Pageable pageable);
}
