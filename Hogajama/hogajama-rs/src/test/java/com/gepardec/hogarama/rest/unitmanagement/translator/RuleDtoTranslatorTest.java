package com.gepardec.hogarama.rest.unitmanagement.translator;

import com.gepardec.hogarama.domain.unitmanagement.dao.ActorDAO;
import com.gepardec.hogarama.domain.unitmanagement.dao.SensorDAO;
import com.gepardec.hogarama.domain.unitmanagement.dao.UnitDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.Actor;
import com.gepardec.hogarama.domain.unitmanagement.entity.LowWaterWateringRule;
import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;
import com.gepardec.hogarama.rest.unitmanagement.dto.RuleDto;
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
public class RuleDtoTranslatorTest {

    private static final long RULE_ID = 1338L;
    private static final String NAME = "MY_RULE";
    private static final long SENSOR_ID = 2L;
    private static final long ACTOR_ID = 3L;
    private static final long UNIT_ID = 4L;
    private static final double LOW_WATER = 0.3;
    private static final int WATER_DURATION = 15;


    @Mock
    private SensorDAO sensorDAO;
    
    @Mock
    private ActorDAO actorDAO;
    
    @Mock
    private UnitDAO unitDAO;

    @InjectMocks
    private RuleDtoTranslator translator;

    @Test
    public void toDto_NullInput_ExceptionExpected() {
        assertThrows(NullPointerException.class, () -> translator.toDto(null));
    }

    @Test
    public void toDto() {
        LowWaterWateringRule rule = new LowWaterWateringRule();
        rule.setId(RULE_ID);
        rule.setName(NAME);
        rule.setSensor(newSensor());
        rule.setActor(newActor());
        rule.setUnit(newUnit());
        rule.setLowWater(LOW_WATER);
        rule.setWaterDuration(WATER_DURATION);
        
        RuleDto result = translator.toDto(rule);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(RULE_ID);
        assertThat(result.getName()).isEqualTo(NAME);
        assertThat(result.getUnitId()).isEqualTo(UNIT_ID);
        assertThat(result.getSensorId()).isEqualTo(SENSOR_ID);
        assertThat(result.getActorId()).isEqualTo(ACTOR_ID);
        assertThat(result.getWaterDuration()).isEqualTo(WATER_DURATION);
        assertThat(result.getLowWater()).isEqualTo(LOW_WATER);
    }

    @Test
    public void fromDto_NullInput_ExceptionExpected() {
        assertThrows(NullPointerException.class, () -> translator.fromDto(null));
    }

    @Test
    public void fromDto() {
        Sensor sensor = newSensor();
        Actor actor = newActor();
        Unit unit = newUnit();

        Mockito.when(sensorDAO.getByIdNonOpt(SENSOR_ID)).thenReturn(sensor);
        Mockito.when(actorDAO.getByIdNonOpt(ACTOR_ID)).thenReturn(actor);
        Mockito.when(unitDAO.getById(UNIT_ID)).thenReturn(Optional.of(unit));

        RuleDto dto = new RuleDto();
        dto.setId(RULE_ID);
        dto.setName(NAME);
        dto.setSensorId(SENSOR_ID);
        dto.setActorId(ACTOR_ID);
        dto.setUnitId(UNIT_ID);
        dto.setWaterDuration(WATER_DURATION);
        dto.setLowWater(LOW_WATER);

        LowWaterWateringRule result = translator.fromDto(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(RULE_ID);
        assertThat(result.getName()).isEqualTo(NAME);
        assertThat(result.getSensor()).isEqualTo(sensor);
        assertThat(result.getActor()).isEqualTo(actor);
        assertThat(result.getUnit()).isEqualTo(unit);
        assertThat(result.getLowWater()).isEqualTo(LOW_WATER);
        assertThat(result.getWaterDuration()).isEqualTo(WATER_DURATION);
    }

    private Sensor newSensor() {
        Sensor sensor = new Sensor();
        sensor.setId(SENSOR_ID);
        return sensor;
    }
    
    private Actor newActor() {
        Actor actor = new Actor();
        actor.setId(ACTOR_ID);
        return actor;
    }
    
    private Unit newUnit() {
        Unit unit = new Unit();
        unit.setId(UNIT_ID);
        return unit;
    }
}
