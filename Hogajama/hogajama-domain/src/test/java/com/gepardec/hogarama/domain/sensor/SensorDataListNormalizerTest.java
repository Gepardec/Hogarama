package com.gepardec.hogarama.domain.sensor;

import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.gepardec.hogarama.domain.unitmanagement.cache.SensorCache;

@RunWith(MockitoJUnitRunner.class)
public class SensorDataListNormalizerTest {

    private static final String DEVICE_ID = "My Plant";
 
    @Mock
    private SensorCache sensorCache;

    @InjectMocks
    private SensorNormalizer sn;

    
	@Before
	public void setUpMethod() throws Exception {
        Mockito.when(sensorCache.getByDeviceId(DEVICE_ID)).thenReturn(Optional.empty());
	}

	@Test
	public void emptyListReturnsEmptyList() {
		List<SensorData> data = sn.normalize(Collections.emptyList());
		assertTrue(data.isEmpty());
	}

	@Test
	public void testFullList() throws Exception {
		SensorNormalizerTest.checkNormalised(
				sn.normalize(SensorNormalizerTest.getDataList()));
	}
}
