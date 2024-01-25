package com.gepardec.hogarama.domain.unitmanagement.service;

import com.gepardec.hogarama.domain.unitmanagement.context.UserContext;
import com.gepardec.hogarama.domain.unitmanagement.entity.UserProfile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceTest {

    @Mock
    private UserContext userContext;
  
    @InjectMocks
    private UserProfileService service;
 
    @Test
    public void userProfileNotSet_exceptionIsThrown() throws Exception {
        assertThrows(RuntimeException.class, () -> {
            service.getUserProfile();
        });
    }

    @Test
    public void userProfileSet_correctResult() throws Exception {
        UserProfile mockProfile = new UserProfile();
        Mockito.when(userContext.getUserProfile()).thenReturn(mockProfile);
        UserProfile userProfile = service.getUserProfile();
        assertThat(userProfile).isNotNull().isEqualTo(mockProfile);
    }


}
