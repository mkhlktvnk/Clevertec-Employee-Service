package ru.clevertec.employeeservice.provider;

import lombok.experimental.UtilityClass;
import ru.clevertec.employeeservice.builder.impl.EmployeeTestBuilder;
import ru.clevertec.employeeservice.entity.Employee;
import ru.clevertec.employeeservice.entity.Gender;

import java.sql.Date;

@UtilityClass
public class EmployeeTestDataProvider {

    private static final String UNIQUE_EMAIL = "john.jef@example.com";

    private static final String NOT_UNIQUE_EMAIL = "john.doe@example.com";

    private static final String UNIQUE_PHONE_NUMBER = "+375257534523";

    private static final String NOT_UNIQUE_PHONE_NUMBER = "+375291234567";

    public static Employee getValidEmployee() {
        return EmployeeTestBuilder.anEmployee()
                .withName("John")
                .withSurname("Jef")
                .withPatronymic("Smith")
                .withEmail(UNIQUE_EMAIL)
                .withPhoneNumber(UNIQUE_PHONE_NUMBER)
                .withDateOfBirth(Date.valueOf("1990-1-1"))
                .withDateOfEmployment(Date.valueOf("2020-10-1"))
                .withGender(Gender.MALE)
                .build();
    }

    public static Employee getEmployeeWithNotUniqueEmail() {
        return EmployeeTestBuilder.anEmployee()
                .withName("John")
                .withSurname("Jef")
                .withPatronymic("Smith")
                .withEmail(NOT_UNIQUE_EMAIL)
                .withPhoneNumber(UNIQUE_PHONE_NUMBER)
                .withDateOfBirth(Date.valueOf("1990-1-1"))
                .withDateOfEmployment(Date.valueOf("2020-10-1"))
                .withGender(Gender.MALE)
                .build();
    }

    public static Employee getEmployeeWithNotUniquePhoneNumber() {
        return EmployeeTestBuilder.anEmployee()
                .withName("John")
                .withSurname("Jef")
                .withPatronymic("Smith")
                .withEmail(UNIQUE_PHONE_NUMBER)
                .withPhoneNumber(NOT_UNIQUE_PHONE_NUMBER)
                .withDateOfBirth(Date.valueOf("1990-1-1"))
                .withDateOfEmployment(Date.valueOf("2020-10-1"))
                .withGender(Gender.MALE)
                .build();
    }
}
