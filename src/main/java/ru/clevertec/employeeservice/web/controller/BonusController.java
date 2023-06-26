package ru.clevertec.employeeservice.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.employeeservice.entity.Bonus;
import ru.clevertec.employeeservice.mapper.BonusMapper;
import ru.clevertec.employeeservice.service.BonusService;
import ru.clevertec.employeeservice.web.dto.BonusDto;

import java.util.List;

@RestController
@RequestMapping("/api/v0")
@RequiredArgsConstructor
public class BonusController {
    private final BonusService bonusService;
    private final BonusMapper bonusMapper = Mappers.getMapper(BonusMapper.class);

    @GetMapping("/employees/{employeeId}/bonuses")
    public ResponseEntity<List<BonusDto>> findAllByEmployeeIdAndPageable(
            @PathVariable Long employeeId, @PageableDefault Pageable pageable) {
        List<Bonus> bonuses = bonusService.findAllByEmployeeIdAndPageable(employeeId, pageable);

        return ResponseEntity.ok(bonusMapper.toDto(bonuses));
    }

    @GetMapping("/employees/{employeeId}/bonuses/{bonusId}")
    public ResponseEntity<BonusDto> findByEmployeeAndBonusIds(@PathVariable Long employeeId,
                                                              @PathVariable Long bonusId) {
        Bonus bonus = bonusService.findByEmployeeAndBonusIds(employeeId, bonusId);

        return ResponseEntity.ok(bonusMapper.toDto(bonus));
    }

    @PostMapping("/employees/{employeeId}/bonuses")
    public ResponseEntity<BonusDto> addBonusToEmployee(@PathVariable Long employeeId,
                                                       @Valid @RequestBody BonusDto bonusDto) {
        Bonus createdBonus = bonusService.addBonusToEmployee(employeeId, bonusMapper.toEntity(bonusDto));

        return ResponseEntity.status(HttpStatus.CREATED).body(bonusMapper.toDto(createdBonus));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/employees/{employeeId}/bonuses/{bonusId}")
    public void updateBonusByEmployeeAndBonusIds(@PathVariable Long employeeId,
                                                 @PathVariable Long bonusId,
                                                 @Valid @RequestBody BonusDto bonusDto) {
        bonusService.updateBonusByEmployeeAndBonusIds(employeeId, bonusId, bonusMapper.toEntity(bonusDto));

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/employees/{employeeId}/bonuses/{bonusId}")
    public void deleteBonusByEmployeeAndBonusIds(@PathVariable Long employeeId, @PathVariable Long bonusId) {
        bonusService.deleteBonusByEmployeeAndBonusIds(employeeId, bonusId);
    }
}
