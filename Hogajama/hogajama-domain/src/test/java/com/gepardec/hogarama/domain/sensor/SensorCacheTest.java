package com.gepardec.hogarama.domain.sensor;

import static com.gepardec.hogarama.testdata.TestSensors.DEVICE_GRUENER_GEPARD;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.gepardec.hogarama.domain.unitmanagement.cache.SensorCache;
import com.gepardec.hogarama.domain.unitmanagement.dao.SensorDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import com.gepardec.hogarama.testdata.TestSensors;

@RunWith(MockitoJUnitRunner.class)
public class SensorCacheTest{

    private static final String LINEAR1024 = "LINEAR1024";
    @Mock
    private SensorDAO sensorDAO;
  
    @InjectMocks
    private SensorCache sensorCache;
 
    @Before
    public void setUpMethod() {
        Mockito.when(sensorDAO.getByDeviceId(DEVICE_GRUENER_GEPARD)).thenReturn(TestSensors.sensorGruenerGepard());
    }
    
    @Test
    public void sensorCacheReturnsGruenerGepard() throws Exception {
        Optional<Sensor> sensor = sensorCache.getByDeviceId(DEVICE_GRUENER_GEPARD);
        assertThat(sensor.isPresent());
        assertThat(sensor.get().getName()).isEqualTo("Grüner Gepard");
        Mockito.verify(sensorDAO).getByDeviceId(DEVICE_GRUENER_GEPARD);
    }
    
    @Test
    public void sensorCacheCaches() throws Exception {
        Optional<Sensor> sensor = sensorCache.getByDeviceId(DEVICE_GRUENER_GEPARD);
        assertThat(sensor.isPresent());
        assertThat(sensor.get().getName()).isEqualTo("Grüner Gepard");
        sensor = sensorCache.getByDeviceId(DEVICE_GRUENER_GEPARD);
        assertThat(sensor.isPresent());
        assertThat(sensor.get().getName()).isEqualTo("Grüner Gepard");
        Mockito.verify(sensorDAO).getByDeviceId(DEVICE_GRUENER_GEPARD);
        Mockito.verifyNoMoreInteractions(sensorDAO);
    }
    
    @Test
    public void invalidateRemovesCachedValue() throws Exception {
        Optional<Sensor> sensor = sensorCache.getByDeviceId(DEVICE_GRUENER_GEPARD);
        assertThat(sensor.isPresent());
        assertThat(sensor.get().getName()).isEqualTo("Grüner Gepard");
        sensorCache.invalidateCache(sensor.get());
        sensor = sensorCache.getByDeviceId(DEVICE_GRUENER_GEPARD);
        assertThat(sensor.isPresent());
        assertThat(sensor.get().getName()).isEqualTo("Grüner Gepard");
        Mockito.verify(sensorDAO, Mockito.times(2)).getByDeviceId(DEVICE_GRUENER_GEPARD);
    }
    
    @Test
    public void GruenerGepardHasMappingTypeLINEAR1024() throws Exception {
        Optional<Sensor> sensor = sensorCache.getByDeviceId(DEVICE_GRUENER_GEPARD);
        assertThat(sensor.get().getSensorType().getMappingType().name()).isEqualTo(LINEAR1024);
    }


}
