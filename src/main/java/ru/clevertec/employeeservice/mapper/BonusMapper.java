package ru.clevertec.employeeservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import ru.clevertec.employeeservice.entity.Bonus;
import ru.clevertec.employeeservice.web.dto.BonusDto;

import java.util.List;

@Mapper
public interface BonusMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "employee", ignore = true)
    })
    void copyAllFields(@MappingTarget Bonus target, Bonus source);

    BonusDto toDto(Bonus bonus);

    List<BonusDto> toDto(List<Bonus> bonuses);

    Bonus toEntity(BonusDto bonusDto);
}
