package ru.clevertec.employeeservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import ru.clevertec.employeeservice.entity.Payroll;
import ru.clevertec.employeeservice.web.dto.PayrollDto;

import java.util.List;

@Mapper
public interface PayrollMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "salary", ignore = true)
    })
    void copyNotNullFields(@MappingTarget Payroll target, Payroll source);

    PayrollDto toDto(Payroll payroll);

    List<PayrollDto> toDto(List<Payroll> payrolls);

    Payroll toEntity(PayrollDto payrollDto);
}
