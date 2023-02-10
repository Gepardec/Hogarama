package com.gepardec.hogarama.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gepardec.hogarama.domain.sensor.SensorData;
import com.gepardec.hogarama.domain.watering.WateringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.io.IOException;

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

    public void onMessage(Message message) {
        log.debug("Receive message of type " + message.getClass().getName());
        BytesMessage msg = (BytesMessage) message;
        try {
            byte[] b = new byte[(int) msg.getBodyLength()];
            msg.readBytes(b);

            ObjectMapper mapper = new ObjectMapper();
            SensorData sensorData = mapper.readValue(b, SensorData.class);

            wateringSvc.processSensorData(sensorData);

        } catch (JMSException | IOException e) {
            throw new RuntimeException("Error handling sensor data!", e);
        }
    }
}
