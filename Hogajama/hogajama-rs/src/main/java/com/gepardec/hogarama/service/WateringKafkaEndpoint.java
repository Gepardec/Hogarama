package com.gepardec.hogarama.service;

import java.io.IOException;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
// import org.jboss.ejb3.annotation.ResourceAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gepardec.hogarama.domain.metrics.Metrics;
import com.gepardec.hogarama.domain.sensor.SensorData;
import com.gepardec.hogarama.domain.sensor.SensorNormalizer;
import com.gepardec.hogarama.domain.sensor.SensorProperties;
import com.gepardec.hogarama.domain.unitmanagement.cache.SensorCache;
import com.gepardec.hogarama.domain.watering.WateringService;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class WateringKafkaEndpoint {

    private static final Logger log = LoggerFactory.getLogger(WateringKafkaEndpoint.class);

    @Inject
    WateringService wateringSvc;

    @Inject
    HogaramaServiceConfiguration config;

    @Inject
    SensorNormalizer sensorNormalizer;
    
    @Inject
    SensorCache sensors;
    
    @Incoming("habarama-in")
	public void onMessage(String message) {
        if ( !config.useKafkaWatering() ) {
            return;
        }
	    log.info("Receive message from habarama-in: " + message);
		try {
		    
		    ObjectMapper mapper = new ObjectMapper();
            SensorData sensorData = sensorNormalizer.normalize(mapper.readValue(message, SensorData.class));

            SensorProperties sensorProps = new SensorProperties(sensorData, sensors);
 
            Metrics.sensorValues.labels(
                    sensorProps.getSensorName(), 
                    sensorProps.getDeviceId(), 
                    sensorProps.getUnitName()
                    ).set(sensorData.getValue());

            wateringSvc.water(sensorData);
            
		} catch ( IOException e) {
			throw new RuntimeException("Error handling sensor data!", e);
		}
	}
}
