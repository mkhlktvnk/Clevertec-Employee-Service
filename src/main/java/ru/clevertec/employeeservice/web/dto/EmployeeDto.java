package ru.clevertec.employeeservice.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.clevertec.employeeservice.entity.Gender;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {

    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    @JsonProperty(value = "name")
    private String name;

    @NotBlank
    @JsonProperty(value = "surname")
    private String surname;

    @NotBlank
    @JsonProperty(value = "patronymic")
    private String patronymic;

    @Email
    @JsonProperty(value = "email")
    private String email;

    @Pattern(regexp = "^(\\+375|80)(29|25|44|33)(\\d{3})(\\d{2})(\\d{2})$")
    @JsonProperty(value = "phoneNumber")
    private String phoneNumber;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonProperty(value = "dateOfBirth")
    private LocalDate dateOfBirth;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonProperty(value = "dateOfEmployment")
    private LocalDate dateOfEmployment;

    @NotNull
    @JsonProperty(value = "gender")
    private Gender gender;
}
