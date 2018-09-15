package com.gepardec.hogarama.mocks.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttClient {
	
	private static final String SSL = "ssl";
	private static final String HTTPS = "https";
	private static String PORT_PROVIDED_REGEX ="(.*)(:\\d+)";
	private static final Logger LOGGER = LoggerFactory.getLogger(MqttClient.class);
	
	private MQTT mqtt;
	private String host;
	private String user;
	private String password;
	private String topic;

	public MqttClient defaultConnection() {
		return withURL(Optional.ofNullable(System.getenv("AMQ_HOST")).orElse("https://broker-amq-mqtt-ssl::8883")).
	      withUser(Optional.ofNullable(System.getenv("AMQ_USER")).orElse("mq_habarama")).
	      withPassword(Optional.ofNullable(System.getenv("AMQ_PASSWDORD")).orElse("mq_habarama_pass"));
	}	

	/**
	 * Set the URL to connect to the message broker
	 * @param url
	 * @return
	 */
	public MqttClient withURL(String url) {
		this.host = fixUrl(url);
		return this;
	}


	protected static String fixUrl(String url) {
		URI uri;
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		int port = uri.getPort();
		String scheme = uri.getScheme();
		if ( HTTPS.equals(scheme) ) {
			scheme = SSL;
		}
		if ( port == -1 &&  SSL.equals(scheme) ) {
			port = 443;
		}
		String host;
		try {
			host = new URI(scheme,uri.getUserInfo(), uri.getHost(), port, uri.getPath(), uri.getQuery(), uri.getFragment()).toString();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		return host;
	}


	/**
	 * Use {@link #withURL(String)} instead. This funktion contains obscure logic.
	 * @param host
	 * @return
	 */
	@Deprecated
	public MqttClient withHost(String host) {
		this.host = host.replaceAll(HTTPS, SSL);
		this.host = this.host.replaceAll("http", "tcp");
		if(!this.host.matches(PORT_PROVIDED_REGEX)) {
			this.host += ":8883";
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
			LOGGER.info("Publising to " + this.host);
			BlockingConnection connection = mqtt.blockingConnection();
			connection.connect();
			int counter = 0;
			for(String message : messages) {
				counter++;
				
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
