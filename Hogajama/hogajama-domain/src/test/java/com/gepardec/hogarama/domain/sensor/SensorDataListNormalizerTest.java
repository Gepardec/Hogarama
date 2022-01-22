package com.gepardec.hogarama.domain.sensor;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.gepardec.hogarama.domain.unitmanagement.cache.SensorCache;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SensorDataListNormalizerTest {

    private static final String DEVICE_ID = "My Plant";
 
    @Mock
    private SensorCache sensorCache;

    @InjectMocks
    private SensorNormalizer sn;

    
	@BeforeEach
	public void setUpMethod() {
        Mockito.lenient().when(sensorCache.getByDeviceId(DEVICE_ID)).thenReturn(Optional.empty());
	}

	@Test
	public void emptyListReturnsEmptyList() {
		List<SensorData> data = sn.normalize(Collections.emptyList());
		assertTrue(data.isEmpty());
	}

	@Test
	public void testFullList() {
		SensorNormalizerTest.checkNormalised(
				sn.normalize(SensorNormalizerTest.getDataList()));
	}
}
