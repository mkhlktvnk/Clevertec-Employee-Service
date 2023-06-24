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
import ru.clevertec.employeeservice.builder.impl.SalaryTestBuilder;
import ru.clevertec.employeeservice.entity.Employee;
import ru.clevertec.employeeservice.entity.Salary;
import ru.clevertec.employeeservice.message.Messages;
import ru.clevertec.employeeservice.repository.SalaryRepository;
import ru.clevertec.employeeservice.service.EmployeeService;
import ru.clevertec.employeeservice.service.exception.ResourceNotFoundException;
import ru.clevertec.employeeservice.service.impl.SalaryServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SalaryServiceImplTest {

    private static final Long EMPLOYEE_ID = 1L;
    private static final Long SALARY_ID = 2L;

    @Mock
    private SalaryRepository salaryRepository;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private Messages messages;

    @InjectMocks
    private SalaryServiceImpl salaryService;

    @Nested
    class ReadMethodsTest {

        @Test
        void findAllByEmployeeIdAndPageableShouldReturnExpectedSalariesAndCallRepository() {
            List<Salary> expectedSalaries = List.of(
                    SalaryTestBuilder.aSalary().build(),
                    SalaryTestBuilder.aSalary().build(),
                    SalaryTestBuilder.aSalary().build()
            );
            Pageable pageable = PageRequest.of(0, 3);
            doReturn(true).when(employeeService).existsById(EMPLOYEE_ID);
            doReturn(expectedSalaries).when(salaryRepository).findAllByEmployeeId(EMPLOYEE_ID, pageable);

            List<Salary> actualSalaries = salaryService.findAllByEmployeeIdAndPageable(EMPLOYEE_ID, pageable);

            assertThat(actualSalaries).isEqualTo(expectedSalaries);
            verify(salaryRepository).findAllByEmployeeId(EMPLOYEE_ID, pageable);
        }

        @Test
        void findAllByEmployeeIdAndPageableShouldThrowResourceNotFoundExceptionWhenEmployeeIsNotPresent() {
            Pageable pageable = PageRequest.of(0, 3);
            doReturn(false).when(employeeService).existsById(EMPLOYEE_ID);

            assertThatThrownBy(() -> salaryService.findAllByEmployeeIdAndPageable(EMPLOYEE_ID, pageable))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void findByEmployeeAndSalaryIdsShouldReturnExpectedSalaryWhenSalaryIsPresent() {
            Salary expectedSalary = SalaryTestBuilder.aSalary().build();
            doReturn(Optional.of(expectedSalary)).when(salaryRepository).findByEmployeeIdAndId(EMPLOYEE_ID, SALARY_ID);

            Salary actualSalary = salaryService.findByEmployeeAndSalaryIds(EMPLOYEE_ID, SALARY_ID);

            assertThat(actualSalary).isEqualTo(expectedSalary);
            verify(salaryRepository).findByEmployeeIdAndId(EMPLOYEE_ID, SALARY_ID);
        }

        @Test
        void findByEmployeeAndSalaryIdsShouldThrowResourceNotFoundExceptionWhenSalaryIsNotPresent() {
            doReturn(Optional.empty()).when(salaryRepository).findByEmployeeIdAndId(EMPLOYEE_ID, SALARY_ID);

            assertThatThrownBy(() -> salaryService.findByEmployeeAndSalaryIds(EMPLOYEE_ID, SALARY_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

    }

    @Nested
    class CreateMethodsTest {

        @Test
        void addSalaryToEmployeeShouldReturnExpectedSalaryAndCallRepositoryAndService() {
            Salary expectedSalary = SalaryTestBuilder.aSalary().build();
            Employee employeeToAddSalary = EmployeeTestBuilder.anEmployee().build();
            doReturn(employeeToAddSalary).when(employeeService).findById(EMPLOYEE_ID);
            doReturn(expectedSalary).when(salaryRepository).save(expectedSalary);

            Salary actualSalary = salaryService.addSalaryToEmployee(EMPLOYEE_ID, expectedSalary);

            assertThat(actualSalary).isEqualTo(expectedSalary);
            assertThat(actualSalary.getEmployee()).isEqualTo(employeeToAddSalary);
            verify(employeeService).findById(EMPLOYEE_ID);
            verify(salaryRepository).save(expectedSalary);
        }

    }

    @Nested
    class UpdateMethodsTest {

        @Test
        void updateSalaryByEmployeeAndSalaryIdsShouldCallRepositoryWhenSalaryIsPresent() {
            Salary updatedSalary = SalaryTestBuilder.aSalary().build();
            Salary salaryToUpdate = SalaryTestBuilder.aSalary().build();
            doReturn(Optional.of(salaryToUpdate)).when(salaryRepository)
                    .findByEmployeeIdAndId(EMPLOYEE_ID, SALARY_ID);

            salaryService.updateSalaryByEmployeeAndSalaryIds(EMPLOYEE_ID, SALARY_ID, updatedSalary);

            verify(salaryRepository).findByEmployeeIdAndId(EMPLOYEE_ID, SALARY_ID);
            verify(salaryRepository).save(salaryToUpdate);
        }

        @Test
        void updateSalaryByEmployeeAndSalaryIdsShouldThrowResourceNotFoundExceptionWhenSalaryIsNotPresent() {
            Salary updatedSalary = SalaryTestBuilder.aSalary().build();
            doReturn(Optional.empty()).when(salaryRepository).findByEmployeeIdAndId(EMPLOYEE_ID, SALARY_ID);

            assertThatThrownBy(() -> salaryService.updateSalaryByEmployeeAndSalaryIds(EMPLOYEE_ID, SALARY_ID, updatedSalary))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

    }

    @Nested
    class DeleteMethodsTest {

        @Test
        void deleteSalaryByEmployeeAndSalaryIdsShouldCallRepositoryWhenSalaryIsPresent() {
            Salary salaryToDelete = SalaryTestBuilder.aSalary().build();
            doReturn(Optional.of(salaryToDelete)).when(salaryRepository)
                    .findByEmployeeIdAndId(EMPLOYEE_ID, SALARY_ID);

            salaryService.deleteSalaryByEmployeeAndSalaryIds(EMPLOYEE_ID, SALARY_ID);

            verify(salaryRepository).findByEmployeeIdAndId(EMPLOYEE_ID, SALARY_ID);
            verify(salaryRepository).delete(salaryToDelete);
        }

        @Test
        void deleteSalaryByEmployeeAndSalaryIdsShouldThrowResourceNotFoundExceptionWhenSalaryIsNotPresent() {
            doReturn(Optional.empty()).when(salaryRepository).findByEmployeeIdAndId(EMPLOYEE_ID, SALARY_ID);

            assertThatThrownBy(() -> salaryService.deleteSalaryByEmployeeAndSalaryIds(EMPLOYEE_ID, SALARY_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

    }
}

