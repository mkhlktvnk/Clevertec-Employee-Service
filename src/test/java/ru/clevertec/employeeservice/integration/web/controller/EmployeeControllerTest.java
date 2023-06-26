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
import ru.clevertec.employeeservice.entity.Gender;
import ru.clevertec.employeeservice.integration.BaseIntegrationTest;
import ru.clevertec.employeeservice.web.dto.EmployeeDto;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

@AutoConfigureMockMvc
@WithMockKeycloakAuth(authorities = "ROLE_ADMIN")
class EmployeeControllerTest extends BaseIntegrationTest {

    private static final Long CORRECT_EMPLOYEE_ID = 1L;

    private static final Long INCORRECT_EMPLOYEE_ID = 1000L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class ReadMethodsTest {

        @Test
        @SneakyThrows
        void findAllByPageableAndCriteriaShouldReturnOkStatusAndReturnExpectedCountOfEmployees() {
            int expectedLength = 3;
            String url = fromPath("/api/v0/employees")
                    .queryParam("page", 0)
                    .queryParam("size", 3)
                    .queryParam("name", "John")
                    .toUriString();

            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.length()").value(expectedLength));
        }

        @Test
        @SneakyThrows
        void findByIdShouldReturnOkStatusAndExpectedEmployee() {
            EmployeeDto expectedDto = EmployeeDto.builder()
                    .id(CORRECT_EMPLOYEE_ID)
                    .name("John")
                    .surname("Doe")
                    .patronymic("Smith")
                    .email("john.doe@example.com")
                    .phoneNumber("+375291234567")
                    .dateOfBirth(LocalDate.of(1980, 5, 15))
                    .dateOfEmployment(LocalDate.of(2020, 1, 1))
                    .gender(Gender.MALE)
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID)
                    .toUriString();

            mockMvc.perform(get(url))
                    .andExpect(jsonPath("$.id").value(expectedDto.getId()))
                    .andExpect(jsonPath("$.name").value(expectedDto.getName()))
                    .andExpect(jsonPath("$.surname").value(expectedDto.getSurname()))
                    .andExpect(jsonPath("$.patronymic").value(expectedDto.getPatronymic()))
                    .andExpect(jsonPath("$.email").value(expectedDto.getEmail()))
                    .andExpect(jsonPath("$.phoneNumber").value(expectedDto.getPhoneNumber()))
                    .andExpect(jsonPath("$.dateOfBirth").value(expectedDto.getDateOfBirth().toString()))
                    .andExpect(jsonPath("$.dateOfEmployment").value(expectedDto.getDateOfEmployment().toString()))
                    .andExpect(jsonPath("$.gender").value(expectedDto.getGender().name()));
        }

        @Test
        @SneakyThrows
        void findByIdShouldReturnNotFoundStatusAndApiError() {
            String url = fromPath("/api/v0/employees/{employeeId}")
                    .buildAndExpand(INCORRECT_EMPLOYEE_ID)
                    .toUriString();

            mockMvc.perform(get(url))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").value(url))
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

    }

    @Nested
    class CreateMethodsTest {

        @Test
        @SneakyThrows
        void createEmployeeShouldReturnCreatedStatusAndEmployeeWithNotNullId() {
            EmployeeDto dtoToCreate = EmployeeDto.builder()
                    .name("Jane")
                    .surname("Smith")
                    .patronymic("Johnson")
                    .email("jane123@example.com")
                    .phoneNumber("+375299876543")
                    .dateOfBirth(LocalDate.of(1990, 10, 20))
                    .dateOfEmployment(LocalDate.of(2018, 6, 1))
                    .gender(Gender.FEMALE)
                    .build();

            mockMvc.perform(post("/api/v0/employees")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(dtoToCreate)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").isNotEmpty());
        }

        @Test
        @SneakyThrows
        void createEmployeeShouldReturnBadRequestStatusWhenEmailIsNotUnique() {
            EmployeeDto dtoToCreate = EmployeeDto.builder()
                    .name("Jane")
                    .surname("Smith")
                    .patronymic("Johnson")
                    .email("jane.smith@example.com")
                    .phoneNumber("+375299876543")
                    .dateOfBirth(LocalDate.of(1990, 10, 20))
                    .dateOfEmployment(LocalDate.of(2018, 6, 1))
                    .gender(Gender.FEMALE)
                    .build();
            String url = "/api/v0/employees";

            mockMvc.perform(post(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(dtoToCreate)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").value(url))
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

        @Test
        @SneakyThrows
        void createEmployeeShouldReturnBadRequestStatusWhenPhoneNumberIsNotUnique() {
            EmployeeDto dtoToCreate = EmployeeDto.builder()
                    .name("Jane")
                    .surname("Smith")
                    .patronymic("Johnson")
                    .email("jane123@example.com")
                    .phoneNumber("+375291234567")
                    .dateOfBirth(LocalDate.of(1990, 10, 20))
                    .dateOfEmployment(LocalDate.of(2018, 6, 1))
                    .gender(Gender.FEMALE)
                    .build();
            String url = "/api/v0/employees";

            mockMvc.perform(post(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(dtoToCreate)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").value(url))
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

    }

    @Nested
    class UpdateMethodsTest {

        @Test
        @SneakyThrows
        void updateEmployeeByIdShouldReturnNoContentStatus() {
            EmployeeDto updatedEmployee = EmployeeDto.builder()
                    .name("Jane")
                    .surname("Smith")
                    .patronymic("Johnson")
                    .email("jane123@example.com")
                    .phoneNumber("+375299876543")
                    .dateOfBirth(LocalDate.of(1990, 10, 20))
                    .dateOfEmployment(LocalDate.of(2018, 6, 1))
                    .gender(Gender.FEMALE)
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID)
                    .toUriString();

            mockMvc.perform(put(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(updatedEmployee)))
                    .andExpect(status().isNoContent());
        }

        @Test
        @SneakyThrows
        void updateEmployeeByIdShouldReturnNotFoundStatusWhenEmployeeIsNotPresent() {
            EmployeeDto updatedEmployee = EmployeeDto.builder()
                    .name("Jane")
                    .surname("Smith")
                    .patronymic("Johnson")
                    .email("jane123@example.com")
                    .phoneNumber("+375299876543")
                    .dateOfBirth(LocalDate.of(1990, 10, 20))
                    .dateOfEmployment(LocalDate.of(2018, 6, 1))
                    .gender(Gender.FEMALE)
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}")
                    .buildAndExpand(INCORRECT_EMPLOYEE_ID)
                    .toUriString();

            mockMvc.perform(put(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(updatedEmployee)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").value(url))
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

        @Test
        @SneakyThrows
        void updateEmployeeByIdShouldReturnBadRequestStatusWhenEmailIsNotUnique() {
            EmployeeDto updatedEmployee = EmployeeDto.builder()
                    .name("Jane")
                    .surname("Smith")
                    .patronymic("Johnson")
                    .email("jane.smith@example.com")
                    .phoneNumber("+375299876543")
                    .dateOfBirth(LocalDate.of(1990, 10, 20))
                    .dateOfEmployment(LocalDate.of(2018, 6, 1))
                    .gender(Gender.FEMALE)
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}")
                    .buildAndExpand(INCORRECT_EMPLOYEE_ID)
                    .toUriString();

            mockMvc.perform(put(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(updatedEmployee)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").value(url))
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

        @Test
        @SneakyThrows
        void updateEmployeeByIdShouldReturnBadRequestStatusWhenPhoneNumberIsNotUnique() {
            EmployeeDto updatedEmployee = EmployeeDto.builder()
                    .name("Jane")
                    .surname("Smith")
                    .patronymic("Johnson")
                    .email("jane123@example.com")
                    .phoneNumber("+375291234567")
                    .dateOfBirth(LocalDate.of(1990, 10, 20))
                    .dateOfEmployment(LocalDate.of(2018, 6, 1))
                    .gender(Gender.FEMALE)
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}")
                    .buildAndExpand(INCORRECT_EMPLOYEE_ID)
                    .toUriString();

            mockMvc.perform(put(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(updatedEmployee)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").value(url))
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

    }

    @Nested
    class DeleteMethodsTest {

        @Test
        @SneakyThrows
        void deleteEmployeeByIdShouldReturnNoContentStatus() {
            String url = fromPath("/api/v0/employees/{employeeId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID)
                    .toUriString();

            mockMvc.perform(delete(url))
                    .andExpect(status().isNoContent());
        }

        @Test
        @SneakyThrows
        void deleteEmployeeByIdShouldReturnNotFoundStatusWhenEmployeeIsNotPresent() {
            String url = fromPath("/api/v0/employees/{employeeId}")
                    .buildAndExpand(INCORRECT_EMPLOYEE_ID)
                    .toUriString();

            mockMvc.perform(delete(url))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").value(url))
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

    }

}