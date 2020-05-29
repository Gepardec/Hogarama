package com.gepardec.hogarama.domain.unitmanagement.service;

import com.gepardec.hogarama.domain.unitmanagement.context.UserContext;
import com.gepardec.hogarama.domain.unitmanagement.entity.UserProfile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class UserProfileServiceTest {

    @Mock
    private UserContext userContext;
  
    @InjectMocks
    private UserProfileService service;
 
    @Test(expected = RuntimeException.class)
    public void userProfileNotSet_exceptionIsThrown() throws Exception {
        service.getUserProfile();
    }

    @Test
    public void userProfileSet_correctResult() throws Exception {
        UserProfile mockProfile = new UserProfile();
        Mockito.when(userContext.getUserProfile()).thenReturn(mockProfile);
        UserProfile userProfile = service.getUserProfile();
        assertThat(userProfile).isNotNull().isEqualTo(mockProfile);
    }


}
