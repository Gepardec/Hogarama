package com.gepardec.hogajama.raspberrypimocks.websocket;

import com.gepardec.hogarama.mocks.cli.MqttClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Optional;

@ServerEndpoint("/mock")
public class Endpoint {
	
	private static final String TEST_MESSAGE = "{\"sensorName\" : \"$name\", \"type\" : \"$type\", \"value\" : $value, \"location\" : \"Wien\", \"version\" : 0}";
	private static final Logger LOGGER = LoggerFactory.getLogger(Endpoint.class);
	
	private MqttClient mqttClient;
	
	@PostConstruct
	public void init() {
		mqttClient = new MqttClient().
				defaultConnection().
				withTopic(Optional.ofNullable(System.getenv("AMQ_TOPICS")).orElse("habarama")).
				build(); 
		
	}
	
	@OnOpen
	public void open(Session session) {
	}
	
	@OnClose
	public void close(Session session, CloseReason c) {
	}
	
	@OnMessage
	public void receiveMessage(String messageString, Session session) {
		try {
			if("ping".equals(messageString)) {
				session.getBasicRemote().sendText("pong");
			} else {
				String[] values = messageString.split(",");
				String messageToSend = TEST_MESSAGE.replace("$name", values[0]).replace("$type", values[1]).replace("$value", values[2]);
				mqttClient.connectAndPublish(messageToSend);
				session.getBasicRemote().sendText("ok");
			}
		} catch (IOException e) {
			LOGGER.error("Exception occured while receiving messages", e);
		}
	}

}
