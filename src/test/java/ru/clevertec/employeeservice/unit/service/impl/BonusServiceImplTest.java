package ru.clevertec.employeeservice.unit.service.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.employeeservice.builder.impl.BonusTestBuilder;
import ru.clevertec.employeeservice.builder.impl.EmployeeTestBuilder;
import ru.clevertec.employeeservice.entity.Bonus;
import ru.clevertec.employeeservice.entity.Employee;
import ru.clevertec.employeeservice.message.Messages;
import ru.clevertec.employeeservice.repository.BonusRepository;
import ru.clevertec.employeeservice.service.EmployeeService;
import ru.clevertec.employeeservice.service.exception.ResourceNotFoundException;
import ru.clevertec.employeeservice.service.impl.BonusServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BonusServiceImplTest {

    private static final Long EMPLOYEE_ID = 1L;

    private static final Long BONUS_ID = 2L;

    @Mock
    private BonusRepository bonusRepository;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private Messages messages;

    @InjectMocks
    private BonusServiceImpl bonusService;

    @Nested
    class ReadMethodsTest {

        @Test
        void findAllByEmployeeIdAndPageableShouldReturnExpectedBonusesAndCallRepository() {
            List<Bonus> expectedBonuses = List.of(
                    BonusTestBuilder.aBonus().build(),
                    BonusTestBuilder.aBonus().build(),
                    BonusTestBuilder.aBonus().build()
            );
            Pageable pageable = PageRequest.of(0, 3);
            doReturn(true).when(employeeService).existsById(EMPLOYEE_ID);
            doReturn(expectedBonuses).when(bonusRepository).findAllByEmployeeId(EMPLOYEE_ID, pageable);

            List<Bonus> actualBonuses = bonusService.findAllByEmployeeIdAndPageable(EMPLOYEE_ID, pageable);

            assertThat(actualBonuses).isEqualTo(expectedBonuses);
            verify(bonusRepository).findAllByEmployeeId(EMPLOYEE_ID, pageable);
        }

        @Test
        void findByEmployeeAndBonusIdsShouldReturnExpectedBonusWhenBonusIsPresent() {
            Bonus expectedBonus = BonusTestBuilder.aBonus().build();
            doReturn(Optional.of(expectedBonus)).when(bonusRepository).findByEmployeeIdAndId(EMPLOYEE_ID, BONUS_ID);

            Bonus actualBonus = bonusService.findByEmployeeAndBonusIds(EMPLOYEE_ID, BONUS_ID);

            assertThat(actualBonus).isEqualTo(expectedBonus);
            verify(bonusRepository).findByEmployeeIdAndId(EMPLOYEE_ID, BONUS_ID);
        }

        @Test
        void findByEmployeeAndBonusIdsShouldThrowResourceNotFoundExceptionWhenBonusIsNotPresent() {
            doReturn(Optional.empty()).when(bonusRepository).findByEmployeeIdAndId(EMPLOYEE_ID, BONUS_ID);

            assertThatThrownBy(() -> bonusService.findByEmployeeAndBonusIds(EMPLOYEE_ID, BONUS_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

    }

    @Nested
    class CreateMethodsTest {

        @Test
        void addBonusToEmployeeShouldReturnExpectedBonusAndCallRepositoryAndService() {
            Bonus expectedBonus = BonusTestBuilder.aBonus().build();
            Employee employeeToAddBonus = EmployeeTestBuilder.anEmployee().build();
            doReturn(employeeToAddBonus).when(employeeService).findById(EMPLOYEE_ID);
            doReturn(expectedBonus).when(bonusRepository).save(expectedBonus);

            Bonus actualBonus = bonusService.addBonusToEmployee(EMPLOYEE_ID, expectedBonus);

            assertThat(actualBonus).isEqualTo(expectedBonus);
            assertThat(actualBonus.getEmployee()).isEqualTo(employeeToAddBonus);
            verify(employeeService).findById(EMPLOYEE_ID);
            verify(bonusRepository).save(expectedBonus);
        }

    }

    @Nested
    class UpdateMethodsTest {

        @Test
        void updateBonusByEmployeeAndBonusIdsShouldCallRepositoryWhenBonusIsPresent() {
            Bonus updateBonus = BonusTestBuilder.aBonus().build();
            Bonus bonusToUpdate = BonusTestBuilder.aBonus().build();
            doReturn(Optional.of(bonusToUpdate)).when(bonusRepository)
                    .findByEmployeeIdAndId(EMPLOYEE_ID, BONUS_ID);

            bonusService.updateBonusByEmployeeAndBonusIds(EMPLOYEE_ID, BONUS_ID, updateBonus);

            verify(bonusRepository).findByEmployeeIdAndId(EMPLOYEE_ID, BONUS_ID);
            verify(bonusRepository).save(bonusToUpdate);
        }

        @Test
        void updateBonusByEmployeeAndBonusIdsShouldThrowResourceNotFoundExceptionWhenBonusIsNotPresent() {
            Bonus updateBonus = BonusTestBuilder.aBonus().build();
            doReturn(Optional.empty()).when(bonusRepository)
                    .findByEmployeeIdAndId(EMPLOYEE_ID, BONUS_ID);

            assertThatThrownBy(() -> bonusService
                    .updateBonusByEmployeeAndBonusIds(EMPLOYEE_ID, BONUS_ID, updateBonus))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

    }

    @Nested
    class DeleteMethodsTest {

        @Test
        void deleteBonusByEmployeeAndBonusIdsShouldCallRepositoryWhenBonusIsPresent() {
            Bonus bonusToDelete = BonusTestBuilder.aBonus().build();
            doReturn(Optional.of(bonusToDelete)).when(bonusRepository)
                    .findByEmployeeIdAndId(EMPLOYEE_ID, BONUS_ID);

            bonusService.deleteBonusByEmployeeAndBonusIds(EMPLOYEE_ID, BONUS_ID);

            verify(bonusRepository).findByEmployeeIdAndId(EMPLOYEE_ID, BONUS_ID);
            verify(bonusRepository).delete(bonusToDelete);
        }

        @Test
        void deleteBonusByEmployeeAndBonusIdsShouldThrowResourceNotFoundExceptionWhenBonusIsNotPresent() {
            doReturn(Optional.empty()).when(bonusRepository)
                    .findByEmployeeIdAndId(EMPLOYEE_ID, BONUS_ID);

            assertThatThrownBy(() -> bonusService.deleteBonusByEmployeeAndBonusIds(EMPLOYEE_ID, BONUS_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

    }

}