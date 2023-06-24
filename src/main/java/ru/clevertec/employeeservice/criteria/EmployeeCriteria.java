package ru.clevertec.employeeservice.criteria;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.clevertec.employeeservice.entity.Gender;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeCriteria {

    private String name;

    private String surname;

    private String patronymic;

    private String email;

    private String phoneNumber;

    private Date dateOfBirth;

    private Date dateOfEmployment;

    private Gender gender;

    private String positionName;
}
