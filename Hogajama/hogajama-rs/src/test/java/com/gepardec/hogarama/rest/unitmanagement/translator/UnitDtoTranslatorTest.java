package com.gepardec.hogarama.rest.unitmanagement.translator;

import static org.assertj.core.api.Assertions.assertThat;

import com.gepardec.hogarama.domain.unitmanagement.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.gepardec.hogarama.domain.unitmanagement.context.UserContext;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;
import com.gepardec.hogarama.rest.unitmanagement.dto.UnitDto;


@ExtendWith(MockitoExtension.class)
public class UnitDtoTranslatorTest {

    private static final String EXAMPLE_UNIT = "ExampleUnit";
	  private static final long USER_ID = 5L;
    private static final long UNIT_ID = 2L;

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
    	
    	Mockito.when(userContext.getUser()).thenReturn(currentUser());
 
        Unit result = translator.fromDto(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(UNIT_ID);
        assertThat(result.getUser().getId()).isEqualTo(USER_ID);
        assertThat(result.getName()).isEqualTo(EXAMPLE_UNIT);
     }

    private User currentUser() {
		User user = new User();
		user.setId(USER_ID);
		return user;
	}
}
