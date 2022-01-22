package com.gepardec.hogarama.domain.sensor;

import static com.gepardec.hogarama.testdata.TestSensors.DEVICE_GRUENER_GEPARD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gepardec.hogarama.domain.unitmanagement.cache.SensorCache;
import com.gepardec.hogarama.domain.unitmanagement.dao.SensorDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import com.gepardec.hogarama.testdata.TestSensors;


@ExtendWith(MockitoExtension.class)
public class SensorCacheTest{

    private static final String LINEAR1024 = "LINEAR1024";
    @Mock
    private SensorDAO sensorDAO;
  
    @InjectMocks
    private SensorCache sensorCache;
 
    @BeforeEach
    public void setUpMethod() {
        Mockito.when(sensorDAO.getByDeviceId(DEVICE_GRUENER_GEPARD)).thenReturn(TestSensors.sensorGruenerGepard());
    }
    
    @Test
    public void sensorCacheReturnsGruenerGepard() {
        Optional<Sensor> sensor = sensorCache.getByDeviceId(DEVICE_GRUENER_GEPARD);
        assertTrue(sensor.isPresent());
        assertThat(sensor.get().getName()).isEqualTo("Grüner Gepard");
        Mockito.verify(sensorDAO).getByDeviceId(DEVICE_GRUENER_GEPARD);
    }
    
    @Test
    public void sensorCacheCaches() {
        Optional<Sensor> sensor = sensorCache.getByDeviceId(DEVICE_GRUENER_GEPARD);
        assertTrue(sensor.isPresent());
        assertThat(sensor.get().getName()).isEqualTo("Grüner Gepard");
        sensor = sensorCache.getByDeviceId(DEVICE_GRUENER_GEPARD);
        assertTrue(sensor.isPresent());
        assertThat(sensor.get().getName()).isEqualTo("Grüner Gepard");
        Mockito.verify(sensorDAO).getByDeviceId(DEVICE_GRUENER_GEPARD);
        Mockito.verifyNoMoreInteractions(sensorDAO);
    }
    
    @Test
    public void invalidateRemovesCachedValue() {
        Optional<Sensor> sensor = sensorCache.getByDeviceId(DEVICE_GRUENER_GEPARD);
        assertTrue(sensor.isPresent());
        assertThat(sensor.get().getName()).isEqualTo("Grüner Gepard");
        sensorCache.invalidateCache(sensor.get());
        sensor = sensorCache.getByDeviceId(DEVICE_GRUENER_GEPARD);
        assertTrue(sensor.isPresent());
        assertThat(sensor.get().getName()).isEqualTo("Grüner Gepard");
        Mockito.verify(sensorDAO, Mockito.times(2)).getByDeviceId(DEVICE_GRUENER_GEPARD);
    }
    
    @Test
    public void GruenerGepardHasMappingTypeLINEAR1024() {
        Optional<Sensor> sensor = sensorCache.getByDeviceId(DEVICE_GRUENER_GEPARD);
        //noinspection OptionalGetWithoutIsPresent
        assertThat(sensor.get().getSensorType().getMappingType().name()).isEqualTo(LINEAR1024);
    }
}
