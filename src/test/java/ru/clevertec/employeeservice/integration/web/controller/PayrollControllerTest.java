package ru.clevertec.employeeservice.integration.web.controller;

import com.c4_soft.springaddons.security.oauth2.test.annotations.keycloak.WithMockKeycloakAuth;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.employeeservice.integration.BaseIntegrationTest;
import ru.clevertec.employeeservice.web.dto.PayrollDto;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

@AutoConfigureMockMvc
@WithMockKeycloakAuth(authorities = "ROLE_ADMIN")
class PayrollControllerTest extends BaseIntegrationTest {

    private static final Long CORRECT_EMPLOYEE_ID = 1L;

    private static final Long INCORRECT_EMPLOYEE_ID = 1000L;

    private static final Long CORRECT_SALARY_ID = 1L;

    private static final Long INCORRECT_SALARY_ID = 1001L;

    private static final Long CORRECT_PAYROLL_ID = 1L;

    private static final Long INCORRECT_PAYROLL_ID = 1002L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class ReadMethodsTest {

        @Test
        @SneakyThrows
        void findAllByEmployeeAndSalaryIdsAndPageable_ShouldReturnExpectedCountOfPayrolls() {
            int expectedLength = 3;
            String url = fromPath("/api/v0/employees/{employeeId}/salaries/{salaryId}/payrolls")
                    .queryParam("page", 0)
                    .queryParam("size", 3)
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID)
                    .toUriString();

            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.length()").value(expectedLength));
        }

        @Test
        @SneakyThrows
        void findAllByEmployeeAndSalaryIdsAndPageableShouldThrowResourceNotFoundExceptionWhenEmployeeIsNotPresent() {
            String url = fromPath("/api/v0/employees/{employeeId}/salaries/{salaryId}/payrolls")
                    .queryParam("page", 0)
                    .queryParam("size", 3)
                    .buildAndExpand(INCORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID)
                    .toUriString();

            mockMvc.perform(get(url, INCORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").isNotEmpty())
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

        @Test
        @SneakyThrows
        void findAllByEmployeeAndSalaryIdsAndPageableShouldThrowResourceNotFoundExceptionWhenSalaryIsNotPresent() {
            String url = fromPath("/api/v0/employees/{employeeId}/salaries/{salaryId}/payrolls")
                    .queryParam("page", 0)
                    .queryParam("size", 3)
                    .buildAndExpand(INCORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID)
                    .toUriString();

            mockMvc.perform(get(url))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").isNotEmpty())
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

        @Test
        @SneakyThrows
        void findByEmployeeSalaryAndPayrollIdsShouldReturnExpectedPayroll() {
            PayrollDto payrollDto = PayrollDto.builder()
                    .id(CORRECT_PAYROLL_ID)
                    .paymentDate(LocalDate.of(2023, 6, 15))
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}/salaries/{salaryId}/payrolls/{payrollId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID, CORRECT_PAYROLL_ID)
                    .toUriString();

            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(payrollDto.getId()))
                    .andExpect(jsonPath("$.paymentDate").value(payrollDto.getPaymentDate().toString()));
        }

        @Test
        @SneakyThrows
        void findByEmployeeSalaryAndPayrollIdsShouldReturnNotFoundStatusWhenEmployeeIsNotPresent() {
            String url = fromPath("/api/v0/employees/{employeeId}/salaries/{salaryId}/payrolls/{payrollId}")
                    .buildAndExpand(INCORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID, CORRECT_PAYROLL_ID)
                    .toUriString();

            mockMvc.perform(get(url))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").isNotEmpty())
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

        @Test
        @SneakyThrows
        void findByEmployeeSalaryAndPayrollIdsShouldReturnNotFoundStatusWhenSalaryIsNotPresent() {
            String url = fromPath("/api/v0/employees/{employeeId}/salaries/{salaryId}/payrolls/{payrollId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, INCORRECT_SALARY_ID, CORRECT_PAYROLL_ID)
                    .toUriString();

            mockMvc.perform(get(url))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").isNotEmpty())
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

        @Test
        @SneakyThrows
        void findByEmployeeSalaryAndPayrollIdsShouldReturnNotFoundStatusWhenPayrollIsNotPresent() {
            String url = fromPath("/api/v0/employees/{employeeId}/salaries/{salaryId}/payrolls/{payrollId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID, INCORRECT_PAYROLL_ID)
                    .toUriString();

            mockMvc.perform(get(url))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").isNotEmpty())
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

    }

    @Nested
    class CreateMethodsTest {

        @Test
        @SneakyThrows
        void addPayrollToEmployeeSalaryShouldReturnCreatedPayrollWithNotNullId() {
            PayrollDto payrollDto = PayrollDto.builder()
                    .paymentDate(LocalDate.of(2023, 6, 15))
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}/salaries/{salaryId}/payrolls")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID)
                    .toUriString();

            mockMvc.perform(post(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(payrollDto)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").isNotEmpty());
        }

        @Test
        @SneakyThrows
        void addPayrollToEmployeeSalaryShouldReturnNotFoundStatusWhenEmployeeIsNotPresent() {
            PayrollDto payrollDto = PayrollDto.builder()
                    .paymentDate(LocalDate.of(2023, 6, 15))
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}/salaries/{salaryId}/payrolls")
                    .buildAndExpand(INCORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID)
                    .toUriString();

            mockMvc.perform(post(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(payrollDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").isNotEmpty())
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

        @Test
        @SneakyThrows
        void addPayrollToEmployeeSalaryShouldReturnNotFoundStatusWhenSalaryIsNotPresent() {
            PayrollDto payrollDto = PayrollDto.builder()
                    .paymentDate(LocalDate.of(2023, 6, 15))
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}/salaries/{salaryId}/payrolls")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, INCORRECT_SALARY_ID)
                    .toUriString();

            mockMvc.perform(post(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(payrollDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").isNotEmpty())
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

    }

    @Nested
    class UpdateMethodsTest {

        @Test
        @SneakyThrows
        void updatePayrollOfEmployeeSalaryShouldReturnNoContentStatus() {
            PayrollDto payrollDto = PayrollDto.builder()
                    .paymentDate(LocalDate.of(2023, 6, 15))
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}/salaries/{salaryId}/payrolls/{payrollId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID, CORRECT_PAYROLL_ID)
                    .toUriString();

            mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payrollDto)))
                    .andExpect(status().isNoContent());
        }

        @Test
        @SneakyThrows
        void updatePayrollOfEmployeeSalaryShouldReturnNotFoundStatusWhenEmployeeIsNotPresent() {
            PayrollDto payrollDto = PayrollDto.builder()
                    .paymentDate(LocalDate.of(2023, 6, 15))
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}/salaries/{salaryId}/payrolls/{payrollId}")
                    .buildAndExpand(INCORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID, CORRECT_PAYROLL_ID)
                    .toUriString();

            mockMvc.perform(put(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(payrollDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").isNotEmpty())
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

        @Test
        @SneakyThrows
        void updatePayrollOfEmployeeSalaryShouldReturnNotFoundStatusWhenSalaryIsNotPresent() {
            PayrollDto payrollDto = PayrollDto.builder()
                    .paymentDate(LocalDate.of(2023, 6, 15))
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}/salaries/{salaryId}/payrolls/{payrollId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, INCORRECT_SALARY_ID, CORRECT_PAYROLL_ID)
                    .toUriString();

            mockMvc.perform(put(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(payrollDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").isNotEmpty())
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

        @Test
        @SneakyThrows
        void updatePayrollOfEmployeeSalaryShouldReturnNotFoundStatusWhenPayrollIsNotPresent() {
            PayrollDto payrollDto = PayrollDto.builder()
                    .paymentDate(LocalDate.of(2023, 6, 15))
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}/salaries/{salaryId}/payrolls/{payrollId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID, INCORRECT_PAYROLL_ID)
                    .toUriString();

            mockMvc.perform(put(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(payrollDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").isNotEmpty())
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

    }

    @Nested
    class DeleteMethodsTest {

        @Test
        @SneakyThrows
        void deletePayrollOfEmployeeSalaryShouldReturnNoContentStatus() {
            String url = fromPath("/api/v0/employees/{employeeId}/salaries/{salaryId}/payrolls/{payrollId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID, CORRECT_PAYROLL_ID)
                    .toUriString();

            mockMvc.perform(delete(url))
                    .andExpect(status().isNoContent());
        }

        @Test
        @SneakyThrows
        void deletePayrollOfEmployeeSalaryShouldReturnNotFoundStatusWhenEmployeeIsNotPresent() {
            String url = fromPath("/api/v0/employees/{employeeId}/salaries/{salaryId}/payrolls/{payrollId}")
                    .buildAndExpand(INCORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID, CORRECT_PAYROLL_ID)
                    .toUriString();

            mockMvc.perform(delete(url))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").isNotEmpty())
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

        @Test
        @SneakyThrows
        void deletePayrollOfEmployeeSalaryShouldReturnNotFoundStatusWhenSalaryIsNotPresent() {
            String url = fromPath("/api/v0/employees/{employeeId}/salaries/{salaryId}/payrolls/{payrollId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, INCORRECT_SALARY_ID, CORRECT_PAYROLL_ID)
                    .toUriString();

            mockMvc.perform(delete(url))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").isNotEmpty())
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

        @Test
        @SneakyThrows
        void deletePayrollOfEmployeeSalaryShouldReturnNotFoundStatusWhenPayrollIsNotPresent() {
            String url = fromPath("/api/v0/employees/{employeeId}/salaries/{salaryId}/payrolls/{payrollId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID, INCORRECT_PAYROLL_ID)
                    .toUriString();

            mockMvc.perform(delete(url))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").isNotEmpty())
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

    }
}