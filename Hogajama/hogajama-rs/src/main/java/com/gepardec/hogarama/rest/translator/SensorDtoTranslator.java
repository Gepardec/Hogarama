package com.gepardec.hogarama.rest.translator;

import com.gepardec.hogarama.domain.cache.SensorTypeCache;
import com.gepardec.hogarama.domain.entity.Sensor;
import com.gepardec.hogarama.domain.entity.SensorType;
import com.gepardec.hogarama.domain.entity.Unit;
import com.gepardec.hogarama.domain.exception.TechnicalException;
import com.gepardec.hogarama.domain.unit.UnitDao;
import com.gepardec.hogarama.rest.v2.dto.SensorDto;
import com.gepardec.hogarama.service.OwnerStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.Optional;

@Dependent
public class SensorDtoTranslator implements Translator<SensorDto, Sensor> {

    private static final Logger LOG = LoggerFactory.getLogger(SensorDtoTranslator.class);

    @Inject
    private UnitDao unitDao;
    @Inject
    private OwnerStore ownerStore;
    @Inject
    private SensorTypeCache sensorTypeCache;

    @Override
    public SensorDto toDto(Sensor sensor) {
        return SensorDto.of(sensor.getId(),
                sensor.getName(),
                sensor.getDeviceId(),
                sensor.getUnit().getId(),
                sensor.getSensorType().getId());
    }

    @Override
    public Sensor fromDto(SensorDto dto) {
        Sensor sensor = new Sensor();
        if (dto.getUnitId() != null) {
            Unit unit = unitDao.getById(dto.getUnitId()).orElseGet(() -> {
                LOG.warn("No unit with id {} found.", dto.getUnitId());
                return ownerStore.getOwner().getDefaultUnit();
            });
            sensor.setUnit(unit);
        }
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
}
