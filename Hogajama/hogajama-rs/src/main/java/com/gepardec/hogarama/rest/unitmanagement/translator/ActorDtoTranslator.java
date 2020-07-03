package com.gepardec.hogarama.rest.unitmanagement.translator;

import com.gepardec.hogarama.domain.unitmanagement.context.UserContext;
import com.gepardec.hogarama.domain.unitmanagement.dao.UnitDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.Actor;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;
import com.gepardec.hogarama.rest.unitmanagement.dto.ActorDto;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class ActorDtoTranslator implements Translator<ActorDto, Actor> {

    private static final Logger LOG = LoggerFactory.getLogger(ActorDtoTranslator.class);

    @Inject
    private UnitDAO unitDAO;
    @Inject
    private UserContext userContext;

    @Override
    public ActorDto toDto(Actor actor) {
        Preconditions.checkNotNull(actor, "Actor must not be null");

        return ActorDto.of(actor.getId(),
                actor.getName(),
                actor.getDeviceId(),
                actor.getUnit().getId(),
                actor.getQueueName());
    }

    @Override
    public Actor fromDto(ActorDto dto) {
        Preconditions.checkNotNull(dto, "ActorDto must not be null");

        Actor actor = new Actor();
        actor.setId(dto.getId());
        Unit unit = getUnitByUnitIdOrDefaultUnit(dto.getUnitId());
        actor.setUnit(unit);
        actor.setDeviceId(dto.getDeviceId());
        actor.setName(dto.getName());
        actor.setQueueName(dto.getQueueName());

        return actor;
    }

    private Unit getUnitByUnitIdOrDefaultUnit(Long unitId) {
        if (unitId == null) {
            LOG.debug("No unitId supplied - Use default unit.");
            return userContext.getOwner().getDefaultUnit();
        }

        return unitDAO.getById(unitId).orElseGet(() -> {
            LOG.warn("No unit with id {} found.", unitId);
            return userContext.getOwner().getDefaultUnit();
        });
    }
}
