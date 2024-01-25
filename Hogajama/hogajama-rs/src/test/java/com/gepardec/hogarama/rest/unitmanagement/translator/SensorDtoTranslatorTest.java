package com.gepardec.hogarama.rest.unitmanagement.translator;

import com.gepardec.hogarama.domain.unitmanagement.cache.SensorTypeCache;
import com.gepardec.hogarama.domain.unitmanagement.dao.UnitDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import com.gepardec.hogarama.domain.unitmanagement.entity.SensorType;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;
import com.gepardec.hogarama.rest.unitmanagement.dto.SensorDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
public class SensorDtoTranslatorTest {

    private static final long SENSOR_ID = 1337L;
    private static final String DEVICE_ID = "DEVICE_ID";
    private static final String NAME = "MY_SENSOR";
    private static final long SENSOR_TYPE_ID = 1L;
    private static final long UNIT_ID = 2L;

    @Mock
    private UnitDAO unitDAO;
    @Mock
    private SensorTypeCache sensorTypeCache;
    @InjectMocks
    private SensorDtoTranslator translator;

    @Test
    public void toDto_NullInput_ExceptionExpected() {
        assertThrows(NullPointerException.class, () -> {
            translator.toDto(null);
        });
    }

    @Test
    public void toDto() {
        Sensor sensor = new Sensor();
        sensor.setId(SENSOR_ID);
        sensor.setDeviceId(DEVICE_ID);
        sensor.setName(NAME);
        sensor.setSensorType(newSensorType());
        sensor.setUnit(newUnit());

        SensorDto result = translator.toDto(sensor);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(SENSOR_ID);
        assertThat(result.getDeviceId()).isEqualTo(DEVICE_ID);
        assertThat(result.getName()).isEqualTo(NAME);
        assertThat(result.getSensorTypeId()).isEqualTo(SENSOR_TYPE_ID);
        assertThat(result.getUnitId()).isEqualTo(UNIT_ID);
    }

    @Test
    public void fromDto_NullInput_ExceptionExpected() {
        assertThrows(NullPointerException.class, () -> {
            translator.fromDto(null);
        });
    }

    @Test
    public void fromDto() {
        Unit unit = newUnit();
        Mockito.when(unitDAO.getById(UNIT_ID)).thenReturn(Optional.of(unit));
        SensorType sensorType = newSensorType();
        Mockito.when(sensorTypeCache.byId(SENSOR_TYPE_ID)).thenReturn(Optional.of(sensorType));

        SensorDto dto = new SensorDto();
        dto.setId(SENSOR_ID);
        dto.setDeviceId(DEVICE_ID);
        dto.setName(NAME);
        dto.setSensorTypeId(SENSOR_TYPE_ID);
        dto.setUnitId(UNIT_ID);

        Sensor result = translator.fromDto(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(SENSOR_ID);
        assertThat(result.getDeviceId()).isEqualTo(DEVICE_ID);
        assertThat(result.getName()).isEqualTo(NAME);
        assertThat(result.getSensorType()).isEqualTo(sensorType);
        assertThat(result.getUnit()).isEqualTo(unit);
    }

    private SensorType newSensorType() {
        SensorType sensorType = new SensorType();
        sensorType.setId(SENSOR_TYPE_ID);
        return sensorType;
    }

    private Unit newUnit() {
        Unit unit = new Unit();
        unit.setId(UNIT_ID);
        return unit;
    }
}
