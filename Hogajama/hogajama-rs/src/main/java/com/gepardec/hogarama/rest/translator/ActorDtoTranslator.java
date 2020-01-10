package com.gepardec.hogarama.rest.translator;

import com.gepardec.hogarama.domain.entity.Actor;
import com.gepardec.hogarama.domain.entity.Unit;
import com.gepardec.hogarama.domain.unit.UnitDao;
import com.gepardec.hogarama.rest.v2.dto.ActorDto;
import com.gepardec.hogarama.service.OwnerStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class ActorDtoTranslator implements Translator<ActorDto, Actor> {

    private static final Logger LOG = LoggerFactory.getLogger(ActorDtoTranslator.class);

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
            Unit unit = unitDao.getById(dto.getUnitId()).orElseGet(() -> {
                LOG.warn("No unit with id {} found.", dto.getUnitId());
                return ownerStore.getOwner().getDefaultUnit();
            });
            actor.setUnit(unit);
        }
        actor.setDeviceId(dto.getDeviceId());
        actor.setName(dto.getName());
        return null;
    }
}
