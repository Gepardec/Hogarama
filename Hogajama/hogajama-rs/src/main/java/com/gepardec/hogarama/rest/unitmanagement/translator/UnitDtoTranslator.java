package com.gepardec.hogarama.rest.unitmanagement.translator;

import com.gepardec.hogarama.domain.unitmanagement.dao.OwnerDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;
import com.gepardec.hogarama.rest.unitmanagement.dto.UnitDto;
import org.apache.commons.lang3.NotImplementedException;

import javax.inject.Inject;

public class UnitDtoTranslator implements Translator<UnitDto, Unit> {

    @Inject
    private OwnerDAO ownerDAO;

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
        throw new NotImplementedException("Not necessary yet.");
    }
}
