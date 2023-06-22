package ru.clevertec.employeeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.employeeservice.entity.Position;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    List<Position> findAllByEmployeesId(Long employeesId);

    Optional<Position> findByEmployeesId(Long employeesId);

    Optional<Position> findByEmployeesIdAndId(Long employeesId, Long id);
}
