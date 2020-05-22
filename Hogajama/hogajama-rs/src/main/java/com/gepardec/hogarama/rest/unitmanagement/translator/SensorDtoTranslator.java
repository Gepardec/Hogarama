package com.gepardec.hogarama.rest.unitmanagement.translator;

import com.gepardec.hogarama.domain.exception.TechnicalException;
import com.gepardec.hogarama.domain.unitmanagement.cache.SensorTypeCache;
import com.gepardec.hogarama.domain.unitmanagement.dao.UnitDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import com.gepardec.hogarama.domain.unitmanagement.entity.SensorType;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;
import com.gepardec.hogarama.domain.unitmanagement.context.UnitManagementContext;
import com.gepardec.hogarama.rest.unitmanagement.dto.SensorDto;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.Optional;

@Dependent
public class SensorDtoTranslator implements Translator<SensorDto, Sensor> {

    private static final Logger LOG = LoggerFactory.getLogger(SensorDtoTranslator.class);

    @Inject
    private UnitDAO unitDAO;
    @Inject
    private UnitManagementContext unitManagementContext;
    @Inject
    private SensorTypeCache sensorTypeCache;

    @Override
    public SensorDto toDto(Sensor sensor) {
        Preconditions.checkNotNull(sensor, "Sensor must not be null");

        return SensorDto.of(sensor.getId(),
                sensor.getName(),
                sensor.getDeviceId(),
                sensor.getUnit().getId(),
                sensor.getSensorType().getId());
    }

    @Override
    public Sensor fromDto(SensorDto dto) {
        Preconditions.checkNotNull(dto, "SensorDto must not be null");

        Sensor sensor = new Sensor();
        sensor.setId(dto.getId());
        Unit unit = getUnitByUnitIdOrDefaultUnit(dto.getUnitId());
        sensor.setUnit(unit);
        sensor.setDeviceId(dto.getDeviceId());
        sensor.setName(dto.getName());

        Optional<SensorType> optionalSensorType = sensorTypeCache.byId(dto.getSensorTypeId());
        if (optionalSensorType.isPresent()) {
            sensor.setSensorType(optionalSensorType.get());
        } else {
            throw new TechnicalException("No sensorType with id " + dto.getSensorTypeId() + " found.");
        }
        return sensor;
    }

    private Unit getUnitByUnitIdOrDefaultUnit(Long unitId) {
        if (unitId == null) {
            LOG.debug("No unitId supplied - Use default unit.");
            return unitManagementContext.getOwner().getDefaultUnit();
        }

        return unitDAO.getById(unitId).orElseGet(() -> {
            LOG.warn("No unit with id {} found.", unitId);
            return unitManagementContext.getOwner().getDefaultUnit();
        });
    }
}
