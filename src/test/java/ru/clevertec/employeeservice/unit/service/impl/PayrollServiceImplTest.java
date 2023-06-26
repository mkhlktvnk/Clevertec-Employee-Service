package ru.clevertec.employeeservice.unit.service.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.employeeservice.builder.impl.PayrollTestBuilder;
import ru.clevertec.employeeservice.builder.impl.SalaryTestBuilder;
import ru.clevertec.employeeservice.entity.Payroll;
import ru.clevertec.employeeservice.entity.Salary;
import ru.clevertec.employeeservice.message.Messages;
import ru.clevertec.employeeservice.repository.PayrollRepository;
import ru.clevertec.employeeservice.service.exception.ResourceNotFoundException;
import ru.clevertec.employeeservice.service.SalaryService;
import ru.clevertec.employeeservice.service.impl.PayrollServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PayrollServiceImplTest {

    private static final Long EMPLOYEE_ID = 1L;
    private static final Long SALARY_ID = 2L;
    private static final Long PAYROLL_ID = 3L;

    @Mock
    private PayrollRepository payrollRepository;

    @Mock
    private SalaryService salaryService;

    @Mock
    private Messages messages;

    @InjectMocks
    private PayrollServiceImpl payrollService;

    @Nested
    class ReadMethodsTest {

        @Test
        void findAllOfSalaryWithPageableShouldReturnExpectedPayrollsAndCallRepository() {
            List<Payroll> expectedPayrolls = List.of(
                    PayrollTestBuilder.aPayroll().build(),
                    PayrollTestBuilder.aPayroll().build(),
                    PayrollTestBuilder.aPayroll().build()
            );
            Pageable pageable = PageRequest.of(0, 3);
            doReturn(expectedPayrolls).when(payrollRepository).findAllByEmployeeAndSalaryIds(EMPLOYEE_ID, SALARY_ID, pageable);
            doReturn(true).when(salaryService).existsByEmployeeAndSalaryIds(EMPLOYEE_ID, SALARY_ID);

            List<Payroll> actualPayrolls = payrollService.findAllOfEmployeeSalaryWithPageable(EMPLOYEE_ID, SALARY_ID, pageable);

            assertThat(actualPayrolls).isEqualTo(expectedPayrolls);
            verify(payrollRepository).findAllByEmployeeAndSalaryIds(EMPLOYEE_ID, SALARY_ID, pageable);
        }

        @Test
        void findAllOfSalaryWithPageableShouldThrowResourceNotFoundExceptionWhenSalaryIsNotPresent() {
            Pageable pageable = PageRequest.of(0, 3);
            doReturn(false).when(salaryService).existsByEmployeeAndSalaryIds(EMPLOYEE_ID, SALARY_ID);

            assertThatThrownBy(() -> payrollService.findAllOfEmployeeSalaryWithPageable(EMPLOYEE_ID, SALARY_ID, pageable))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void findByEmployeeSalaryAndPayrollIdsShouldReturnActualPayrollAndCallRepositoryWhenPayrollIsPresent() {
            Payroll expectedPayroll = PayrollTestBuilder.aPayroll().build();
            doReturn(Optional.of(expectedPayroll)).when(payrollRepository)
                    .findByEmployeeSalaryAndPayrollIds(EMPLOYEE_ID, SALARY_ID, PAYROLL_ID);

            Payroll actualPayroll = payrollService.findByEmployeeSalaryAndPayrollIds(EMPLOYEE_ID, SALARY_ID, PAYROLL_ID);

            assertThat(actualPayroll).isEqualTo(expectedPayroll);
            verify(payrollRepository).findByEmployeeSalaryAndPayrollIds(EMPLOYEE_ID, SALARY_ID, PAYROLL_ID);
        }

        @Test
        void findByEmployeeSalaryAndPayrollIdsShouldThrowResourceNotFoundExceptionWhenPayrollIsNotPresent() {
            doReturn(Optional.empty()).when(payrollRepository)
                    .findByEmployeeSalaryAndPayrollIds(EMPLOYEE_ID, SALARY_ID, PAYROLL_ID);

            assertThatThrownBy(() -> payrollService.findByEmployeeSalaryAndPayrollIds(EMPLOYEE_ID, SALARY_ID, PAYROLL_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    class CreateMethodsTest {

        @Test
        void addPayrollToEmployeeSalaryShouldReturnExpectedPayrollAndCallRepositoryAndService() {
            Payroll expectedPayroll = PayrollTestBuilder.aPayroll().build();
            Salary salaryToAddPayroll = SalaryTestBuilder.aSalary().build();
            doReturn(salaryToAddPayroll).when(salaryService).findByEmployeeAndSalaryIds(EMPLOYEE_ID, SALARY_ID);
            doReturn(expectedPayroll).when(payrollRepository).save(expectedPayroll);

            Payroll actualPayroll = payrollService.addPayrollToEmployeeSalary(EMPLOYEE_ID, SALARY_ID, expectedPayroll);

            assertThat(actualPayroll).isEqualTo(expectedPayroll);
            assertThat(actualPayroll.getSalary()).isEqualTo(salaryToAddPayroll);
            verify(salaryService).findByEmployeeAndSalaryIds(EMPLOYEE_ID, SALARY_ID);
            verify(payrollRepository).save(expectedPayroll);
        }

    }

    @Nested
    class UpdateMethodsTest {

        @Test
        void updatePayrollOfEmployeeSalaryShouldCallRepositoryWhenPayrollIsPresent() {
            Payroll updatedPayroll = PayrollTestBuilder.aPayroll().build();
            Payroll payrollToUpdate = PayrollTestBuilder.aPayroll().build();
            doReturn(Optional.of(payrollToUpdate)).when(payrollRepository)
                    .findByEmployeeSalaryAndPayrollIds(EMPLOYEE_ID, SALARY_ID, PAYROLL_ID);

            payrollService.updatePayrollOfEmployeeSalary(EMPLOYEE_ID, SALARY_ID, PAYROLL_ID, updatedPayroll);

            verify(payrollRepository).findByEmployeeSalaryAndPayrollIds(EMPLOYEE_ID, SALARY_ID, PAYROLL_ID);
            verify(payrollRepository).save(payrollToUpdate);
        }

        @Test
        void updatePayrollOfEmployeeSalaryShouldThrowResourceNotFoundExceptionWhenPayrollIsNotPresent() {
            Payroll updatedPayroll = PayrollTestBuilder.aPayroll().build();
            doReturn(Optional.empty()).when(payrollRepository)
                    .findByEmployeeSalaryAndPayrollIds(EMPLOYEE_ID, SALARY_ID, PAYROLL_ID);

            assertThatThrownBy(() -> payrollService
                    .updatePayrollOfEmployeeSalary(EMPLOYEE_ID, SALARY_ID, PAYROLL_ID, updatedPayroll))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

    }

    @Nested
    class DeleteMethodsTest {

        @Test
        void deletePayrollOfEmployeeSalaryShouldCallRepositoryWhenPayrollIsPresent() {
            Payroll payrollToDelete = PayrollTestBuilder.aPayroll().build();
            doReturn(Optional.of(payrollToDelete)).when(payrollRepository)
                    .findByEmployeeSalaryAndPayrollIds(EMPLOYEE_ID, SALARY_ID, PAYROLL_ID);

            payrollService.deletePayrollOfEmployeeSalary(EMPLOYEE_ID, SALARY_ID, PAYROLL_ID);

            verify(payrollRepository).findByEmployeeSalaryAndPayrollIds(EMPLOYEE_ID, SALARY_ID, PAYROLL_ID);
            verify(payrollRepository).delete(payrollToDelete);
        }

        @Test
        void deletePayrollOfEmployeeSalaryShouldThrowResourceNotFoundExceptionWhenPayrollIsNotPresent() {
            doReturn(Optional.empty()).when(payrollRepository)
                    .findByEmployeeSalaryAndPayrollIds(EMPLOYEE_ID, SALARY_ID, PAYROLL_ID);

            assertThatThrownBy(() -> payrollService.deletePayrollOfEmployeeSalary(EMPLOYEE_ID, SALARY_ID, PAYROLL_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

    }

}
