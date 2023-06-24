package ru.clevertec.employeeservice.integration.service.impl;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.employeeservice.builder.impl.BonusTestBuilder;
import ru.clevertec.employeeservice.entity.Bonus;
import ru.clevertec.employeeservice.integration.BaseIntegrationTest;
import ru.clevertec.employeeservice.service.BonusService;
import ru.clevertec.employeeservice.service.exception.ResourceNotFoundException;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BonusServiceImplTest extends BaseIntegrationTest {

    private static final Long CORRECT_EMPLOYEE_ID = 1L;

    private static final Long INCORRECT_EMPLOYEE_ID = 1000L;

    private static final Long CORRECT_BONUS_ID = 2L;

    private static final Long INCORRECT_BONUS_ID = 1001L;

    @Autowired
    private BonusService bonusService;

    @Autowired
    private EntityManager entityManager;

    @Nested
    class ReadMethodsTest {

        @Test
        void findAllByEmployeeIdAndPageableShouldReturnResultWithExpectedSize() {
            int expectedSize = 3;
            Pageable pageable = PageRequest.of(0, 3);

            List<Bonus> bonuses = bonusService.findAllByEmployeeIdAndPageable(CORRECT_EMPLOYEE_ID, pageable);

            assertThat(bonuses.size()).isEqualTo(expectedSize);
        }

        @Test
        void findAllByEmployeeIdAndPageableShouldThrowResourceNotFoundExceptionWhenEmployeeIsNotPresent() {
            Pageable pageable = PageRequest.of(0, 3);

            assertThatThrownBy(() -> bonusService.findAllByEmployeeIdAndPageable(INCORRECT_EMPLOYEE_ID, pageable))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void findAllByEmployeeIdAndPageableShouldReturnEmptyResultWhenPageIsGreaterThaBonusesCount() {
            Pageable pageable = PageRequest.of(1000, 3);

            List<Bonus> bonuses = bonusService.findAllByEmployeeIdAndPageable(CORRECT_EMPLOYEE_ID, pageable);

            assertThat(bonuses).isEmpty();
        }

        @Test
        void findByEmployeeAndBonusIdsShouldReturnBonusWithExpectedId() {
            Bonus actualBonus = bonusService.findByEmployeeAndBonusIds(CORRECT_EMPLOYEE_ID, CORRECT_BONUS_ID);

            assertThat(actualBonus.getId()).isEqualTo(CORRECT_BONUS_ID);
        }

        @Test
        void findByEmployeeAndBonusIdsShouldThrowResourceNotFoundExceptionWhenEmployeeIdIsIncorrect() {
            assertThatThrownBy(() -> bonusService.findByEmployeeAndBonusIds(INCORRECT_EMPLOYEE_ID, CORRECT_BONUS_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void findByEmployeeAndBonusIdsShouldThrowResourceNotFoundExceptionWhenBonusIdIsIncorrect() {
            assertThatThrownBy(() -> bonusService.findByEmployeeAndBonusIds(CORRECT_EMPLOYEE_ID, INCORRECT_BONUS_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

    }

    @Nested
    class CreateMethodsTest {

        @Test
        void addBonusToEmployeeShouldReturnCreatedBonusWithNotNullIdAndWithCorrectEmployee() {
            Bonus bonusToCreate = BonusTestBuilder.aBonus()
                    .withAmount(BigDecimal.valueOf(1000.00))
                    .withDescription("Special Achievement Bonus")
                    .withPaymentDate(Date.valueOf("2021-12-1"))
                    .build();

            Bonus actualBonus = bonusService.addBonusToEmployee(CORRECT_EMPLOYEE_ID, bonusToCreate);

            assertThat(actualBonus.getId()).isNotNull();
            assertThat(actualBonus.getEmployee().getId()).isEqualTo(CORRECT_EMPLOYEE_ID);
        }

        @Test
        void addBonusToEmployeeShouldThrowResourceNotFoundExceptionWhenEmployeeNotPresent() {
            Bonus bonusToCreate = BonusTestBuilder.aBonus()
                    .withAmount(BigDecimal.valueOf(1000.00))
                    .withDescription("Special Achievement Bonus")
                    .withPaymentDate(Date.valueOf("2021-12-1"))
                    .build();

            assertThatThrownBy(() -> bonusService.addBonusToEmployee(INCORRECT_EMPLOYEE_ID, bonusToCreate))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

    }

    @Nested
    class UpdateMethodsTest {

        @Test
        void updateBonusByEmployeeAndBonusIdsShouldUpdateBonus() {
            Bonus updatedBonus = BonusTestBuilder.aBonus()
                    .withAmount(BigDecimal.valueOf(1000.00))
                    .withDescription("Special Achievement Bonus")
                    .withPaymentDate(Date.valueOf("2021-12-1"))
                    .build();

            bonusService.updateBonusByEmployeeAndBonusIds(CORRECT_EMPLOYEE_ID, CORRECT_BONUS_ID, updatedBonus);
            entityManager.flush();
        }

        @Test
        void updateBonusByEmployeeAndBonusIdsShouldThrowResourceNotFoundExceptionWhenEmployeeIdIsIncorrect() {
            Bonus updatedBonus = BonusTestBuilder.aBonus()
                    .withAmount(BigDecimal.valueOf(1000.00))
                    .withDescription("Special Achievement Bonus")
                    .withPaymentDate(Date.valueOf("2021-12-1"))
                    .build();

            assertThatThrownBy(() -> bonusService
                    .updateBonusByEmployeeAndBonusIds(INCORRECT_EMPLOYEE_ID, CORRECT_BONUS_ID, updatedBonus))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void updateBonusByEmployeeAndBonusIdsShouldThrowResourceNotFoundExceptionWhenBonusIdIsIncorrect() {
            Bonus updatedBonus = BonusTestBuilder.aBonus()
                    .withAmount(BigDecimal.valueOf(1000.00))
                    .withDescription("Special Achievement Bonus")
                    .withPaymentDate(Date.valueOf("2021-12-1"))
                    .build();

            assertThatThrownBy(() -> bonusService
                    .updateBonusByEmployeeAndBonusIds(CORRECT_EMPLOYEE_ID, INCORRECT_BONUS_ID, updatedBonus))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    class DeleteMethodsTest {

        @Test
        void deleteBonusByEmployeeAndBonusIdsShouldDeleteBonus() {
            bonusService.deleteBonusByEmployeeAndBonusIds(CORRECT_EMPLOYEE_ID, CORRECT_BONUS_ID);
            entityManager.flush();
        }

        @Test
        void deleteBonusByEmployeeAndBonusIdsShouldThrowResourceNotFoundExceptionWhenEmployeeIdIsIncorrect() {
            assertThatThrownBy(() -> bonusService
                    .deleteBonusByEmployeeAndBonusIds(INCORRECT_EMPLOYEE_ID, CORRECT_BONUS_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void deleteBonusByEmployeeAndBonusIdsShouldThrowResourceNotFoundExceptionWhenBonusIdIsIncorrect() {
            assertThatThrownBy(() -> bonusService
                    .deleteBonusByEmployeeAndBonusIds(CORRECT_EMPLOYEE_ID, INCORRECT_BONUS_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

    }

}