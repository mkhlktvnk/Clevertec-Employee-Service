package ru.clevertec.employeeservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import ru.clevertec.employeeservice.entity.Leave;

@Mapper
public interface LeaveMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "employee", ignore = true)
    })
    void copyAllFields(@MappingTarget Leave target, Leave source);
}
