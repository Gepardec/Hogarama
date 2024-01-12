package com.gepardec.hogarama.domain.sensor;

import static com.gepardec.hogarama.testdata.TestSensors.DEVICE_GRUENER_GEPARD;
import static com.gepardec.hogarama.testdata.TestSensors.sensorGruenerGepard;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.gepardec.hogarama.domain.unitmanagement.cache.SensorCache;
import com.gepardec.hogarama.testdata.TestSensors;

@ExtendWith(MockitoExtension.class)
public class SensorPropertiesTest {

    private static final String DEVICE_ID_NOTFOUND = "SomeSensor";
 
    @Mock
    private SensorCache sensorCache;

    @BeforeEach
    public void setUpMethod() throws Exception {
        Mockito.when(sensorCache.getByDeviceId(DEVICE_ID_NOTFOUND)).thenReturn(Optional.empty());
        Mockito.when(sensorCache.getByDeviceId(DEVICE_GRUENER_GEPARD)).thenReturn(TestSensors.sensorGruenerGepard());
    }

    @Test
    public void dbSensorReturnsDeviceIdAsDeviceId() {
        assertThat(gruenerGepardProperties().getDeviceId())
        .isEqualTo(sensorGruenerGepard().get().getDeviceId());
    }
    
    @Test
    public void unknownSensorReturnsSensorNameAsDeviceId() {
        assertThat(unknownSensorProperties().getDeviceId())
        .isEqualTo(unknownSensorData().getSensorName());
    }

    @Test
    public void dbSensorReturnsNameAsSensorName() {
        assertThat(gruenerGepardProperties().getSensorName())
        .isEqualTo(sensorGruenerGepard().get().getName());
    }

    @Test
    public void unknownSensorReturnsSensorNameAsSensorName() {
        assertThat(unknownSensorProperties().getSensorName())
        .isEqualTo(unknownSensorData().getSensorName());
    }

    @Test
    public void dbSensorReturnsUnitNameAsUnitName() {
        assertThat(gruenerGepardProperties().getUnitName())
        .isEqualTo(sensorGruenerGepard().get().getUnit().getName());
    }

    @Test
    public void unknownSensorReturnsLocationAsUnitName() {
        assertThat(unknownSensorProperties().getUnitName())
        .isEqualTo(unknownSensorData().getLocation());
    }

    @Test
    public void dbSensorReturnsLINEAR1024AsMappingType() {
        assertThat(gruenerGepardProperties().getMappingType())
        .isEqualTo(MappingType.LINEAR1024);
    }
    
    @Test
    public void unknownSensorReturnsINVERSE_LINEAR1024AsMappingType() {
        assertThat(unknownSensorProperties().getMappingType())
        .isEqualTo(MappingType.INVERSE_LINEAR1024);
    }
    
    private SensorProperties gruenerGepardProperties() {
        return new SensorProperties(gruenerGepardData(), sensorCache);
    }

    private SensorProperties unknownSensorProperties() {
        return new SensorProperties(unknownSensorData(), sensorCache);
    }

    private SensorData unknownSensorData() {
        SensorData data = new SensorData();
        data.setSensorName(DEVICE_ID_NOTFOUND);
        data.setLocation("Linz");
        data.setType("Chinese Water Sensor");
        data.setVersion("1");
        data.setValue(0.45);
        return data;
    }

    private SensorData gruenerGepardData() {
        SensorData data = new SensorData();
        data.setSensorName(DEVICE_GRUENER_GEPARD);
        data.setLocation("Wien");
        data.setType("sparkfun");
        data.setVersion("1");
        data.setValue(0.566);
        return data;
    }

}
