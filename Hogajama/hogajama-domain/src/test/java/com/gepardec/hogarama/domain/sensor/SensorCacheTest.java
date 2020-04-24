package com.gepardec.hogarama.domain.sensor;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.map.HashedMap;
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
import com.gepardec.hogarama.domain.unitmanagement.entity.SensorType;


@RunWith(MockitoJUnitRunner.class)
public class SensorCacheTest {

    private static final String LINEAR1024 = "LINEAR1024";
    private static final String DEVICE_GRUENER_GEPARD = "GruenerGepard";
    private static final String SPARKFUN = "sparkfun";
    private static final long SPARKFUN_ID = 6L;
  
    @Mock
    private SensorDAO sensorDAO;
  
    @InjectMocks
    private SensorCache sensorCache;
 
    @Before
    public void setUpMethod() {
        Mockito.when(sensorDAO.getByDeviceId(DEVICE_GRUENER_GEPARD)).thenReturn(sensor());
    }
    
    @Test
    public void sensorCacheReturnsGruenerGepard() throws Exception {
        Optional<Sensor> sensor = sensorCache.getByDeviceId(DEVICE_GRUENER_GEPARD);
        assertThat(sensor.isPresent());
        assertThat(sensor.get().getName()).isEqualTo("Grüner Gepard");
    }
    
    @Test
    public void GruenerGepardHasMappingTypeLINEAR1024() throws Exception {
        Optional<Sensor> sensor = sensorCache.getByDeviceId(DEVICE_GRUENER_GEPARD);
        assertThat(sensor.get().getSensorType().getMappingType().name()).isEqualTo(LINEAR1024);
    }
            
    private Map<Long, SensorType> sensorTypeMap() {
        Map<Long, SensorType> map = new HashedMap<Long, SensorType>();
        map.put(1L, new SensorType(1L, "IDENTITY"));
        map.put(2L, new SensorType(2L, "LINEAR100"));
        map.put(3L, new SensorType(3L, LINEAR1024));
        map.put(4L, new SensorType(4L, "INVERSE_LINEAR1024"));
        map.put(5L, new SensorType(5L, "Chinese Water Sensor"));
        map.put(SPARKFUN_ID, new SensorType(SPARKFUN_ID, SPARKFUN));

        return map;
    }
    
    private Optional<Sensor> sensor() {
        Sensor sensor = new Sensor();
        sensor.setId(99L);
        sensor.setDeviceId(DEVICE_GRUENER_GEPARD);
        sensor.setName("Grüner Gepard");
        sensor.setSensorType( new SensorType(SPARKFUN_ID, SPARKFUN));
        return Optional.ofNullable(sensor);
    }


}
