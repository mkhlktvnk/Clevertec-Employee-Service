package ru.clevertec.employeeservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import ru.clevertec.employeeservice.entity.Leave;
import ru.clevertec.employeeservice.web.dto.LeaveDto;

import java.util.List;

@Mapper
public interface LeaveMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "employee", ignore = true)
    })
    void copyAllFields(@MappingTarget Leave target, Leave source);

    LeaveDto toDto(Leave leave);

    List<LeaveDto> toDto(List<Leave> leaves);

    Leave toEntity(LeaveDto leaveDto);
}
