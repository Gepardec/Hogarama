package com.gepardec.hogajama.raspberrypimocks.websocket;

import java.io.IOException;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.gepardec.hogarama.mocks.cli.MqttClient;

@ServerEndpoint("/mock")
public class Endpoint {
	
	private static final String TEST_MESSAGE = "{\"sensorName\" : \"$name\", \"type\" : \"$type\", \"value\" : $value, \"location\" : \"Wien\", \"version\" : 0}";
	
	private MqttClient mqttClient;
	
	@PostConstruct
	public void init() {
		mqttClient = new MqttClient().
				withHost(Optional.ofNullable(System.getenv("AMQ_HOST")).orElse("https://broker-amq-mqtt-ssl-hogarama.10.0.75.2.nip.io")).
				withUser(Optional.ofNullable(System.getenv("AMQ_USER")).orElse("mq_habarama")).
				withPassword(Optional.ofNullable(System.getenv("AMQ_PASSWDORD")).orElse("mq_habarama_pass")).
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
