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
import ru.clevertec.employeeservice.web.dto.LeaveDto;

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
class LeaveControllerTest extends BaseIntegrationTest {

    private static final Long CORRECT_EMPLOYEE_ID = 1L;

    private static final Long INCORRECT_EMPLOYEE_ID = 1000L;

    private static final Long CORRECT_LEAVE_ID = 2L;

    private static final Long INCORRECT_LEAVE_ID = 1001L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class ReadMethodsTest {

        @Test
        @SneakyThrows
        void findAllByEmployeeIdAndPageableShouldReturnExpectedCountOfLeaves() {
            int expectedLength = 3;
            String url = fromPath("/api/v0/employees/{employeeId}/leaves")
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
            String url = fromPath("/api/v0/employees/{employeeId}/leaves")
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
        void findByEmployeeAndLeaveIdsShouldReturnOkStatusAndExpectedLeave() {
            LeaveDto leaveDto = LeaveDto.builder()
                    .startDate(LocalDate.of(2023, 7, 5))
                    .endDate(LocalDate.of(2023, 7, 8))
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}/leaves/{leaveId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, CORRECT_LEAVE_ID)
                    .toUriString();

            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(CORRECT_LEAVE_ID))
                    .andExpect(jsonPath("$.startDate").value(leaveDto.getStartDate().toString()))
                    .andExpect(jsonPath("$.endDate").value(leaveDto.getEndDate().toString()));
        }

        @Test
        @SneakyThrows
        void findByEmployeeAndLeaveIdsShouldReturnNotFoundStatusWhenEmployeeIsNotPresent() {
            String url = fromPath("/api/v0/employees/{employeeId}/leaves/{leaveId}")
                    .buildAndExpand(INCORRECT_EMPLOYEE_ID, CORRECT_LEAVE_ID)
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
        void findByEmployeeAndLeaveIdsShouldReturnNotFoundStatusWhenLeaveIsNotPresent() {
            String url = fromPath("/api/v0/employees/{employeeId}/leaves/{leaveId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, INCORRECT_LEAVE_ID)
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
        void addLeaveToEmployeeShouldReturnCreatedStatusAndLeaveWithNotNullId() {
            LeaveDto leaveToAdd = LeaveDto.builder()
                    .startDate(LocalDate.of(2023, 7, 5))
                    .endDate(LocalDate.of(2023, 7, 8))
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}/leaves")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID)
                    .toUriString();

            mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(leaveToAdd)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").isNotEmpty());
        }

        @Test
        @SneakyThrows
        void addLeaveToEmployeeShouldReturnNotFoundStatusWhenEmployeeIsNotPresent() {
            LeaveDto leaveToAdd = LeaveDto.builder()
                    .startDate(LocalDate.of(2023, 7, 5))
                    .endDate(LocalDate.of(2023, 7, 8))
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}/leaves")
                    .buildAndExpand(INCORRECT_EMPLOYEE_ID)
                    .toUriString();

            mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(leaveToAdd)))
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
        void updateLeaveByIdShouldReturnNoContentStatus() {
            LeaveDto leaveToAdd = LeaveDto.builder()
                    .startDate(LocalDate.of(2023, 7, 5))
                    .endDate(LocalDate.of(2023, 7, 8))
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}/leaves/{leaveId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, CORRECT_LEAVE_ID)
                    .toUriString();

            mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(leaveToAdd)))
                    .andExpect(status().isNoContent());
        }

        @Test
        @SneakyThrows
        void updateLeaveByIdShouldReturnNotFoundStatusWhenEmployeeIsNotPresent() {
            LeaveDto leaveToAdd = LeaveDto.builder()
                    .startDate(LocalDate.of(2023, 7, 5))
                    .endDate(LocalDate.of(2023, 7, 8))
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}/leaves/{leaveId}")
                    .buildAndExpand(INCORRECT_EMPLOYEE_ID, CORRECT_LEAVE_ID)
                    .toUriString();

            mockMvc.perform(put(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(leaveToAdd)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").isNotEmpty())
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

        @Test
        @SneakyThrows
        void updateLeaveByIdShouldReturnNotFoundStatusWhenLeaveIsNotPresent() {
            LeaveDto leaveToAdd = LeaveDto.builder()
                    .startDate(LocalDate.of(2023, 7, 5))
                    .endDate(LocalDate.of(2023, 7, 8))
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}/leaves/{leaveId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, INCORRECT_EMPLOYEE_ID)
                    .toUriString();

            mockMvc.perform(put(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(leaveToAdd)))
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
        void deleteLeaveByEmployeeAndLeaveIdsShouldReturnNoContentStatus() {
            String url = fromPath("/api/v0/employees/{employeeId}/leaves/{leaveId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, CORRECT_LEAVE_ID)
                    .toUriString();

            mockMvc.perform(delete(url))
                    .andExpect(status().isNoContent());
        }

        @Test
        @SneakyThrows
        void deleteLeaveByEmployeeAndLeaveIdsShouldReturnNotFoundStatusWhenEmployeeIsNotPresent() {
            String url = fromPath("/api/v0/employees/{employeeId}/leaves/{leaveId}")
                    .buildAndExpand(INCORRECT_EMPLOYEE_ID, CORRECT_LEAVE_ID)
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
        void deleteLeaveByEmployeeAndLeaveIdsShouldReturnNotFoundStatusWhenLeaveIsNotPresent() {
            String url = fromPath("/api/v0/employees/{employeeId}/leaves/{leaveId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, INCORRECT_LEAVE_ID)
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