package com.gepardec.mqtt.client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SNIServerName;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/**
 * 
 * @author mkapferer
 * @see https://gist.github.com/Montecito/a60c8193f43b0b8c85f8096cea8fe3f3
 */
public class SimpleMqttClient implements MqttCallback {

	MqttClient myClient;
	MqttConnectOptions connOpt;

	static final String BROKER_URL = "tcp://localhost:1883";
	static final String OPENSHIFT_HOST = "broker-amq-mqtt-ssl-57-hogarama.cloud.itandtel.at";
	static final String BROKER_URL_SSL = "ssl://"+OPENSHIFT_HOST+":443";
	
	static final String CLIENT_TRUSTSTORE_PATH = "/home/mkapferer/OpenShift/ITAndTEL/Hogarama/Hogarama/AMQ/client.ts";
	static final String CLIENT_TRUSTSTORE_PASSWD = "L(o?cqGPtJ}7YiHu";
	
//	static final String M2MIO_DOMAIN = "<Insert m2m.io domain here>";
//	static final String M2MIO_STUFF = "test";
	static final String M2MIO_STUFF = "habarama";
//	static final String M2MIO_THING = "<Unique device ID>";
//	static final String M2MIO_USERNAME = "E5vGV4Uu4n7f3P8z";
//	static final String M2MIO_PASSWORD_MD5 = "zXw5WU3zG99xHZNv";
	static final String M2MIO_USERNAME = "mq_habarama";
	static final String M2MIO_PASSWORD_MD5 = "mq_habarama_pass";
	
	// the following two flags control whether this example is a publisher, a
	// subscriber or both
	static final Boolean subscriber = false;
	static final Boolean publisher = true;
	static final Boolean sslEnabled = true;
	static final int mqttPackageRepeat = 1;

	/**
	 * 
	 * connectionLost This callback is invoked upon losing the MQTT connection.
	 * 
	 */
	public void connectionLost(Throwable t) {
		System.out.println("Connection lost!");
		// code to reconnect to the broker would go here if desired
	}

	/**
	 * 
	 * deliveryComplete This callback is invoked when a message published by
	 * this client is successfully received by the broker.
	 * 
	 */
	public void deliveryComplete(MqttDeliveryToken token) {
		// System.out.println("Pub complete" + new
		// String(token.getMessage().getPayload()));
	}
	
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub
	}

	/**
	 * 
	 * messageArrived This callback is invoked when a message is received on a
	 * subscribed topic.
	 * 
	 */
	public void messageArrived(MqttTopic topic, MqttMessage message) throws Exception {
		System.out.println("-------------------------------------------------");
		System.out.println("| Topic:" + topic.getName());
		System.out.println("| Message: " + new String(message.getPayload()));
		System.out.println("-------------------------------------------------");
	}
	
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println("-------------------------------------------------");
		System.out.println("| Topic:" + topic);
		System.out.println("| Message: " + new String(message.getPayload()));
		System.out.println("-------------------------------------------------");
	}
	
	public MqttClient getClientSSLwithSNI(String clientID) throws MqttException, NoSuchAlgorithmException, KeyManagementException{

		SSLContext sc = SSLContext.getInstance("SSL");
		
		TrustManager[] myTrustManagerArray = new TrustManager[]{new TrustEveryoneManager()};
		KeyManagerFactory myKeyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		
		KeyStore trustStore;
		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		try {
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(new FileInputStream(CLIENT_TRUSTSTORE_PATH), CLIENT_TRUSTSTORE_PASSWD.toCharArray());
			
			trustManagerFactory.init(trustStore);
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		
		sc.init(null, trustManagerFactory.getTrustManagers(), new java.security.SecureRandom());
		
		URL url = null;
		try {
			url = new URL("https://"+OPENSHIFT_HOST);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		
		List<SNIServerName> sniServerNames = new ArrayList<SNIServerName>(1);
		sniServerNames.add(new SNIHostName(url.getHost()));
		
		SSLParameters sslParameters = new SSLParameters();
		sslParameters.setServerNames(sniServerNames);
		
		SSLSocketFactory wrappedSSLSocketFactory = new SSLSocketFactoryWrapper(sc.getSocketFactory(), sslParameters);
		
		connOpt.setSocketFactory(wrappedSSLSocketFactory);
		
		return new MqttClient(BROKER_URL_SSL, clientID);
	}
	
	/**
	 * 
	 * runClient The main functionality of this simple example. Create a MQTT
	 * client, connect to broker, pub/sub, disconnect.
	 * 
	 */
	public void runClient() {
		// setup MQTT Client
		//String clientID = M2MIO_THING;
		String clientID = MqttClient.generateClientId();
		connOpt = new MqttConnectOptions();

//		connOpt.setCleanSession(true);
		connOpt.setKeepAliveInterval(30);
		connOpt.setUserName(M2MIO_USERNAME);
		connOpt.setPassword(M2MIO_PASSWORD_MD5.toCharArray());

		// Connect to Broker
		try {
			if(sslEnabled){
//				myClient = getClientSSLHive(clientID);
				myClient = getClientSSLwithSNI(clientID);
			} else {
				myClient = new MqttClient(BROKER_URL, clientID);
			}
			myClient.setCallback(this);
			myClient.connect(connOpt);
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		
		if(sslEnabled){
			System.out.println("Connected to " + BROKER_URL_SSL);
		} else {
			System.out.println("Connected to " + BROKER_URL);
		}
		
		// setup topic
		// topics on m2m.io are in the form <domain>/<stuff>/<thing>
//		String myTopic = M2MIO_DOMAIN + "/" + M2MIO_STUFF + "/" + M2MIO_THING;
		String myTopic = M2MIO_STUFF ;
		MqttTopic topic = myClient.getTopic(myTopic);

		// subscribe to topic if subscriber
		if (subscriber) {
			try {
				int subQoS = 0;
				myClient.subscribe(myTopic, subQoS);
				System.out.println("Subscribed to topic " + myTopic);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// publish messages if publisher
		if (publisher) {
			for (int i = 1; i <= mqttPackageRepeat; i++) {
//				String pubMsg = "{\"pubmsg\":" + i + "}";
//				String pubMsg = //"{"+
//							//"'habarama':"+
//								"{"+
//								"\"habarama_id\":"+"\""+clientID+"\"," +
//								"\"sensor_data\":"+"{"
//									+"\"temp\":"+27.2+","
//									+"\"comment\":"+"\"Too hot!\""
//								+"}"
////							+"}"
//						+"}";
				String pubMsg = "{\"pubmsg\": 1.5 }";
				int pubQoS = 0;
				MqttMessage message = new MqttMessage(pubMsg.getBytes());
				message.setQos(pubQoS);
				message.setRetained(false);

				// Publish the message
				System.out.println("Publishing to topic \"" + topic + "\" qos " + pubQoS);
				MqttDeliveryToken token = null;
				try {
					// publish message to broker
					token = topic.publish(message);
					// Wait until the message has been delivered to the broker
					token.waitForCompletion();
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// disconnect
		try {
			// wait to ensure subscribed messages are delivered
			if (subscriber) {
				Thread.sleep(5000);
			}
			myClient.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class TrustEveryoneManager implements X509TrustManager {
	    public void checkClientTrusted(X509Certificate[] arg0, String arg1){
	    	System.out.println("############# checkClientTrusted #############");
	    	for(X509Certificate cert : arg0){
	    		System.out.println(cert.toString());
	    	}
	    	System.out.println(arg1);
	    	System.out.println("##########################");
	    }
	    public void checkServerTrusted(X509Certificate[] arg0, String arg1){
	    	System.out.println("############# checkServerTrusted #############");
	    	for(X509Certificate cert : arg0){
	    		System.out.println(cert.toString());
	    	}
	    	System.out.println(arg1);
	    	System.out.println("##########################");
	    }
	    public X509Certificate[] getAcceptedIssuers() {
	    	System.out.println("############# getAcceptedIssuers #############");
	    	
	    	KeyStore trustStore = null;
			try {
				trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//				trustStore.load(this.getClass().getResourceAsStream(CLIENT_TRUSTSTORE_PATH), CLIENT_TRUSTSTORE_PASSWD.toCharArray());
				trustStore.load(new FileInputStream("src/main/resources/client2.ts"), CLIENT_TRUSTSTORE_PASSWD.toCharArray());
				
			} catch (KeyStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CertificateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Certificate crt = null;
			try {
				System.out.println(trustStore.aliases());
				Enumeration<String> en = trustStore.aliases();
				while(en.hasMoreElements()){
					System.out.println(en.nextElement());
				}
				crt = trustStore.getCertificate("broker");
			} catch (KeyStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			X509Certificate cert = (X509Certificate) crt;

	    	X509Certificate[] ret = { cert };
	    	
//	    	for(X509Certificate c : ret){
//	    		System.out.println(c.toString());
//	    	}
	    	System.out.println("Size of ret:" + ret.length);
	    	System.out.println(ret[0]);
	    	
	    	System.out.println("##########################");
	    	return ret;
	    }
	}
}
