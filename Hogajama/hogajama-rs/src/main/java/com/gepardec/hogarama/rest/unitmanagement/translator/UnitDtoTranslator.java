package com.gepardec.hogarama.rest.unitmanagement.translator;

import com.gepardec.hogarama.domain.unitmanagement.context.UserContext;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;
import com.gepardec.hogarama.rest.unitmanagement.dto.UnitDto;

import javax.inject.Inject;

public class UnitDtoTranslator implements Translator<UnitDto, Unit> {

    @Inject
    private UserContext userContext;

    @Override
    public UnitDto toDto(Unit unit) {
        return UnitDto.of(unit.getId(),
                unit.getDescription(),
                unit.isDefaultUnit(),
                unit.getName(),
                unit.getOwner().getId());
    }

    @Override
    public Unit fromDto(UnitDto dto) {
        Unit unit = new Unit();
        unit.setDescription(dto.getDescription());
        unit.setName(dto.getName());
        unit.setId(dto.getId());
        unit.setOwner(userContext.getOwner()); // We always use the current Owner? Should OwnerId be in Dto?
        unit.setDefaultUnit(dto.isDefaultUnit());
        return unit;
     }
}
