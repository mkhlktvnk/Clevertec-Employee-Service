package ru.clevertec.employeeservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.employeeservice.entity.Bonus;
import ru.clevertec.employeeservice.entity.Employee;
import ru.clevertec.employeeservice.mapper.BonusMapper;
import ru.clevertec.employeeservice.message.code.BonusMessageCode;
import ru.clevertec.employeeservice.message.code.EmployeeMessageCode;
import ru.clevertec.employeeservice.repository.BonusRepository;
import ru.clevertec.employeeservice.service.BonusService;
import ru.clevertec.employeeservice.service.EmployeeService;
import ru.clevertec.employeeservice.service.exception.ResourceNotFoundException;
import ru.clevertec.employeeservice.message.Messages;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BonusServiceImpl implements BonusService {
    private final BonusRepository bonusRepository;
    private final EmployeeService employeeService;
    private final BonusMapper bonusMapper = Mappers.getMapper(BonusMapper.class);
    private final Messages messages;


    @Override
    public List<Bonus> findAllByEmployeeIdAndPageable(Long employeeId, Pageable pageable) {
        if (!employeeService.existsById(employeeId)) {
            throw new ResourceNotFoundException(
                    messages.get(EmployeeMessageCode.NOT_FOUND_BY_ID, employeeId)
            );
        }

        return bonusRepository.findAllByEmployeeId(employeeId, pageable);
    }

    @Override
    public Bonus findByEmployeeAndBonusIds(Long employeeId, Long bonusId) {
        return bonusRepository.findByEmployeeIdAndId(employeeId, bonusId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messages.get(BonusMessageCode.NOT_FOUND_BY_EMPLOYEE_AND_BONUS_IDS, employeeId, bonusId)
                ));
    }

    @Override
    @Transactional
    public Bonus addBonusToEmployee(Long employeeId, Bonus bonus) {
        Employee employeeToAddBonus = employeeService.findById(employeeId);
        bonus.setEmployee(employeeToAddBonus);

        return bonusRepository.save(bonus);
    }

    @Override
    @Transactional
    public void updateBonusByEmployeeAndBonusIds(Long employeeId, Long bonusId, Bonus updatedBonus) {
        Bonus bonusToUpdate = bonusRepository.findByEmployeeIdAndId(employeeId, bonusId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messages.get(BonusMessageCode.NOT_FOUND_BY_EMPLOYEE_AND_BONUS_IDS, employeeId, bonusId)
                ));
        bonusMapper.copyAllFields(bonusToUpdate, updatedBonus);
        bonusRepository.save(bonusToUpdate);
    }

    @Override
    @Transactional
    public void deleteBonusByEmployeeAndBonusIds(Long employeeId, Long bonusId) {
        Bonus bonusToDelete = bonusRepository.findByEmployeeIdAndId(employeeId, bonusId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messages.get(BonusMessageCode.NOT_FOUND_BY_EMPLOYEE_AND_BONUS_IDS, employeeId, bonusId)
                ));
        bonusRepository.delete(bonusToDelete);
    }
}
