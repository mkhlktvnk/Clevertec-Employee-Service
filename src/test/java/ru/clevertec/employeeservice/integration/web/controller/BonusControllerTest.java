package ru.clevertec.employeeservice.integration.web.controller;

import com.c4_soft.springaddons.security.oauth2.test.annotations.WithMockJwtAuth;
import com.c4_soft.springaddons.security.oauth2.test.annotations.keycloak.WithMockKeycloakAuth;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.clevertec.employeeservice.integration.BaseIntegrationTest;
import ru.clevertec.employeeservice.web.dto.BonusDto;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

@AutoConfigureMockMvc
@WithMockKeycloakAuth(authorities = "ROLE_ADMIN")
class BonusControllerTest extends BaseIntegrationTest {

    private static final Long CORRECT_EMPLOYEE_ID = 1L;

    private static final Long INCORRECT_EMPLOYEE_ID = 1000L;

    private static final Long CORRECT_BONUS_ID = 2L;

    private static final Long INCORRECT_BONUS_ID = 1001L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class ReadMethodsTest {

        @Test
        @SneakyThrows
        void findAllByEmployeeIdAndPageableShouldReturnExpectedCountOfBonuses() {
            int expectedLength = 3;
            String url = fromPath("/api/v0/employees/{employeeId}/bonuses")
                    .queryParam("page", 0)
                    .queryParam("size", 3)
                    .buildAndExpand(CORRECT_EMPLOYEE_ID)
                    .toUriString();

            mockMvc.perform(MockMvcRequestBuilders.get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.length()").value(expectedLength));
        }

        @Test
        @SneakyThrows
        void findAllByEmployeeIdAndPageableShouldThrowResourceNotFoundExceptionWhenEmployeeIsNotPresent() {
            String url = fromPath("/api/v0/employees/{employeeId}/bonuses")
                    .queryParam("page", 0)
                    .queryParam("size", 3)
                    .buildAndExpand(INCORRECT_EMPLOYEE_ID)
                    .toUriString();

            mockMvc.perform(MockMvcRequestBuilders.get(url))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").isNotEmpty())
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

        @Test
        @SneakyThrows
        void findByEmployeeAndBonusIdsShouldReturnOkStatusAndExpectedBonus() {
            BonusDto bonusDto = BonusDto.builder()
                    .amount(BigDecimal.valueOf(150.00))
                    .description("Incentive Bonus")
                    .paymentDate(LocalDate.of(2023, 6, 15))
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}/bonuses/{bonusId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, CORRECT_BONUS_ID)
                    .toUriString();

            mockMvc.perform(MockMvcRequestBuilders.get(url))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(CORRECT_BONUS_ID))
                    .andExpect(jsonPath("$.amount").value(bonusDto.getAmount()))
                    .andExpect(jsonPath("$.description").value(bonusDto.getDescription()))
                    .andExpect(jsonPath("$.paymentDate").value(bonusDto.getPaymentDate().toString()));
        }

        @Test
        @SneakyThrows
        void findByEmployeeAndBonusIdsShouldReturnNotFoundStatusWhenEmployeeIsNotPresent() {
            String url = fromPath("/api/v0/employees/{employeeId}/bonuses/{bonusId}")
                    .buildAndExpand(INCORRECT_EMPLOYEE_ID, CORRECT_BONUS_ID)
                    .toUriString();

            mockMvc.perform(MockMvcRequestBuilders.get(url))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").isNotEmpty())
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

        @Test
        @SneakyThrows
        void findByEmployeeAndBonusIdsShouldReturnNotFoundStatusWhenBonusIsNotPresent() {
            String url = fromPath("/api/v0/employees/{employeeId}/bonuses/{bonusId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, INCORRECT_BONUS_ID)
                    .toUriString();

            mockMvc.perform(MockMvcRequestBuilders.get(url))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").isNotEmpty())
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }
    }

    @Nested
    class CreateMethodsTest {

        @Test
        @SneakyThrows
        void addBonusToEmployeeShouldReturnCreatedBonusWithNotNullId() {
            BonusDto bonusDto = BonusDto.builder()
                    .amount(BigDecimal.valueOf(150.00))
                    .description("Per month bonus")
                    .paymentDate(LocalDate.of(2023, 6, 15))
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}/bonuses")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID)
                    .toUriString();

            mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bonusDto)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").isNotEmpty());
        }

        @Test
        @SneakyThrows
        void addBonusToEmployeeShouldReturnNotFoundStatusWhenEmployeeIsNotPresent() {
            BonusDto bonusDto = BonusDto.builder()
                    .amount(BigDecimal.valueOf(150.00))
                    .description("Per month bonus")
                    .paymentDate(LocalDate.of(2023, 6, 15))
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}/bonuses")
                    .buildAndExpand(INCORRECT_EMPLOYEE_ID)
                    .toUriString();

            mockMvc.perform(MockMvcRequestBuilders.post(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(bonusDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").isNotEmpty())
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());

        }

    }

    @Nested
    class UpdateMethodsTest {

        @Test
        @SneakyThrows
        void updateBonusByEmployeeAndBonusIdsShouldReturnNotContentStatus() {
            BonusDto bonusDto = BonusDto.builder()
                    .amount(BigDecimal.valueOf(150.00))
                    .description("Per month bonus")
                    .paymentDate(LocalDate.of(2023, 6, 15))
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}/bonuses/{bonusId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, CORRECT_BONUS_ID)
                    .toUriString();

            mockMvc.perform(MockMvcRequestBuilders.put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bonusDto)))
                    .andExpect(status().isNoContent());
        }

        @Test
        @SneakyThrows
        void updateBonusByEmployeeAndBonusIdsShouldReturnNotFoundStatusWhenEmployeeIsNotPresent() {
            BonusDto bonusDto = BonusDto.builder()
                    .amount(BigDecimal.valueOf(150.00))
                    .description("Per month bonus")
                    .paymentDate(LocalDate.of(2023, 6, 15))
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}/bonuses/{bonusId}")
                    .buildAndExpand(INCORRECT_EMPLOYEE_ID, CORRECT_BONUS_ID)
                    .toUriString();

            mockMvc.perform(MockMvcRequestBuilders.put(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(bonusDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").isNotEmpty())
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

        @Test
        @SneakyThrows
        void updateBonusByEmployeeAndBonusIdsShouldReturnNotFoundStatusWhenBonusIsNotPresent() {
            BonusDto bonusDto = BonusDto.builder()
                    .amount(BigDecimal.valueOf(150.00))
                    .description("Per month bonus")
                    .paymentDate(LocalDate.of(2023, 6, 15))
                    .build();
            String url = fromPath("/api/v0/employees/{employeeId}/bonuses/{bonusId}")
                    .buildAndExpand(INCORRECT_EMPLOYEE_ID, CORRECT_BONUS_ID)
                    .toUriString();

            mockMvc.perform(MockMvcRequestBuilders.put(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(bonusDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").isNotEmpty())
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

    }

    @Nested
    class DeleteMethodsTest {

        @Test
        @SneakyThrows
        void deleteBonusByEmployeeAndBonusIdsShouldReturnNoContentStatus() {
            String url = fromPath("/api/v0/employees/{employeeId}/bonuses/{bonusId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, CORRECT_BONUS_ID)
                    .toUriString();

            mockMvc.perform(MockMvcRequestBuilders.delete(url))
                    .andExpect(status().isNoContent());
        }

        @Test
        @SneakyThrows
        void deleteBonusByEmployeeAndBonusIdsShouldReturnNotFoundStatusWhenEmployeeIsNotPresent() {
            String url = fromPath("/api/v0/employees/{employeeId}/bonuses/{bonusId}")
                    .buildAndExpand(INCORRECT_EMPLOYEE_ID, CORRECT_BONUS_ID)
                    .toUriString();

            mockMvc.perform(MockMvcRequestBuilders.delete(url))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").isNotEmpty())
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

        @Test
        @SneakyThrows
        void deleteBonusByEmployeeAndBonusIdsShouldReturnNotFoundStatusWhenBonusIsNotPresent() {
            String url = fromPath("/api/v0/employees/{employeeId}/bonuses/{bonusId}")
                    .buildAndExpand(CORRECT_EMPLOYEE_ID, INCORRECT_BONUS_ID)
                    .toUriString();

            mockMvc.perform(MockMvcRequestBuilders.delete(url))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").isNotEmpty())
                    .andExpect(jsonPath("$.errorUrl").isNotEmpty())
                    .andExpect(jsonPath("$.errorTime").isNotEmpty());
        }

    }

}
