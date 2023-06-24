package ru.clevertec.employeeservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import ru.clevertec.employeeservice.entity.Employee;

@Mapper
public interface EmployeeMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "leaves", ignore = true),
            @Mapping(target = "salaries", ignore = true),
            @Mapping(target = "bonuses", ignore = true)
    })
    void copyAllFields(@MappingTarget Employee target, Employee source);
}
