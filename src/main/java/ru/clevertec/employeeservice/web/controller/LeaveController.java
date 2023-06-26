package ru.clevertec.employeeservice.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.employeeservice.entity.Leave;
import ru.clevertec.employeeservice.mapper.LeaveMapper;
import ru.clevertec.employeeservice.service.LeaveService;
import ru.clevertec.employeeservice.web.dto.LeaveDto;

import java.util.List;

@RestController
@RequestMapping("/api/v0")
@RequiredArgsConstructor
public class LeaveController {
    private final LeaveService leaveService;
    private final LeaveMapper leaveMapper = Mappers.getMapper(LeaveMapper.class);

    @GetMapping("/employees/{employeeId}/leaves")
    public ResponseEntity<List<LeaveDto>> findAllByEmployeeIdAndPageable(@PathVariable Long employeeId,
                                                                         @PageableDefault Pageable pageable) {
        List<Leave> leaves = leaveService.findAllByEmployeeIdAndPageable(employeeId, pageable);

        return ResponseEntity.ok(leaveMapper.toDto(leaves));
    }

    @GetMapping("/employees/{employeeId}/leaves/{leaveId}")
    public ResponseEntity<LeaveDto> findByEmployeeAndLeaveIds(@PathVariable Long employeeId,
                                                              @PathVariable Long leaveId) {
        Leave leave = leaveService.findByEmployeeAndLeaveIds(employeeId, leaveId);

        return ResponseEntity.ok(leaveMapper.toDto(leave));
    }

    @PostMapping("/employees/{employeeId}/leaves")
    public ResponseEntity<LeaveDto> addLeaveToEmployee(@PathVariable Long employeeId,
                                                       @Valid @RequestBody LeaveDto leaveDto) {
        Leave createdLeave = leaveService.addLeaveToEmployee(employeeId, leaveMapper.toEntity(leaveDto));

        return ResponseEntity.status(HttpStatus.CREATED).body(leaveMapper.toDto(createdLeave));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/employees/{employeeId}/leaves/{leaveId}")
    public void updateLeaveById(@PathVariable Long employeeId, @PathVariable Long leaveId,
                                @Valid @RequestBody LeaveDto leaveDto) {
        leaveService.updateLeaveByEmployeeAndLeaveIds(employeeId, leaveId, leaveMapper.toEntity(leaveDto));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/employees/{employeeId}/leaves/{leaveId}")
    public void deleteLeaveByEmployeeAndLeaveIds(@PathVariable Long employeeId, @PathVariable Long leaveId) {
        leaveService.deleteLeaveByEmployeeAndLeaveIds(employeeId, leaveId);
    }

}
