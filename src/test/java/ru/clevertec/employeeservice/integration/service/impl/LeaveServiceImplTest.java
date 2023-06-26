package ru.clevertec.employeeservice.integration.service.impl;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.employeeservice.builder.impl.LeaveTestBuilder;
import ru.clevertec.employeeservice.entity.Leave;
import ru.clevertec.employeeservice.integration.BaseIntegrationTest;
import ru.clevertec.employeeservice.service.LeaveService;
import ru.clevertec.employeeservice.service.exception.ResourceNotFoundException;

import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LeaveServiceImplTest extends BaseIntegrationTest {

    private static final Long CORRECT_EMPLOYEE_ID = 1L;
    private static final Long INCORRECT_EMPLOYEE_ID = 1000L;
    private static final Long CORRECT_LEAVE_ID = 2L;
    private static final Long INCORRECT_LEAVE_ID = 1001L;

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private EntityManager entityManager;

    @Nested
    class ReadMethodsTest {

        @Test
        void findAllByEmployeeIdAndPageableShouldReturnResultWithExpectedSize() {
            int expectedSize = 3;
            Pageable pageable = PageRequest.of(0, 3);

            List<Leave> leaves = leaveService.findAllByEmployeeIdAndPageable(CORRECT_EMPLOYEE_ID, pageable);

            assertThat(leaves.size()).isEqualTo(expectedSize);
        }

        @Test
        void findAllByEmployeeIdAndPageableShouldThrowResourceNotFoundExceptionWhenEmployeeIsNotPresent() {
            Pageable pageable = PageRequest.of(0, 3);

            assertThatThrownBy(() -> leaveService.findAllByEmployeeIdAndPageable(INCORRECT_EMPLOYEE_ID, pageable))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void findAllByEmployeeIdAndPageableShouldReturnEmptyResultWhenPageIsGreaterThanLeavesCount() {
            Pageable pageable = PageRequest.of(1000, 3);

            List<Leave> leaves = leaveService.findAllByEmployeeIdAndPageable(CORRECT_EMPLOYEE_ID, pageable);

            assertThat(leaves).isEmpty();
        }

        @Test
        void findByEmployeeAndLeaveIdsShouldReturnLeaveWithExpectedId() {
            Leave actualLeave = leaveService.findByEmployeeAndLeaveIds(CORRECT_EMPLOYEE_ID, CORRECT_LEAVE_ID);

            assertThat(actualLeave.getId()).isEqualTo(CORRECT_LEAVE_ID);
        }

        @Test
        void findByEmployeeAndLeaveIdsShouldThrowResourceNotFoundExceptionWhenEmployeeIdIsIncorrect() {
            assertThatThrownBy(() -> leaveService.findByEmployeeAndLeaveIds(INCORRECT_EMPLOYEE_ID, CORRECT_LEAVE_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void findByEmployeeAndLeaveIdsShouldThrowResourceNotFoundExceptionWhenLeaveIdIsIncorrect() {
            assertThatThrownBy(() -> leaveService.findByEmployeeAndLeaveIds(CORRECT_EMPLOYEE_ID, INCORRECT_LEAVE_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    class CreateMethodsTest {

        @Test
        void addLeaveToEmployeeShouldReturnCreatedLeaveWithNotNullIdAndWithCorrectEmployee() {
            Leave leaveToCreate = LeaveTestBuilder.aLeave()
                    .withStartDate(Date.valueOf("2023-06-01"))
                    .withEndDate(Date.valueOf("2023-06-05"))
                    .build();

            Leave actualLeave = leaveService.addLeaveToEmployee(CORRECT_EMPLOYEE_ID, leaveToCreate);

            assertThat(actualLeave.getId()).isNotNull();
            assertThat(actualLeave.getEmployee().getId()).isEqualTo(CORRECT_EMPLOYEE_ID);
        }

        @Test
        void addLeaveToEmployeeShouldThrowResourceNotFoundExceptionWhenEmployeeNotPresent() {
            Leave leaveToCreate = LeaveTestBuilder.aLeave()
                    .withStartDate(Date.valueOf("2023-06-01"))
                    .withEndDate(Date.valueOf("2023-06-05"))
                    .build();

            assertThatThrownBy(() -> leaveService.addLeaveToEmployee(INCORRECT_EMPLOYEE_ID, leaveToCreate))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    class UpdateMethodsTest {

        @Test
        void updateLeaveByEmployeeAndLeaveIdsShouldUpdateLeave() {
            Leave updatedLeave = LeaveTestBuilder.aLeave()
                    .withStartDate(Date.valueOf("2023-06-01"))
                    .withEndDate(Date.valueOf("2023-06-05"))
                    .build();

            leaveService.updateLeaveByEmployeeAndLeaveIds(CORRECT_EMPLOYEE_ID, CORRECT_LEAVE_ID, updatedLeave);
            entityManager.flush();
        }

        @Test
        void updateLeaveByEmployeeAndLeaveIdsShouldThrowResourceNotFoundExceptionWhenEmployeeIdIsIncorrect() {
            Leave updatedLeave = LeaveTestBuilder.aLeave()
                    .withStartDate(Date.valueOf("2023-06-01"))
                    .withEndDate(Date.valueOf("2023-06-05"))
                    .build();

            assertThatThrownBy(() -> leaveService
                    .updateLeaveByEmployeeAndLeaveIds(INCORRECT_EMPLOYEE_ID, CORRECT_LEAVE_ID, updatedLeave))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void updateLeaveByEmployeeAndLeaveIdsShouldThrowResourceNotFoundExceptionWhenLeaveIdIsIncorrect() {
            Leave updatedLeave = LeaveTestBuilder.aLeave()
                    .withStartDate(Date.valueOf("2023-06-01"))
                    .withEndDate(Date.valueOf("2023-06-05"))
                    .build();

            assertThatThrownBy(() -> leaveService
                    .updateLeaveByEmployeeAndLeaveIds(CORRECT_EMPLOYEE_ID, INCORRECT_LEAVE_ID, updatedLeave))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    class DeleteMethodsTest {

        @Test
        void deleteLeaveByEmployeeAndLeaveIdsShouldDeleteLeave() {
            leaveService.deleteLeaveByEmployeeAndLeaveIds(CORRECT_EMPLOYEE_ID, CORRECT_LEAVE_ID);
            entityManager.flush();
        }

        @Test
        void deleteLeaveByEmployeeAndLeaveIdsShouldThrowResourceNotFoundExceptionWhenEmployeeIdIsIncorrect() {
            assertThatThrownBy(() -> leaveService
                    .deleteLeaveByEmployeeAndLeaveIds(INCORRECT_EMPLOYEE_ID, CORRECT_LEAVE_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void deleteLeaveByEmployeeAndLeaveIdsShouldThrowResourceNotFoundExceptionWhenLeaveIdIsIncorrect() {
            assertThatThrownBy(() -> leaveService
                    .deleteLeaveByEmployeeAndLeaveIds(CORRECT_EMPLOYEE_ID, INCORRECT_LEAVE_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}
