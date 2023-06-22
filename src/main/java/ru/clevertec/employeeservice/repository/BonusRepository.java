package ru.clevertec.employeeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.employeeservice.entity.Bonus;

import java.util.List;
import java.util.Optional;

@Repository
public interface BonusRepository extends JpaRepository<Bonus, Long> {
    List<Bonus> findAllByEmployeeId(Long employeeId);

    Optional<Bonus> findByEmployeeId(Long employeeId);

    Optional<Bonus> findByEmployeeIdAndId(Long employeeId, Long id);
}