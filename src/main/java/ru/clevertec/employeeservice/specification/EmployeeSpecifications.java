package ru.clevertec.employeeservice.specification;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.employeeservice.criteria.EmployeeCriteria;
import ru.clevertec.employeeservice.entity.Employee;
import ru.clevertec.employeeservice.entity.Gender;

import java.util.Date;

@UtilityClass
public class EmployeeSpecifications {

    public static Specification<Employee> hasMatchWithCriteria(EmployeeCriteria employeeCriteria) {
        return Specification.allOf(
                hasNameLike(employeeCriteria.getName()),
                hasSurnameLike(employeeCriteria.getSurname()),
                hasPatronymicLike(employeeCriteria.getPatronymic()),
                hasPhoneNumber(employeeCriteria.getPhoneNumber()),
                hasEmail(employeeCriteria.getEmail()),
                hasDateOfBirth(employeeCriteria.getDateOfBirth()),
                hasDateOfEmployment(employeeCriteria.getDateOfEmployment()),
                hasPositionName(employeeCriteria.getPositionName())
        );
    }

    public static Specification<Employee> hasNameLike(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(root.get("name"), "%" + name + "%");
        };
    }

    public static Specification<Employee> hasSurnameLike(String surname) {
        return (root, query, criteriaBuilder) -> {
            if (surname == null || surname.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(root.get("surname"), "%" + surname + "%");
        };
    }

    public static Specification<Employee> hasPatronymicLike(String patronymic) {
        return (root, query, criteriaBuilder) -> {
            if (patronymic == null || patronymic.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(root.get("surname"), "%" + patronymic + "%");
        };
    }

    public static Specification<Employee> hasPhoneNumber(String phoneNumber) {
        return (root, query, criteriaBuilder) -> {
            if (phoneNumber == null || phoneNumber.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.equal(root.get("phoneNumber"), phoneNumber);
        };
    }

    public static Specification<Employee> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.equal(root.get("email"), email);
        };
    }

    public static Specification<Employee> hasDateOfBirth(Date dateOfBirth) {
        return (root, query, criteriaBuilder) -> {
            if (dateOfBirth == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.equal(root.get("dateOfBirth"), dateOfBirth);
        };
    }

    public static Specification<Employee> hasDateOfEmployment(Date dateOfEmployment) {
        return (root, query, criteriaBuilder) -> {
            if (dateOfEmployment == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.equal(root.get("dateOfBirth"), dateOfEmployment);
        };
    }

    public static Specification<Employee> hasGender(Gender gender) {
        return (root, query, criteriaBuilder) -> {
            if (gender == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.equal(root.get("gender"), gender);
        };
    }

    public static Specification<Employee> hasPositionName(String positionName) {
        return (root, query, criteriaBuilder) -> {
            if (positionName == null || positionName.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.equal(root.join("positions").get("name"), positionName);
        };
    }
}
