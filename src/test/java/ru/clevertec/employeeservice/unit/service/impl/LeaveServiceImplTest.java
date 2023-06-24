package ru.clevertec.employeeservice.unit.service.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.employeeservice.builder.impl.EmployeeTestBuilder;
import ru.clevertec.employeeservice.builder.impl.LeaveTestBuilder;
import ru.clevertec.employeeservice.entity.Employee;
import ru.clevertec.employeeservice.entity.Leave;
import ru.clevertec.employeeservice.message.Messages;
import ru.clevertec.employeeservice.repository.LeaveRepository;
import ru.clevertec.employeeservice.service.EmployeeService;
import ru.clevertec.employeeservice.service.exception.ResourceNotFoundException;
import ru.clevertec.employeeservice.service.impl.LeaveServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class LeaveServiceImplTest {

    private static final Long EMPLOYEE_ID = 1L;
    private static final Long LEAVE_ID = 2L;

    @Mock
    private LeaveRepository leaveRepository;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private Messages messages;

    @InjectMocks
    private LeaveServiceImpl leaveService;

    @Nested
    class ReadMethodsTest {

        @Test
        void findAllByEmployeeIdAndPageableShouldReturnExpectedLeavesAndCallRepository() {
            List<Leave> expectedLeaves = List.of(
                    LeaveTestBuilder.aLeave().build(),
                    LeaveTestBuilder.aLeave().build(),
                    LeaveTestBuilder.aLeave().build()
            );
            Pageable pageable = PageRequest.of(0, 3);
            doReturn(true).when(employeeService).existsById(EMPLOYEE_ID);
            doReturn(expectedLeaves).when(leaveRepository).findAllByEmployeeId(EMPLOYEE_ID, pageable);

            List<Leave> actualLeaves = leaveService.findAllByEmployeeIdAndPageable(EMPLOYEE_ID, pageable);

            assertThat(actualLeaves).isEqualTo(expectedLeaves);
            verify(leaveRepository).findAllByEmployeeId(EMPLOYEE_ID, pageable);
        }

        @Test
        void findAllByEmployeeIdAndPageableShouldThrowResourceNotFoundExceptionWhenEmployeeIsNotPresent() {
            Pageable pageable = PageRequest.of(0, 3);
            doReturn(false).when(employeeService).existsById(EMPLOYEE_ID);

            assertThatThrownBy(() -> leaveService.findAllByEmployeeIdAndPageable(EMPLOYEE_ID, pageable))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void findByEmployeeAndBonusIdsShouldReturnActualLeaveAndCallRepositoryWhenLeaveIsPresent() {
            Leave expectedLeave = LeaveTestBuilder.aLeave().build();
            doReturn(Optional.of(expectedLeave)).when(leaveRepository).findByEmployeeIdAndId(EMPLOYEE_ID, LEAVE_ID);

            Leave actualLeave = leaveService.findByEmployeeAndLeaveIds(EMPLOYEE_ID, LEAVE_ID);

            assertThat(actualLeave).isEqualTo(expectedLeave);
            verify(leaveRepository).findByEmployeeIdAndId(EMPLOYEE_ID, LEAVE_ID);
        }

        @Test
        void findByEmployeeAndBonusIdsShouldThrowResourceNotFoundExceptionWhenLeaveIsNotPresent() {
            doReturn(Optional.empty()).when(leaveRepository).findByEmployeeIdAndId(EMPLOYEE_ID, LEAVE_ID);

            assertThatThrownBy(() -> leaveService.findByEmployeeAndLeaveIds(EMPLOYEE_ID, LEAVE_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }


    @Nested
    class CreateMethodsTest {

        @Test
        void addLeaveToEmployeeShouldReturnExpectedLeaveAndCallRepositoryAndService() {
            Leave expectedLeave = LeaveTestBuilder.aLeave().build();
            Employee employeeToAddLeave = EmployeeTestBuilder.anEmployee().build();
            doReturn(employeeToAddLeave).when(employeeService).findById(EMPLOYEE_ID);
            doReturn(expectedLeave).when(leaveRepository).save(expectedLeave);

            Leave actualLeave = leaveService.addLeaveToEmployee(EMPLOYEE_ID, expectedLeave);

            assertThat(actualLeave).isEqualTo(expectedLeave);
            assertThat(actualLeave.getEmployee()).isEqualTo(employeeToAddLeave);
            verify(employeeService).findById(EMPLOYEE_ID);
            verify(leaveRepository).save(expectedLeave);
        }

    }

    @Nested
    class UpdateMethodsTest {

        @Test
        void updateLeaveByEmployeeAndLeaveIdsShouldCallRepositoryWhenLeaveIsPresent() {
            Leave updateLeave = LeaveTestBuilder.aLeave().build();
            Leave leaveToUpdate = LeaveTestBuilder.aLeave().build();
            doReturn(Optional.of(leaveToUpdate)).when(leaveRepository)
                    .findByEmployeeIdAndId(EMPLOYEE_ID, LEAVE_ID);

            leaveService.updateLeaveByEmployeeAndLeaveIds(EMPLOYEE_ID, LEAVE_ID, updateLeave);

            verify(leaveRepository).findByEmployeeIdAndId(EMPLOYEE_ID, LEAVE_ID);
            verify(leaveRepository).save(leaveToUpdate);
        }

        @Test
        void updateLeaveByEmployeeAndLeaveIdsShouldThrowResourceNotFoundExceptionWhenLeaveIsNotPresent() {
            Leave updateLeave = LeaveTestBuilder.aLeave().build();
            doReturn(Optional.empty()).when(leaveRepository)
                    .findByEmployeeIdAndId(EMPLOYEE_ID, LEAVE_ID);

            assertThatThrownBy(() -> leaveService
                    .updateLeaveByEmployeeAndLeaveIds(EMPLOYEE_ID, LEAVE_ID, updateLeave))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

    }

    @Nested
    class DeleteMethodsTest {

        @Test
        void deleteLeaveByEmployeeAndLeaveIdsShouldCallRepositoryWhenLeaveIsPresent() {
            Leave leaveToDelete = LeaveTestBuilder.aLeave().build();
            doReturn(Optional.of(leaveToDelete)).when(leaveRepository)
                    .findByEmployeeIdAndId(EMPLOYEE_ID, LEAVE_ID);

            leaveService.deleteLeaveByEmployeeAndLeaveIds(EMPLOYEE_ID, LEAVE_ID);

            verify(leaveRepository).findByEmployeeIdAndId(EMPLOYEE_ID, LEAVE_ID);
            verify(leaveRepository).delete(leaveToDelete);
        }

        @Test
        void deleteLeaveByEmployeeAndLeaveIdsShouldThrowResourceNotFoundExceptionWhenLeaveIsNotPresent() {
            doReturn(Optional.empty()).when(leaveRepository)
                    .findByEmployeeIdAndId(EMPLOYEE_ID, LEAVE_ID);

            assertThatThrownBy(() -> leaveService.deleteLeaveByEmployeeAndLeaveIds(EMPLOYEE_ID, LEAVE_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

    }

}
