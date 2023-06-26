package ru.clevertec.employeeservice.unit.service.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.employeeservice.builder.impl.EmployeeCriteriaTestBuilder;
import ru.clevertec.employeeservice.builder.impl.EmployeeTestBuilder;
import ru.clevertec.employeeservice.criteria.EmployeeCriteria;
import ru.clevertec.employeeservice.entity.Employee;
import ru.clevertec.employeeservice.repository.EmployeeRepository;
import ru.clevertec.employeeservice.service.exception.InvalidDataException;
import ru.clevertec.employeeservice.service.exception.ResourceNotFoundException;
import ru.clevertec.employeeservice.service.impl.EmployeeServiceImpl;
import ru.clevertec.employeeservice.message.Messages;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    private static final Long EMPLOYEE_ID = 1L;

    private static final String EMPLOYEE_PHONE_NUMBER = "123123123";

    private static final String EMPLOYEE_EMAIL = "employee@example.com";

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private Messages messages;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Nested
    class ReadMethodsTest {

        @Test
        void findAllByPageableAndCriteriaShouldReturnExpectedEmployeesAndCallRepository() {
            List<Employee> expectedEmployees = List.of(
                    EmployeeTestBuilder.anEmployee().build(),
                    EmployeeTestBuilder.anEmployee().build(),
                    EmployeeTestBuilder.anEmployee().build()
            );
            Pageable pageable = PageRequest.of(0, 3);
            EmployeeCriteria criteria = EmployeeCriteriaTestBuilder.anEmployeeCriteria().build();
            doReturn(new PageImpl<>(expectedEmployees)).when(employeeRepository)
                    .findAll(any(Specification.class), any(Pageable.class));

            List<Employee> actualEmployees = employeeService.findAllByPageableAndCriteria(pageable, criteria);

            assertThat(actualEmployees).isEqualTo(expectedEmployees);
            verify(employeeRepository).findAll(any(Specification.class), any(Pageable.class));
        }

        @Test
        void findByIdShouldReturnExpectedEmployeeAndCallRepository() {
            Employee expectedEmployee = EmployeeTestBuilder.anEmployee().build();
            doReturn(Optional.of(expectedEmployee)).when(employeeRepository).findById(EMPLOYEE_ID);

            Employee actualEmployee = employeeService.findById(EMPLOYEE_ID);

            assertThat(actualEmployee).isEqualTo(expectedEmployee);
            verify(employeeRepository).findById(EMPLOYEE_ID);
        }

        @Test
        void findByIdShouldThrowResourceNotFoundExceptionWhenEmployeeIsNotPresent() {
            doReturn(Optional.empty()).when(employeeRepository).findById(EMPLOYEE_ID);

            assertThatThrownBy(() -> employeeService.findById(EMPLOYEE_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

    }

    @Nested
    class CreateMethodsTest {

        @Test
        void createEmployeeShouldReturnExpectedEmployeeWhenPhoneAndEmailAreUnique() {
            Employee expectedEmployee = EmployeeTestBuilder.anEmployee()
                    .withEmail(EMPLOYEE_EMAIL)
                    .withPhoneNumber(EMPLOYEE_PHONE_NUMBER)
                    .build();
            doReturn(false).when(employeeRepository).existsByEmail(EMPLOYEE_EMAIL);
            doReturn(false).when(employeeRepository).existsByPhoneNumber(EMPLOYEE_PHONE_NUMBER);
            doReturn(expectedEmployee).when(employeeRepository).save(expectedEmployee);

            Employee actualEmployee = employeeService.createEmployee(expectedEmployee);

            assertThat(actualEmployee).isEqualTo(expectedEmployee);
            verify(employeeRepository).existsByEmail(EMPLOYEE_EMAIL);
            verify(employeeRepository).existsByPhoneNumber(EMPLOYEE_PHONE_NUMBER);
            verify(employeeRepository).save(expectedEmployee);
        }

        @Test
        void createEmployeeShouldThrowInvalidDataExceptionWhenEmailIsNotUnique() {
            Employee employeeToCreate = EmployeeTestBuilder.anEmployee()
                    .withEmail(EMPLOYEE_EMAIL)
                    .withPhoneNumber(EMPLOYEE_PHONE_NUMBER)
                    .build();
            doReturn(true).when(employeeRepository).existsByEmail(EMPLOYEE_EMAIL);

            assertThatThrownBy(() -> employeeService.createEmployee(employeeToCreate))
                    .isInstanceOf(InvalidDataException.class);
        }

        @Test
        void createEmployeeShouldThrowInvalidDataExceptionWhenPhoneNumberIsNotUnique() {
            Employee employeeToCreate = EmployeeTestBuilder.anEmployee()
                    .withEmail(EMPLOYEE_EMAIL)
                    .withPhoneNumber(EMPLOYEE_PHONE_NUMBER)
                    .build();
            doReturn(false).when(employeeRepository).existsByEmail(EMPLOYEE_EMAIL);
            doReturn(true).when(employeeRepository).existsByPhoneNumber(EMPLOYEE_PHONE_NUMBER);

            assertThatThrownBy(() -> employeeService.createEmployee(employeeToCreate))
                    .isInstanceOf(InvalidDataException.class);
        }
    }

    @Nested
    class UpdateMethodsTest {

        @Test
        void updateByIdShouldCallRepositoryWhenEmailAndPhoneNumberAreUniqueAndEmployeeIsPresent() {
            Employee updateEmployee = EmployeeTestBuilder.anEmployee()
                    .withEmail(EMPLOYEE_EMAIL)
                    .withPhoneNumber(EMPLOYEE_PHONE_NUMBER)
                    .build();
            Employee employeeToUpdate = EmployeeTestBuilder.anEmployee().build();
            doReturn(false).when(employeeRepository).existsByEmail(EMPLOYEE_EMAIL);
            doReturn(false).when(employeeRepository).existsByPhoneNumber(EMPLOYEE_PHONE_NUMBER);
            doReturn(Optional.of(employeeToUpdate)).when(employeeRepository).findById(EMPLOYEE_ID);

            employeeService.updateById(EMPLOYEE_ID, updateEmployee);

            verify(employeeRepository).existsByEmail(EMPLOYEE_EMAIL);
            verify(employeeRepository).existsByPhoneNumber(EMPLOYEE_PHONE_NUMBER);
            verify(employeeRepository).findById(EMPLOYEE_ID);
            verify(employeeRepository).save(employeeToUpdate);
        }

        @Test
        void updateByIdShouldThrowInvalidDataExceptionWhenEmailIsNotUnique() {
            Employee updateEmployee = EmployeeTestBuilder.anEmployee()
                    .withEmail(EMPLOYEE_EMAIL)
                    .withPhoneNumber(EMPLOYEE_PHONE_NUMBER)
                    .build();
            doReturn(true).when(employeeRepository).existsByEmail(EMPLOYEE_EMAIL);

            assertThatThrownBy(() -> employeeService.updateById(EMPLOYEE_ID, updateEmployee))
                    .isInstanceOf(InvalidDataException.class);
        }

        @Test
        void updateByIdShouldThrowInvalidDataExceptionWhenPhoneNumberIsNotUnique() {
            Employee updateEmployee = EmployeeTestBuilder.anEmployee()
                    .withEmail(EMPLOYEE_EMAIL)
                    .withPhoneNumber(EMPLOYEE_PHONE_NUMBER)
                    .build();
            doReturn(false).when(employeeRepository).existsByEmail(EMPLOYEE_EMAIL);
            doReturn(true).when(employeeRepository).existsByPhoneNumber(EMPLOYEE_PHONE_NUMBER);

            assertThatThrownBy(() -> employeeService.updateById(EMPLOYEE_ID, updateEmployee))
                    .isInstanceOf(InvalidDataException.class);
        }

        @Test
        void updateByIdShouldThrowResourceNotFoundExceptionWhenEmployeeIsNotPresent() {
            Employee updateEmployee = EmployeeTestBuilder.anEmployee()
                    .withEmail(EMPLOYEE_EMAIL)
                    .withPhoneNumber(EMPLOYEE_PHONE_NUMBER)
                    .build();
            doReturn(false).when(employeeRepository).existsByEmail(EMPLOYEE_EMAIL);
            doReturn(false).when(employeeRepository).existsByPhoneNumber(EMPLOYEE_PHONE_NUMBER);
            doReturn(Optional.empty()).when(employeeRepository).findById(EMPLOYEE_ID);

            assertThatThrownBy(() -> employeeService.updateById(EMPLOYEE_ID, updateEmployee))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    class DeleteMethodsTest {

        @Test
        void deleteByIdShouldCallRepositoryWhenEmployeeIsPresent() {
            Employee employeeToDelete = EmployeeTestBuilder.anEmployee().build();
            doReturn(Optional.of(employeeToDelete)).when(employeeRepository).findById(EMPLOYEE_ID);

            employeeService.deleteById(EMPLOYEE_ID);

            verify(employeeRepository).findById(EMPLOYEE_ID);
            verify(employeeRepository).delete(employeeToDelete);
        }

        @Test
        void deleteByIdShouldThrowResourceNotFoundExceptionWhenEmployeeIsNotPresent() {
            doReturn(Optional.empty()).when(employeeRepository).findById(EMPLOYEE_ID);

            assertThatThrownBy(() -> employeeService.deleteById(EMPLOYEE_ID))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}