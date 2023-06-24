package ru.clevertec.employeeservice.builder.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.employeeservice.builder.TestBuilder;
import ru.clevertec.employeeservice.criteria.EmployeeCriteria;
import ru.clevertec.employeeservice.entity.Gender;

import java.util.Date;


@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "anEmployeeCriteria")
public class EmployeeCriteriaTestBuilder implements TestBuilder<EmployeeCriteria> {


    private String name = "";

    private String surname = "";

    private String patronymic = "";

    private String email = "";

    private String phoneNumber = "";

    private Date dateOfBirth;

    private Date dateOfEmployment;

    private Gender gender;

    private String positionName = "";

    @Override
    public EmployeeCriteria build() {
        EmployeeCriteria criteria = new EmployeeCriteria();

        criteria.setName(name);
        criteria.setSurname(surname);
        criteria.setPatronymic(patronymic);
        criteria.setEmail(email);
        criteria.setPhoneNumber(phoneNumber);
        criteria.setDateOfBirth(dateOfBirth);
        criteria.setDateOfEmployment(dateOfEmployment);
        criteria.setGender(gender);
        criteria.setPositionName(positionName);

        return criteria;
    }
}
