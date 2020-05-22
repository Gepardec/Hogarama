package com.gepardec.hogarama.domain.unitmanagement.service;

import com.gepardec.hogarama.domain.unitmanagement.cache.SensorCache;
import com.gepardec.hogarama.domain.unitmanagement.context.UnitManagementContext;
import com.gepardec.hogarama.domain.unitmanagement.dao.SensorDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import com.gepardec.hogarama.domain.unitmanagement.entity.UserProfile;
import com.gepardec.hogarama.domain.unitmanagement.service.UserProfileService;
import com.gepardec.hogarama.testdata.TestSensors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static com.gepardec.hogarama.testdata.TestSensors.DEVICE_GRUENER_GEPARD;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class UserProfileServiceTest {

    @Mock
    private UnitManagementContext context;
  
    @InjectMocks
    private UserProfileService service;
 
    @Test(expected = RuntimeException.class)
    public void userProfileNotSet_exceptionIsThrown() throws Exception {
        service.getUserProfile();
    }

    @Test
    public void userProfileSet_correctResult() throws Exception {
        UserProfile mockProfile = new UserProfile();
        Mockito.when(context.getUserProfile()).thenReturn(mockProfile);
        UserProfile userProfile = service.getUserProfile();
        assertThat(userProfile).isNotNull().isEqualTo(mockProfile);
    }


}
