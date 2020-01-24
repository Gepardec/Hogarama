package com.gepardec.hogarama.rest.translator;

import com.gepardec.hogarama.domain.entity.Owner;
import com.gepardec.hogarama.domain.entity.Unit;
import com.gepardec.hogarama.domain.owner.OwnerDao;
import com.gepardec.hogarama.domain.unit.UnitDao;
import com.gepardec.hogarama.rest.v2.dto.UnitDto;
import com.gepardec.hogarama.service.OwnerStore;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class UnitDtoTranslator implements Translator<UnitDto, Unit> {

    @Inject
    private UnitDao unitDao;

    @Inject
    private OwnerDao ownerDao;

    @Inject
    private OwnerStore ownerStore;

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
        unit.setId(dto.getId());
        if (dto.getOwnerId() != null) {
            Owner ow = ownerDao.getById(dto.getOwnerId()).orElse(null);
            unit.setOwner(ow);
        }
        unit.setDescription(dto.getDescription());
        unit.setName(dto.getName());
        unit.setDefaultUnit(dto.getDefault());

        return unit;
    }
}
