package ru.clevertec.employeeservice.integration.service.impl;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.employeeservice.builder.impl.SalaryTestBuilder;
import ru.clevertec.employeeservice.entity.Salary;
import ru.clevertec.employeeservice.integration.BaseIntegrationTest;
import ru.clevertec.employeeservice.service.SalaryService;
import ru.clevertec.employeeservice.service.exception.ResourceNotFoundException;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SalaryServiceImplTest extends BaseIntegrationTest {

    private static final Long CORRECT_EMPLOYEE_ID = 1L;
    private static final Long INCORRECT_EMPLOYEE_ID = 1000L;
    private static final Long CORRECT_SALARY_ID = 2L;
    private static final Long INCORRECT_SALARY_ID = 1001L;

    @Autowired
    private SalaryService salaryService;

    @Autowired
    private EntityManager entityManager;

    @Nested
    class ReadMethodsTest {

        @Test
        void findAllByEmployeeIdAndPageableShouldReturnResultWithExpectedSize() {
            int expectedSize = 3;
            Pageable pageable = PageRequest.of(0, 3);

            List<Salary> salaries = salaryService.findAllByEmployeeIdAndPageable(CORRECT_EMPLOYEE_ID, pageable);

            assertThat(salaries.size()).isEqualTo(expectedSize);
        }

        @Test
        void findAllByEmployeeIdAndPageableShouldThrowResourceNotFoundExceptionWhenEmployeeIsNotPresent() {
            Pageable pageable = PageRequest.of(0, 3);

            assertThatThrownBy(() -> salaryService.findAllByEmployeeIdAndPageable(INCORRECT_EMPLOYEE_ID, pageable))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void findByEmployeeAndSalaryIdsShouldReturnCorrectSalary() {
            Salary actualSalary = salaryService.findByEmployeeAndSalaryIds(CORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID);

            assertThat(actualSalary.getId()).isEqualTo(CORRECT_SALARY_ID);
            assertThat(actualSalary.getEmployee().getId()).isEqualTo(CORRECT_EMPLOYEE_ID);
        }

        @Test
        void findByEmployeeAndSalaryIdsShouldThrowResourceNotFoundExceptionWhenEmployeeIdIsIncorrect() {
            assertThatThrownBy(() -> salaryService
                    .findByEmployeeAndSalaryIds(INCORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void findByEmployeeAndSalaryIdsShouldThrowResourceNotFoundExceptionWhenSalaryIdIsIncorrect() {
            assertThatThrownBy(() -> salaryService
                    .findByEmployeeAndSalaryIds(CORRECT_EMPLOYEE_ID, INCORRECT_SALARY_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    class CreateMethodsTest {

        @Test
        void addSalaryToEmployeeShouldReturnCreatedSalaryWithNotNullIdAndWithCorrectEmployee() {
            Salary salaryToCreate = SalaryTestBuilder.aSalary()
                    .withAmount(BigDecimal.valueOf(6000))
                    .withStartDate(Date.valueOf("2023-06-01"))
                    .withEndDate(Date.valueOf("2023-06-30"))
                    .withCurrentSalary(false)
                    .build();

            Salary actualSalary = salaryService.addSalaryToEmployee(CORRECT_EMPLOYEE_ID, salaryToCreate);

            assertThat(actualSalary.getId()).isNotNull();
            assertThat(actualSalary.getEmployee().getId()).isEqualTo(CORRECT_EMPLOYEE_ID);
        }

        @Test
        void addSalaryToEmployeeShouldThrowResourceNotFoundExceptionWhenEmployeeNotPresent() {
            Salary salaryToCreate = SalaryTestBuilder.aSalary()
                    .withAmount(BigDecimal.valueOf(6000))
                    .withStartDate(Date.valueOf("2023-06-01"))
                    .withEndDate(Date.valueOf("2023-06-30"))
                    .withCurrentSalary(false)
                    .build();

            assertThatThrownBy(() -> salaryService.addSalaryToEmployee(INCORRECT_EMPLOYEE_ID, salaryToCreate))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

    }

    @Nested
    class UpdateMethodsTest {

        @Test
        void updateSalaryByEmployeeAndSalaryIdsShouldUpdateSalary() {
            Salary updatedSalary = SalaryTestBuilder.aSalary()
                    .withAmount(BigDecimal.valueOf(6000))
                    .withStartDate(Date.valueOf("2023-06-01"))
                    .withEndDate(Date.valueOf("2023-06-30"))
                    .withCurrentSalary(false)
                    .build();

            salaryService.updateSalaryByEmployeeAndSalaryIds(CORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID, updatedSalary);
            entityManager.flush();
        }

        @Test
        void updateSalaryByEmployeeAndSalaryIdsShouldThrowResourceNotFoundExceptionWhenEmployeeIdIsIncorrect() {
            Salary updatedSalary = SalaryTestBuilder.aSalary()
                    .withAmount(BigDecimal.valueOf(6000))
                    .withStartDate(Date.valueOf("2023-06-01"))
                    .withEndDate(Date.valueOf("2023-06-30"))
                    .withCurrentSalary(false)
                    .build();

            assertThatThrownBy(() -> salaryService
                    .updateSalaryByEmployeeAndSalaryIds(INCORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID, updatedSalary))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void updateSalaryByEmployeeAndSalaryIdsShouldThrowResourceNotFoundExceptionWhenSalaryIdIsIncorrect() {
            Salary updatedSalary = SalaryTestBuilder.aSalary()
                    .withAmount(BigDecimal.valueOf(6000))
                    .withStartDate(Date.valueOf("2023-06-01"))
                    .withEndDate(Date.valueOf("2023-06-30"))
                    .withCurrentSalary(false)
                    .build();

            assertThatThrownBy(() -> salaryService
                    .updateSalaryByEmployeeAndSalaryIds(CORRECT_EMPLOYEE_ID, INCORRECT_SALARY_ID, updatedSalary))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

    }

    @Nested
    class DeleteMethodsTest {

        @Test
        void deleteSalaryByEmployeeAndSalaryIdsShouldDeleteSalary() {
            salaryService.deleteSalaryByEmployeeAndSalaryIds(CORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID);
            entityManager.flush();
        }

        @Test
        void deleteSalaryByEmployeeAndSalaryIdsShouldThrowResourceNotFoundExceptionWhenEmployeeIdIsIncorrect() {
            assertThatThrownBy(() -> salaryService
                    .deleteSalaryByEmployeeAndSalaryIds(INCORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void deleteSalaryByEmployeeAndSalaryIdsShouldThrowResourceNotFoundExceptionWhenSalaryIdIsIncorrect() {
            assertThatThrownBy(() -> salaryService
                    .deleteSalaryByEmployeeAndSalaryIds(CORRECT_EMPLOYEE_ID, INCORRECT_SALARY_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

    }
}
