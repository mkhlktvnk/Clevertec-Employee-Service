package ru.clevertec.employeeservice.builder.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.employeeservice.builder.TestBuilder;
import ru.clevertec.employeeservice.entity.Bonus;
import ru.clevertec.employeeservice.entity.Employee;
import ru.clevertec.employeeservice.entity.Gender;
import ru.clevertec.employeeservice.entity.Salary;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "anEmployee")
public class EmployeeTestBuilder implements TestBuilder<Employee> {

    private Long id = 0L;

    private String name = "";

    private String surname = "";

    private String patronymic = "";

    private String email = "";

    private String phoneNumber = "";

    private Date dateOfBirth = Date.valueOf(LocalDate.now());

    private Date dateOfEmployment = Date.valueOf(LocalDate.now());

    private Gender gender = Gender.MALE;

    private Set<Salary> salaries = new HashSet<>();

    private Set<Bonus> bonuses = new HashSet<>();

    @Override
    public Employee build() {
        Employee employee = new Employee();

        employee.setId(id);
        employee.setName(name);
        employee.setSurname(surname);
        employee.setPatronymic(patronymic);
        employee.setEmail(email);
        employee.setPhoneNumber(phoneNumber);
        employee.setDateOfBirth(dateOfBirth);
        employee.setDateOfEmployment(dateOfEmployment);
        employee.setGender(gender);
        employee.setSalaries(salaries);
        employee.setBonuses(bonuses);

        return employee;
    }
}
