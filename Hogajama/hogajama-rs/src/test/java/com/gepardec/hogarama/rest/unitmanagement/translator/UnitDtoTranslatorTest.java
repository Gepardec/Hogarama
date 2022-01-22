package com.gepardec.hogarama.rest.unitmanagement.translator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gepardec.hogarama.domain.unitmanagement.context.UserContext;
import com.gepardec.hogarama.domain.unitmanagement.entity.Owner;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;
import com.gepardec.hogarama.rest.unitmanagement.dto.UnitDto;


@ExtendWith(MockitoExtension.class)
public class UnitDtoTranslatorTest {

  private static final String EXAMPLE_UNIT = "ExampleUnit";
  private static final long   OWNER_ID     = 5L;
  private static final long   UNIT_ID      = 2L;

  @Mock
  private UserContext userContext;

  @InjectMocks
  private UnitDtoTranslator translator;

  @Test
  public void fromDto() {
    UnitDto dto = new UnitDto();
    dto.setDefaultUnit(false);
    dto.setDescription("A Example Unit");
    dto.setId(UNIT_ID);
    dto.setName(EXAMPLE_UNIT);

    Mockito.when(userContext.getOwner()).thenReturn(currentOwner());

    Unit result = translator.fromDto(dto);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(UNIT_ID);
    assertThat(result.getOwner().getId()).isEqualTo(OWNER_ID);
    assertThat(result.getName()).isEqualTo(EXAMPLE_UNIT);
  }

  private Owner currentOwner() {
    Owner owner = new Owner();
    owner.setId(OWNER_ID);
    return owner;
  }
}
