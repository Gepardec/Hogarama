package com.gepardec.hogarama.rest.unitmanagement.translator;

import com.gepardec.hogarama.domain.unitmanagement.dao.UnitDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.Actor;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;
import com.gepardec.hogarama.rest.unitmanagement.dto.ActorDto;

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
public class ActorDtoTranslatorTest {

    private static final long ACTOR_ID = 1337L;
    private static final String DEVICE_ID = "DEVICE_ID";
    private static final String NAME = "MY_ACTOR";
    private static final long UNIT_ID = 2L;
    public static final String QNAME = "QNAME";


    @Mock
    private UnitDAO unitDAO;
    @InjectMocks
    private ActorDtoTranslator translator;

    @Test
    public void toDto_NullInput_ExceptionExpected() {
        assertThrows(NullPointerException.class, () -> translator.toDto(null));
    }

    @Test
    public void toDto() {
        Actor actor = new Actor();
        actor.setId(ACTOR_ID);
        actor.setDeviceId(DEVICE_ID);
        actor.setName(NAME);
        actor.setUnit(newUnit());
        actor.setQueueName(QNAME);
        ActorDto result = translator.toDto(actor);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(ACTOR_ID);
        assertThat(result.getDeviceId()).isEqualTo(DEVICE_ID);
        assertThat(result.getName()).isEqualTo(NAME);
        assertThat(result.getUnitId()).isEqualTo(UNIT_ID);
        assertThat(result.getQueueName()).isEqualTo(QNAME);
    }

    @Test
    public void fromDto_NullInput_ExceptionExpected() {
        assertThrows(NullPointerException.class, () -> translator.fromDto(null));
    }

    @Test
    public void fromDto() {
        Unit unit = newUnit();
        Mockito.when(unitDAO.getById(UNIT_ID)).thenReturn(Optional.of(unit));

        ActorDto dto = new ActorDto();
        dto.setId(ACTOR_ID);
        dto.setDeviceId(DEVICE_ID);
        dto.setName(NAME);
        dto.setUnitId(UNIT_ID);
        dto.setQueueName(QNAME);

        Actor result = translator.fromDto(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(ACTOR_ID);
        assertThat(result.getDeviceId()).isEqualTo(DEVICE_ID);
        assertThat(result.getName()).isEqualTo(NAME);
        assertThat(result.getUnit()).isEqualTo(unit);
        assertThat(result.getQueueName()).isEqualTo(QNAME);
    }

    private Unit newUnit() {
        Unit unit = new Unit();
        unit.setId(UNIT_ID);
        return unit;
    }
}
