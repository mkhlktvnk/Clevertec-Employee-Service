package ru.clevertec.employeeservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.employeeservice.entity.Employee;
import ru.clevertec.employeeservice.entity.Leave;
import ru.clevertec.employeeservice.mapper.LeaveMapper;
import ru.clevertec.employeeservice.message.Messages;
import ru.clevertec.employeeservice.message.code.EmployeeMessageCode;
import ru.clevertec.employeeservice.message.code.LeaveMessageCode;
import ru.clevertec.employeeservice.repository.LeaveRepository;
import ru.clevertec.employeeservice.service.EmployeeService;
import ru.clevertec.employeeservice.service.LeaveService;
import ru.clevertec.employeeservice.service.exception.ResourceNotFoundException;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {
    private final LeaveRepository leaveRepository;
    private final EmployeeService employeeService;
    private final LeaveMapper leaveMapper = Mappers.getMapper(LeaveMapper.class);
    private final Messages messages;

    @Override
    public List<Leave> findAllByEmployeeIdAndPageable(Long employeeId, Pageable pageable) {
        if (!employeeService.existsById(employeeId)) {
            throw new ResourceNotFoundException(
                    messages.get(EmployeeMessageCode.NOT_FOUND_BY_ID, employeeId)
            );
        }

        return leaveRepository.findAllByEmployeeId(employeeId, pageable);
    }

    @Override
    public Leave findByEmployeeAndLeaveIds(Long employeeId, Long leaveId) {
        return leaveRepository.findByEmployeeIdAndId(employeeId, leaveId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messages.get(LeaveMessageCode.NOT_FOUND_BY_EMPLOYEE_AND_LEAVE_ID, employeeId, leaveId)
                ));
    }

    @Override
    @Transactional
    public Leave addLeaveToEmployee(Long employeeId, Leave leave) {
        Employee employeeToAddLeave = employeeService.findById(employeeId);
        leave.setEmployee(employeeToAddLeave);

        return leaveRepository.save(leave);
    }

    @Override
    @Transactional
    public void updateLeaveByEmployeeAndLeaveIds(Long employeeId, Long leaveId, Leave updateLeave) {
        Leave leaveToUpdate = leaveRepository.findByEmployeeIdAndId(employeeId, leaveId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messages.get(LeaveMessageCode.NOT_FOUND_BY_EMPLOYEE_AND_LEAVE_ID, employeeId, leaveId)
                ));
        leaveMapper.copyAllFields(leaveToUpdate, updateLeave);
        leaveRepository.save(leaveToUpdate);
    }

    @Override
    @Transactional
    public void deleteLeaveByEmployeeAndLeaveIds(Long employeeId, Long leaveId) {
        Leave leaveToDelete = leaveRepository.findByEmployeeIdAndId(employeeId, leaveId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messages.get(LeaveMessageCode.NOT_FOUND_BY_EMPLOYEE_AND_LEAVE_ID, employeeId, leaveId)
                ));
        leaveRepository.delete(leaveToDelete);
    }
}
