package ru.clevertec.employeeservice.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.employeeservice.entity.Bonus;

import java.util.List;

public interface BonusService {
    List<Bonus> findAllByEmployeeIdAndPageable(Long employeeId, Pageable pageable);

    Bonus findByEmployeeAndBonusIds(Long employeeId, Long bonusId);

    Bonus addBonusToEmployee(Long employeeId, Bonus bonus);

    void updateBonusByEmployeeAndBonusIds(Long employeeId, Long bonusId, Bonus updatedBonus);

    void deleteBonusByEmployeeAndBonusIds(Long employeeId, Long bonusId);
}
