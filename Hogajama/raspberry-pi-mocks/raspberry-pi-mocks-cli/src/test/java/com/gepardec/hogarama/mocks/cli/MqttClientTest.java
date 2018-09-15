package com.gepardec.hogarama.mocks.cli;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MqttClientTest {
	
	@Mock
	private MQTT mockMQTT;
	
	@Mock
	private BlockingConnection mockBlockingConnection;
	
	private MqttClient mqttClient;
	
	@Before
	public void setup() {
		mqttClient = new MqttClient().
				withHost("testhost").
				withUser("testuser").
				withPassword("testpwd").
				withTopic("testtopic").
				build();
		Whitebox.setInternalState(mqttClient, "mqtt", mockMQTT);
		when(mockMQTT.blockingConnection()).thenReturn(mockBlockingConnection);
	}
	
	@Test
	public void testBasicRun() throws Exception {
		mqttClient.connectAndPublish("testmsg");
		verify(mockBlockingConnection, times(1)).connect();
		verify(mockBlockingConnection, times(1)).publish("testtopic", "testmsg".getBytes(), QoS.AT_LEAST_ONCE, false);
		verify(mockBlockingConnection, times(1)).disconnect();
	}
	
	@Test
	public void testMultipleMessages() throws Exception {
		List<String> messages = new ArrayList<>();
		messages.add("msg1");
		messages.add("msg2");
		mqttClient.connectAndPublish(messages, 0L);
		verify(mockBlockingConnection, times(1)).connect();
		verify(mockBlockingConnection, times(1)).publish("testtopic", "msg1".getBytes(), QoS.AT_LEAST_ONCE, false);
		verify(mockBlockingConnection, times(1)).publish("testtopic", "msg2".getBytes(), QoS.AT_LEAST_ONCE, false);
		verify(mockBlockingConnection, times(1)).disconnect();
	}
	
	@Test
	public void testFixBrokerUrl() throws Exception {
		check("ssl://myhost:443", "https://myhost");
		check("ssl://myhost:443", "ssl://myhost");
		check("ssl://myhost:8443", "https://myhost:8443");
		check("tcp://myhost:80", "http://myhost");
		check("tcp://myhost:8080", "http://myhost:8080");
	}

	private void check(String expected, String url) {
		assertEquals(expected, MqttClient.fixUrl(url));
	}
}
