package ru.clevertec.employeeservice.integration.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.employeeservice.integration.BaseIntegrationTest;
import ru.clevertec.employeeservice.web.dto.SalaryDto;

import java.math.BigDecimal;
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
public class SalaryControllerTest extends BaseIntegrationTest {

    private static final Long CORRECT_EMPLOYEE_ID = 1L;

    private static final Long INCORRECT_EMPLOYEE_ID = 1000L;

    private static final Long CORRECT_SALARY_ID = 2L;

    private static final Long INCORRECT_SALARY_ID = 1001L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class ReadMethodsTest {

        @Test
        @SneakyThrows
        void findAllByEmployeeIdAndPageableShouldReturnExpectedCountOfSalaries() {
            int expectedLength = 3;
            String url = fromPath("/api/v0/employees/{employeeId}/salaries")
                    .queryParam("page", 0)
                    .queryParam("size", 3)
                    .buildAndExpand(CORRECT_EMPLOYEE_ID)
                    .toUriString();

            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.length()").value(expectedLength));
        }

        @Test
        @SneakyThrows
        void findAllByEmployeeIdAndPageableShouldThrowResourceNotFoundExceptionWhenEmployeeIsNotPresent() {
            String url = fromPath("/api/v0/employees/{employeeId}/salaries")
                    .queryParam("page", 0)
                    .queryParam("size", 3)
                    .buildAndExpand(INCORRECT_EMPLOYEE_ID)
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
        void findByEmployeeAndSalaryIdsShouldReturnOkStatusAndExpectedSalary() {
            SalaryDto salaryDto = SalaryDto.builder()
                    .id(CORRECT_SALARY_ID)
                    .amount(BigDecimal.valueOf(4000.00))
                    .startDate(LocalDate.of(2022, 1, 1))
                    .endDate(LocalDate.of(2023, 1, 1))
                    .currentSalary(false)
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}/salaries/{salaryId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID)
                    .toUriString();

            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(CORRECT_SALARY_ID))
                    .andExpect(jsonPath("$.startDate").value(salaryDto.getStartDate().toString()))
                    .andExpect(jsonPath("$.endDate").value(salaryDto.getEndDate().toString()));
        }

        @Test
        @SneakyThrows
        void findByEmployeeAndSalaryIdsShouldReturnNotFoundStatusWhenEmployeeIsNotPresent() {
            String url = fromPath("/api/v0/employees/{employeeId}/salaries/{salaryId}")
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
        void findByEmployeeAndSalaryIdsShouldReturnNotFoundStatusWhenSalaryIsNotPresent() {
            String url = fromPath("/api/v0/employees/{employeeId}/salaries/{salaryId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, INCORRECT_SALARY_ID)
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
        void addSalaryToEmployeeShouldReturnCreatedStatusAndSalaryWithNotNullId() {
            SalaryDto salaryDto = SalaryDto.builder()
                    .id(CORRECT_SALARY_ID)
                    .amount(BigDecimal.valueOf(7000.00))
                    .startDate(LocalDate.of(2024, 1, 1))
                    .endDate(LocalDate.of(2025, 1, 1))
                    .currentSalary(false)
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}/salaries")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID)
                    .toUriString();

            mockMvc.perform(post(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(salaryDto)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").isNotEmpty());
        }

        @Test
        @SneakyThrows
        void addLeaveToEmployeeShouldReturnNotFoundStatusWhenEmployeeIsNotPresent() {
            SalaryDto salaryDto = SalaryDto.builder()
                    .id(CORRECT_SALARY_ID)
                    .amount(BigDecimal.valueOf(7000.00))
                    .startDate(LocalDate.of(2024, 1, 1))
                    .endDate(LocalDate.of(2025, 1, 1))
                    .currentSalary(false)
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}/salaries")
                    .buildAndExpand(INCORRECT_EMPLOYEE_ID)
                    .toUriString();

            mockMvc.perform(post(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(salaryDto)))
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
        void updateSalaryByEmployeeAndSalaryIdsShouldReturnNoContentStatus() {
            SalaryDto salaryDto = SalaryDto.builder()
                    .id(CORRECT_SALARY_ID)
                    .amount(BigDecimal.valueOf(7000.00))
                    .startDate(LocalDate.of(2024, 1, 1))
                    .endDate(LocalDate.of(2025, 1, 1))
                    .currentSalary(false)
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}/salaries/{salaryId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID)
                    .toUriString();

            mockMvc.perform(put(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(salaryDto)))
                    .andExpect(status().isNoContent());
        }

        @Test
        @SneakyThrows
        void updateSalaryByEmployeeAndSalaryIdsShouldReturnNotFoundStatusWhenEmployeeIsNotPresent() {
            SalaryDto salaryDto = SalaryDto.builder()
                    .id(CORRECT_SALARY_ID)
                    .amount(BigDecimal.valueOf(7000.00))
                    .startDate(LocalDate.of(2024, 1, 1))
                    .endDate(LocalDate.of(2025, 1, 1))
                    .currentSalary(false)
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}/salaries/{salaryId}")
                    .buildAndExpand(INCORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID)
                    .toUriString();

            mockMvc.perform(put(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(salaryDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").isNotEmpty())
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

        @Test
        @SneakyThrows
        void updateSalaryByEmployeeAndSalaryIdsShouldReturnNotFoundStatusWhenSalaryIsNotPresent() {
            SalaryDto salaryDto = SalaryDto.builder()
                    .id(CORRECT_SALARY_ID)
                    .amount(BigDecimal.valueOf(7000.00))
                    .startDate(LocalDate.of(2024, 1, 1))
                    .endDate(LocalDate.of(2025, 1, 1))
                    .currentSalary(false)
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}/salaries/{salaryId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, INCORRECT_SALARY_ID)
                    .toUriString();

            mockMvc.perform(put(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(salaryDto)))
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
        void deleteSalaryByEmployeeAndSalaryIdsShouldReturnNoContentStatus() {
            String url = fromPath("/api/v0/employees/{employeeId}/salaries/{salaryId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID)
                    .toUriString();

            mockMvc.perform(delete(url))
                    .andExpect(status().isNoContent());
        }

        @Test
        @SneakyThrows
        void deleteSalaryByEmployeeAndSalaryIdsShouldReturnNotFoundStatusWhenEmployeeIsNotPresent() {
            String url = fromPath("/api/v0/employees/{employeeId}/salaries/{salaryId}")
                    .buildAndExpand(INCORRECT_EMPLOYEE_ID, CORRECT_SALARY_ID)
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
        void deleteSalaryByEmployeeAndSalaryIdsShouldReturnNotFoundStatusWhenSalaryIsNotPresent() {
            String url = fromPath("/api/v0/employees/{employeeId}/salaries/{salaryId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, INCORRECT_SALARY_ID)
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
