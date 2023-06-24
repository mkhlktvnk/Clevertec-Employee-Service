package ru.clevertec.employeeservice.integration.service.impl;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.employeeservice.builder.impl.EmployeeCriteriaTestBuilder;
import ru.clevertec.employeeservice.criteria.EmployeeCriteria;
import ru.clevertec.employeeservice.entity.Employee;
import ru.clevertec.employeeservice.integration.BaseIntegrationTest;
import ru.clevertec.employeeservice.provider.EmployeeTestDataProvider;
import ru.clevertec.employeeservice.service.EmployeeService;
import ru.clevertec.employeeservice.service.exception.InvalidDataException;
import ru.clevertec.employeeservice.service.exception.ResourceNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EmployeeServiceImplTest extends BaseIntegrationTest {

    private static final Long CORRECT_EMPLOYEE_ID = 1L;

    private static final Long INCORRECT_EMPLOYEE_ID = 1000L;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EntityManager entityManager;

    @Nested
    class ReadMethodsTest {

        @Test
        void findAllByPageableAndCriteriaShouldReturnCorrectNumberOfEmployees() {
            int expectedSize = 3;
            Pageable pageable = PageRequest.of(0, 3);
            EmployeeCriteria criteria = EmployeeCriteriaTestBuilder.anEmployeeCriteria()
                    .withName("John")
                    .build();

            List<Employee> employees = employeeService.findAllByPageableAndCriteria(pageable, criteria);

            assertThat(employees.size()).isEqualTo(expectedSize);
        }

        @Test
        void findAllByPageableAndCriteriaShouldReturnEmptyResultWhenPageIsGreaterThanEmployeesNumber() {
            Pageable pageable = PageRequest.of(100, 3);
            EmployeeCriteria criteria = EmployeeCriteriaTestBuilder.anEmployeeCriteria().build();
            List<Employee> employees = employeeService.findAllByPageableAndCriteria(pageable, criteria);

            assertThat(employees).isEmpty();
        }

        @Test
        void findByIdShouldReturnCorrectResultWhenEmployeeIsPresent() {
            Employee employee = employeeService.findById(CORRECT_EMPLOYEE_ID);

            assertThat(employee.getId()).isEqualTo(CORRECT_EMPLOYEE_ID);
        }

        @Test
        void findByIdShouldThrowEntityNotFoundExceptionWhenEmployeeIsNotPresent() {
            assertThatThrownBy(() -> employeeService.findById(INCORRECT_EMPLOYEE_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

    }

    @Nested
    class CreateMethodsTest {

        @Test
        void createEmployeeShouldReturnCreatedEmployeeWithNotNullIdWhenEmailAndPhoneNumberAreUnique() {
            Employee employeeToCreate = EmployeeTestDataProvider.getValidEmployee();

            Employee actualEmployee = employeeService.createEmployee(employeeToCreate);

            assertThat(actualEmployee.getId()).isNotNull();
        }

        @Test
        void createEmployeeShouldThrowInvalidDataExceptionWhenEmailIsNotUnique() {
            Employee employeeToCreate = EmployeeTestDataProvider.getEmployeeWithNotUniqueEmail();

            assertThatThrownBy(() -> employeeService.createEmployee(employeeToCreate))
                    .isInstanceOf(InvalidDataException.class);
        }

        @Test
        void createEmployeeShouldThrowInvalidDataExceptionWhenPhoneNumberIsNotUnique() {
            Employee employeeToCreate = EmployeeTestDataProvider.getEmployeeWithNotUniquePhoneNumber();

            assertThatThrownBy(() -> employeeService.createEmployee(employeeToCreate))
                    .isInstanceOf(InvalidDataException.class);
        }

    }

    @Nested
    class UpdateMethodsTest {

        @Test
        void updateByIdShouldUpdateEmployeeWhenDataIsCorrect() {
            Employee updatedEmployee = EmployeeTestDataProvider.getValidEmployee();

            employeeService.updateById(CORRECT_EMPLOYEE_ID, updatedEmployee);
            entityManager.flush();
        }

        @Test
        void updateByIdShouldThrowResourceNotFoundExceptionWhenEmployeeIsNotPresent() {
            Employee updatedEmployee = EmployeeTestDataProvider.getValidEmployee();

            assertThatThrownBy(() -> employeeService.updateById(INCORRECT_EMPLOYEE_ID, updatedEmployee))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void updateByIdShouldThrowInvalidDataExceptionWhenEmailIsNotUnique() {
            Employee updatedEmployee = EmployeeTestDataProvider.getEmployeeWithNotUniqueEmail();

            assertThatThrownBy(() -> employeeService.updateById(INCORRECT_EMPLOYEE_ID, updatedEmployee))
                    .isInstanceOf(InvalidDataException.class);
        }
    }

    @Nested
    class DeleteMethodsTest {

        @Test
        void deleteByIdShouldDeleteEmployeeWhenEmployeeIsPresent() {
            employeeService.deleteById(CORRECT_EMPLOYEE_ID);
            entityManager.flush();
        }

        @Test
        void deleteByIdShouldThrowResourceNotFoundExceptionWhenEmployeeIsNotPresent() {
            assertThatThrownBy(() -> employeeService.deleteById(INCORRECT_EMPLOYEE_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

    }

}
