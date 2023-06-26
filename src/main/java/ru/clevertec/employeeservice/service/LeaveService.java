package ru.clevertec.employeeservice.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.employeeservice.entity.Leave;

import java.util.List;

public interface LeaveService {
    List<Leave> findAllByEmployeeIdAndPageable(Long employeeId, Pageable pageable);

    Leave findByEmployeeAndLeaveIds(Long employeeId, Long leaveId);

    Leave addLeaveToEmployee(Long employeeId, Leave leave);

    void updateLeaveByEmployeeAndLeaveIds(Long employeeId, Long leaveId, Leave updateLeave);

    void deleteLeaveByEmployeeAndLeaveIds(Long employeeId, Long leaveId);
}
