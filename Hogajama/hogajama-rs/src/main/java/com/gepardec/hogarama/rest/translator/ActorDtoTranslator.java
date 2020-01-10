package com.gepardec.hogarama.rest.translator;

import com.gepardec.hogarama.domain.entity.Actor;
import com.gepardec.hogarama.domain.entity.Unit;
import com.gepardec.hogarama.domain.unit.UnitDao;
import com.gepardec.hogarama.rest.v2.dto.ActorDto;
import com.gepardec.hogarama.service.OwnerStore;

import javax.inject.Inject;

public class ActorDtoTranslator implements Translator<ActorDto, Actor> {

    @Inject
    private UnitDao unitDao;
    @Inject
    private OwnerStore ownerStore;

    @Override
    public ActorDto toDto(Actor actor) {
        return ActorDto.of(actor.getId(),
                actor.getName(),
                actor.getDeviceId(),
                actor.getUnit().getId());
    }

    @Override
    public Actor fromDto(ActorDto dto) {
        Actor actor = new Actor();
        if (dto.getUnitId() != null) {
            Unit unit = TranslatorUtils.getUnitByUnitId(dto.getUnitId(), unitDao, ownerStore);
            actor.setUnit(unit);
        }
        actor.setDeviceId(dto.getDeviceId());
        actor.setName(dto.getName());
        return actor;
    }

}
