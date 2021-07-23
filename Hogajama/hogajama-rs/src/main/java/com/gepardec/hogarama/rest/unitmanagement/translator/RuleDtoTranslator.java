package com.gepardec.hogarama.rest.unitmanagement.translator;

import com.gepardec.hogarama.domain.unitmanagement.context.UserContext;
import com.gepardec.hogarama.domain.unitmanagement.dao.ActorDAO;
import com.gepardec.hogarama.domain.unitmanagement.dao.SensorDAO;
import com.gepardec.hogarama.domain.unitmanagement.dao.UnitDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.EntityNotFoundException;
import com.gepardec.hogarama.domain.unitmanagement.entity.LowWaterWateringRule;
import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;
import com.gepardec.hogarama.rest.unitmanagement.dto.RuleDto;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class RuleDtoTranslator implements Translator<RuleDto, LowWaterWateringRule> {

    private static final Logger LOG = LoggerFactory.getLogger(RuleDtoTranslator.class);

    @Inject
    private SensorDAO sensorDAO;
    
    @Inject
    private ActorDAO actorDAO;
    
    @Inject
    private UnitDAO unitDAO;
    
    @Inject
    private UserContext userContext;

    @Override
    public RuleDto toDto(LowWaterWateringRule rule) {
        Preconditions.checkNotNull(rule, "Rule must not be null");

        return RuleDto.of(rule.getId(),
                rule.getName(),
                rule.getSensor().getId(),
                rule.getActor().getId(),
                rule.getUnit().getId(),
                rule.getWaterDuration(),
                rule.getLowWater()
                );
    }

    @Override
    public LowWaterWateringRule fromDto(RuleDto dto) {
        Preconditions.checkNotNull(dto, "RuleDto must not be null");

        LowWaterWateringRule rule = new LowWaterWateringRule();
        rule.setId(dto.getId());
        Sensor sensor = sensorDAO.getByIdNonOpt(dto.getSensorId());
        rule.setSensor(sensor);
        rule.setActor(actorDAO.getByIdNonOpt(dto.getActorId()));
        rule.setUnit(unitDAO.getById(dto.getUnitId()).orElse(sensor.getUnit()));
        rule.setName(dto.getName());
        rule.setLowWater(dto.getLowWater());
        rule.setWaterDuration(dto.getWaterDuration());

        return rule;
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
