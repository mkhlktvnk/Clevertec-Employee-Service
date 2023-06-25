package ru.clevertec.employeeservice.integration.service.impl;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.employeeservice.builder.impl.PayrollTestBuilder;
import ru.clevertec.employeeservice.entity.Payroll;
import ru.clevertec.employeeservice.integration.BaseIntegrationTest;
import ru.clevertec.employeeservice.service.PayrollService;
import ru.clevertec.employeeservice.service.exception.ResourceNotFoundException;

import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PayrollServiceImplTest extends BaseIntegrationTest {

    private static final Long CORRECT_PAYROLL_ID = 1L;

    private static final Long INCORRECT_PAYROLL_ID = 1000L;

    private static final Long CORRECT_SALARY_ID = 1L;

    private static final Long INCORRECT_SALARY_ID = 2000L;

    private static final Long CORRECT_EMPLOYEE_ID = 1L;

    private static final Long INCORRECT_EMPLOYEE_ID = 3000L;

    @Autowired
    private PayrollService payrollService;

    @Autowired
    private EntityManager entityManager;


    @Nested
    class ReadMethodsTest {

        @Test
        void findAllOfSalaryWithPageableShouldReturnExpectedCountOfEmployees() {
            int expectedSize = 3;
            Pageable pageable = PageRequest.of(0, 3);
            List<Payroll> actualPayrolls = payrollService
                    .findAllOfEmployeeSalaryWithPageable(CORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID, pageable);

            assertThat(actualPayrolls.size()).isEqualTo(expectedSize);
        }

        @Test
        void findByEmployeeSalaryAndPayrollIdsShouldReturnExpectedPayroll() {
            Payroll actualPayroll = payrollService
                    .findByEmployeeSalaryAndPayrollIds(CORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID, CORRECT_PAYROLL_ID);

            assertThat(actualPayroll.getId()).isEqualTo(CORRECT_PAYROLL_ID);
            assertThat(actualPayroll.getSalary().getId()).isEqualTo(CORRECT_SALARY_ID);
            assertThat(actualPayroll.getSalary().getEmployee().getId()).isEqualTo(CORRECT_EMPLOYEE_ID);
        }

        @Test
        void findByEmployeeSalaryAndPayrollsIdsShouldThrowResourceNotFoundExceptionWhenEmployeeIdIsIncorrect() {
            assertThatThrownBy(() -> payrollService
                    .findByEmployeeSalaryAndPayrollIds(INCORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID, CORRECT_PAYROLL_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void findByEmployeeSalaryAndPayrollsIdsShouldThrowResourceNotFoundExceptionWhenSalaryIdIsIncorrect() {
            assertThatThrownBy(() -> payrollService
                    .findByEmployeeSalaryAndPayrollIds(CORRECT_EMPLOYEE_ID, INCORRECT_SALARY_ID, CORRECT_PAYROLL_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void findByEmployeeSalaryAndPayrollsIdsShouldThrowResourceNotFoundExceptionWhenPayrollIdIsIncorrect() {
            assertThatThrownBy(() -> payrollService
                    .findByEmployeeSalaryAndPayrollIds(CORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID, INCORRECT_PAYROLL_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

    }

    @Nested
    class CreateMethodsTest {

        @Test
        void addPayrollToEmployeeSalaryShouldReturnCreatedPayrollWhenEmployeeAndSalaryArePresent() {
            Payroll payrollToCreate = PayrollTestBuilder.aPayroll()
                    .withPaymentDate(Date.valueOf("2023-12-11"))
                    .build();

            Payroll createdPayroll = payrollService
                    .addPayrollToEmployeeSalary(CORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID, payrollToCreate);

            assertThat(createdPayroll.getId()).isNotNull();
            assertThat(createdPayroll.getSalary().getId()).isEqualTo(CORRECT_SALARY_ID);
            assertThat(createdPayroll.getSalary().getEmployee().getId()).isEqualTo(CORRECT_EMPLOYEE_ID);
        }

        @Test
        void addPayrollToEmployeeShouldThrowResourceNotFoundExceptionWhenEmployeeIdIsNotCorrect() {
            Payroll payrollToCreate = PayrollTestBuilder.aPayroll()
                    .withPaymentDate(Date.valueOf("2023-12-11"))
                    .build();

            assertThatThrownBy(() -> payrollService
                    .addPayrollToEmployeeSalary(INCORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID, payrollToCreate))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void addPayrollToEmployeeShouldThrowResourceNotFoundExceptionWhenSalaryIdIsNotCorrect() {
            Payroll payrollToCreate = PayrollTestBuilder.aPayroll()
                    .withPaymentDate(Date.valueOf("2023-12-11"))
                    .build();

            assertThatThrownBy(() -> payrollService
                    .addPayrollToEmployeeSalary(CORRECT_EMPLOYEE_ID, INCORRECT_SALARY_ID, payrollToCreate))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

    }

    @Nested
    class UpdateMethodsTest {

        @Test
        void updatePayrollOfEmployeeSalaryShouldUpdatePayroll() {
            Payroll updatedPayroll = PayrollTestBuilder.aPayroll()
                    .withPaymentDate(Date.valueOf("2023-12-11"))
                    .build();

            payrollService.updatePayrollOfEmployeeSalary(
                    CORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID, CORRECT_PAYROLL_ID, updatedPayroll
            );
            entityManager.flush();
        }

        @Test
        void updatePayrollOfEmployeeSalaryShouldThrowResourceNotFoundExceptionWhenEmployeeIsNotPresent() {
            Payroll updatedPayroll = PayrollTestBuilder.aPayroll()
                    .withPaymentDate(Date.valueOf("2023-12-11"))
                    .build();

            assertThatThrownBy(() -> payrollService.updatePayrollOfEmployeeSalary(
                    INCORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID, CORRECT_PAYROLL_ID, updatedPayroll))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void updatePayrollOfEmployeeSalaryShouldThrowResourceNotFoundExceptionWhenSalaryIsNotPresent() {
            Payroll updatedPayroll = PayrollTestBuilder.aPayroll()
                    .withPaymentDate(Date.valueOf("2023-12-11"))
                    .build();

            assertThatThrownBy(() -> payrollService.updatePayrollOfEmployeeSalary(
                    CORRECT_EMPLOYEE_ID, INCORRECT_SALARY_ID, CORRECT_PAYROLL_ID, updatedPayroll))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void updatePayrollOfEmployeeSalaryShouldThrowResourceNotFoundExceptionWhenPayrollIsNotPresent() {
            Payroll updatedPayroll = PayrollTestBuilder.aPayroll()
                    .withPaymentDate(Date.valueOf("2023-12-11"))
                    .build();

            assertThatThrownBy(() -> payrollService.updatePayrollOfEmployeeSalary(
                    CORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID, INCORRECT_PAYROLL_ID, updatedPayroll))
                    .isInstanceOf(ResourceNotFoundException.class);
        }


    }

    @Nested
    class DeleteMethodsTest {
        @Test
        void deletePayrollOfEmployeeSalaryShouldDeletePayroll() {
            payrollService.deletePayrollOfEmployeeSalary(CORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID, CORRECT_PAYROLL_ID);
            entityManager.flush();
        }

        @Test
        void deletePayrollOfEmployeeSalaryShouldThrowResourceNotFoundExceptionWhenEmployeeIsNotPresent() {
            assertThatThrownBy(() -> payrollService.deletePayrollOfEmployeeSalary(
                    INCORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID, CORRECT_PAYROLL_ID))
                    .isInstanceOf(ResourceNotFoundException.class);

        }

        @Test
        void deletePayrollOfEmployeeSalaryShouldThrowResourceNotFoundExceptionWhenSalaryIsNotPresent() {
            assertThatThrownBy(() -> payrollService.deletePayrollOfEmployeeSalary(
                    CORRECT_EMPLOYEE_ID, INCORRECT_SALARY_ID, CORRECT_PAYROLL_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void deletePayrollOfEmployeeSalaryShouldThrowResourceNotFoundExceptionWhenPayrollIsNotPresent() {
            assertThatThrownBy(() -> payrollService.deletePayrollOfEmployeeSalary(
                    CORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID, INCORRECT_PAYROLL_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

    }
}