package com.gepardec.hogarama.service.schedulers;

import java.io.IOException;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
// import org.jboss.ejb3.annotation.ResourceAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gepardec.hogarama.domain.sensor.SensorData;
import com.gepardec.hogarama.domain.sensor.SensorNormalizer;
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
    
    @Inject
    WateringService wateringSvc;

    @Inject
    SensorNormalizer sensorNormalizer;

	public void onMessage(Message message) {
	    BytesMessage msg = (BytesMessage) message;
		try {
		    byte[] b = new byte[(int) msg.getBodyLength()];
		    msg.readBytes(b);
 
		    ObjectMapper mapper = new ObjectMapper();
            SensorData sensorData = mapper.readValue(b, SensorData.class);

            wateringSvc.water(sensorNormalizer.normalize(sensorData));
            
            System.out.println("Message Type " + message.getClass().getName());
		} catch (JMSException | IOException e) {
			e.printStackTrace();
		}
	}
}
