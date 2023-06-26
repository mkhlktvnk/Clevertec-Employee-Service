package ru.clevertec.employeeservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import ru.clevertec.employeeservice.entity.Salary;
import ru.clevertec.employeeservice.web.dto.SalaryDto;

import java.util.List;

@Mapper
public interface SalaryMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "employee", ignore = true)
    })
    void copyAllFields(@MappingTarget Salary target, Salary source);

    SalaryDto toDto(Salary salary);

    List<SalaryDto> toDto(List<Salary> salaries);

    Salary toEntity(SalaryDto salaryDto);
}
