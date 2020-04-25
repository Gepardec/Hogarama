package com.gepardec.hogarama.testdata;

import java.util.Optional;

import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import com.gepardec.hogarama.domain.unitmanagement.entity.SensorType;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;

public class TestSensors {

    public static final String DEVICE_GRUENER_GEPARD = "GruenerGepard";
    private static final String SPARKFUN = "sparkfun";
    private static final long SPARKFUN_ID = 6L;

    public static Optional<Sensor> sensorGruenerGepard() {
        Sensor sensor = new Sensor();
        sensor.setId(99L);
        sensor.setDeviceId(DEVICE_GRUENER_GEPARD);
        sensor.setName("Grüner Gepard");
        sensor.setSensorType( new SensorType(SPARKFUN_ID, SPARKFUN));
        sensor.setUnit(unit());
        return Optional.ofNullable(sensor);
    }

    private static Unit unit() {
        Unit unit = new Unit();
        unit.setId(1L);
        unit.setDescription("Unit Description");
        unit.setName("My Unit");
        return unit;
    }

    public TestSensors() {
        super();
    }

}