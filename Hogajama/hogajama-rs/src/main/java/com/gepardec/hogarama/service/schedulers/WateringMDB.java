package com.gepardec.hogarama.service.schedulers;

import java.io.IOException;
import java.util.Optional;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
// import org.jboss.ejb3.annotation.ResourceAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gepardec.hogarama.domain.metrics.Metrics;
import com.gepardec.hogarama.domain.sensor.SensorData;
import com.gepardec.hogarama.domain.sensor.SensorNormalizer;
import com.gepardec.hogarama.domain.unitmanagement.cache.SensorCache;
import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import com.gepardec.hogarama.domain.watering.WateringService;

// @ResourceAdapter("activemq-ext")
@MessageDriven(
    activationConfig = {
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "habarama::watering"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
    }
)
public class WateringMDB implements MessageListener {

    private static final Logger log = LoggerFactory.getLogger(WateringMDB.class);

    @Inject
    WateringService wateringSvc;

    @Inject
    SensorNormalizer sensorNormalizer;

    @Inject
    SensorCache sensors;
    
	public void onMessage(Message message) {
	    log.debug("Receive message of type " + message.getClass().getName());
	    BytesMessage msg = (BytesMessage) message;
		try {
		    byte[] b = new byte[(int) msg.getBodyLength()];
		    msg.readBytes(b);
 
		    ObjectMapper mapper = new ObjectMapper();
            SensorData sensorData = sensorNormalizer.normalize(mapper.readValue(b, SensorData.class));

            Optional<Sensor> sensor = sensors.getByDeviceId(sensorData.getSensorName());
            Metrics.sensorValues.labels(
                    sensorName(sensorData, sensor), 
                    deviceId(sensorData, sensor), 
                    unitName(sensorData, sensor)
                    ).set(sensorData.getValue());

            wateringSvc.water(sensorData);
            
		} catch (JMSException | IOException e) {
			throw new RuntimeException("Error handling sensor data!", e);
		}
	}

    private String sensorName(SensorData sensorData, Optional<Sensor> sensor) {
        return sensor.isPresent() ? sensor.get().getName() : sensorData.getSensorName();
    }

    private String deviceId(SensorData sensorData, Optional<Sensor> sensor) {
        return sensor.isPresent() ? sensor.get().getDeviceId() : sensorData.getSensorName();
    }

    private String unitName(SensorData sensorData, Optional<Sensor> sensor) {
        return sensor.isPresent() ? sensor.get().getUnit().getName() : sensorData.getLocation();
    }
}
