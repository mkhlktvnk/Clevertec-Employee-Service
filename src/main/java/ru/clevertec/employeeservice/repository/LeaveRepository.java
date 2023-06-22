package ru.clevertec.employeeservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.employeeservice.entity.Leave;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {
    List<Leave> findAllByEmployeeId(Long employeeId, Pageable pageable);

    Optional<Leave> findByEmployeeIdAndId(Long employeeId, Long leaveId);
}
