package com.gepardec.hogarama.mocks.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttClient {
	
	public static String PORT_PROVIDED_REGEX ="(.*)(:\\d+)";
	public static final Logger LOGGER = LoggerFactory.getLogger(MqttClient.class);
	
	private MQTT mqtt;
	private String host;
	private String user;
	private String password;
	private String topic;
	
	public MqttClient withHost(String host) {
		this.host = host.replaceAll("https", "ssl");
		this.host = this.host.replaceAll("http", "tcp");
		if(!this.host.matches(PORT_PROVIDED_REGEX)) {
			this.host += ":443";
		}
		return this;
	}
	
	public MqttClient withUser(String user) {
		this.user = user;
		return this;
	}
	
	public MqttClient withPassword(String password) {
		this.password = password;
		return this;
	}
	
	public MqttClient withTopic(String topic) {
		this.topic = topic;
		return this;
	}
	
	public MqttClient build() {
		try {
			mqtt = new MQTT();
			mqtt.setHost(host);
			mqtt.setUserName(user);
			mqtt.setPassword(password);
			mqtt.setConnectAttemptsMax(2);
			
			SSLContext sslContext = createSSLContext();
			mqtt.setSslContext(sslContext);
			
			return this;
		} catch (Exception e) {
			LOGGER.error("Error while configuring ", e);
			return null;
		}
	}
	
	public void connectAndPublish(List<String> messages, long delayMs) {
		try {
			BlockingConnection connection = mqtt.blockingConnection();
			connection.connect();
			int counter = 0;
			for(String message : messages) {
				counter++;
				LOGGER.info("Publising " + counter + " of " + messages.size() + " to " + this.host);
				connection.publish(this.topic, message.getBytes(), QoS.AT_LEAST_ONCE, false);
				LOGGER.info("Published " + counter + " of " + messages.size());
				Thread.sleep(delayMs);
			};
			connection.disconnect();
		} catch (Exception e) {
			LOGGER.error("Error while publishing ", e);
		}
	}
	
	public void connectAndPublish(String ... messages) {
		connectAndPublish(Arrays.asList(messages), 1000L);
	}

	private SSLContext createSSLContext() throws NoSuchAlgorithmException, KeyStoreException, IOException,
			CertificateException, FileNotFoundException, UnrecoverableKeyException, KeyManagementException {
		SSLContext sslContext = SSLContext.getInstance("TLSv1.2");

		KeyManagerFactory factory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(MockCli.class.getResourceAsStream("/broker.jks"), "L(o?cqGPtJ}7YiHu".toCharArray());
		
		InputStream keystoreInputStream = MockCli.class.getResourceAsStream("/broker.jks");
		byte[] keystoreBuffer = new byte[keystoreInputStream.available()];
		keystoreInputStream.read(keystoreBuffer);
		
		File keystoreTempFile = File.createTempFile("broker", ".jks");
		FileOutputStream keystoreOutputStream = new FileOutputStream(keystoreTempFile);
		keystoreOutputStream.write(keystoreBuffer);
		keystoreOutputStream.close();
	    
		System.setProperty("javax.net.ssl.trustStore" , keystoreTempFile.getAbsolutePath());
		
		factory.init(keyStore, "L(o?cqGPtJ}7YiHu".toCharArray());
		sslContext.init(factory.getKeyManagers(), null, null);
		
		return sslContext;
	}	
	
}
