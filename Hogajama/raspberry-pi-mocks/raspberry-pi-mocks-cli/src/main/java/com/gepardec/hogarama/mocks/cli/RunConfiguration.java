package com.gepardec.hogarama.mocks.cli;

import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RunConfiguration {
    
    public static final Logger LOGGER = LoggerFactory.getLogger(RunConfiguration.class);
    
    public static final String BROKER = "broker";
    public static final String BROKER_HOST = "brokerHost";
    public static final String BROKER_USERNAME = "brokerUsername";
    public static final String BROKER_PASSWORD = "brokerPassword";
    public static final String BROKER_TOPIC = "brokerTopic";
    public static final String PATH_TO_TEST_DATA = "testData";
    public static final String DELAY_MS = "delayMs";
    public static final String PATH_TO_CONFIG = "configuration";

    // Options for Kafka
    public static final String SECURITY_PROTOCOL = "securityProtocol";
    public static final String SSL_TRUSTSTORE_LOCATION = "sslTruststoreLocation";
    public static final String SSL_TRUSTSTORE_PASSWORD = "sslTruststorePassword";

    // Options for Rest-Endpoint-URL
    public static final String DUMMY_MESSAGING_REST_ENDPOINT_URL = "dummyMessagingRestEndpointUrl";

    private CommandLine cliArguments;
    private Properties properties;

    private String broker;
    private String host;
    private String user;
    private String password;
    private String topic;
    private long delayMs;
    private List<String> mockMessages;

    // For Kafka
    private String securityProtocol;
    private String sslTruststoreLocation;
    private String sslTruststorePassword;

    // For Dummy Messaging REST Url
    private String dummyMessagingRestEndpointUrl;

    public RunConfiguration(CommandLine cliArguments, Properties properties) {

        this.cliArguments = cliArguments;
        this.properties = properties;

        broker = configParam(BROKER, "amq");
        host = configParam(BROKER_HOST, "https://broker-amq-mqtt-ssl-hogarama.10.0.75.2.nip.io");
        user = configParam(BROKER_USERNAME, "mq_habarama");
        password = configParam(BROKER_PASSWORD, "mq_habarama_pass");
        topic = configParam(BROKER_TOPIC, "habarama");
        delayMs = Long.parseLong(configParam(DELAY_MS, "3000"));

        mockMessages = getTestMessages();

        securityProtocol = configParam(SECURITY_PROTOCOL, "PLAINTEXT");
        sslTruststoreLocation = configParam(SSL_TRUSTSTORE_LOCATION, "");
        sslTruststorePassword = configParam(SSL_TRUSTSTORE_PASSWORD, "password");

        dummyMessagingRestEndpointUrl = configParam(DUMMY_MESSAGING_REST_ENDPOINT_URL, "http://localhost:8080/hogajama-rs/rest/messaging/sensorData");

    }

    public String getBroker() {
        return broker;
    }

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getTopic() {
        return topic;
    }

    public long getDelayMs() {
        return delayMs;
    }

    public List<String> getMockMessages() {
        return mockMessages;
    }

    public String getSecurityProtocol() {
        return securityProtocol;
   }

    public String getSslTruststoreLocation() {
        return sslTruststoreLocation;
    }

    public String getSslTruststorePassword() {
        return sslTruststorePassword;
    }

    public String getDummyMessagingRestEndpointUrl() {
        return dummyMessagingRestEndpointUrl;
    }

    private List<String> getTestMessages() {

        try {
            String pathToTestData = cliArguments.getOptionValue(RunConfiguration.PATH_TO_TEST_DATA);
            String content = new String(Files.readAllBytes(Paths.get(pathToTestData)));
            List<String> messages = new ArrayList<>();
            Matcher m = Pattern.compile("\\{[^\\{\\}]*\\}").matcher(content);
            while (m.find()) {
                messages.add(m.group());
            }
            return messages;

        } catch (IOException e) {
            MockCli.LOGGER.error("Exception occured while getting test messages", e);
            System.exit(1);
            return null;
        }
    }

    private String configParam(String paramName, String defaultValue) {

        if (cliArguments.hasOption(paramName)) {
            return cliArguments.getOptionValue(paramName);
        } else if (properties.containsKey(paramName)) {
            return properties.getProperty(paramName);
        } else {
            return defaultValue;
        }
    }

    public void print() {

        LOGGER.info("=================== Hogarama Mock Cli Config =================");
        print(BROKER, getBroker());
        print(BROKER_HOST, getHost());
        print(BROKER_USERNAME, getUser());
        print(BROKER_PASSWORD, getPassword());
        print(BROKER_TOPIC, getTopic());
        print(DELAY_MS, getDelayMs());

        LOGGER.info("For Kafka");
        print(SECURITY_PROTOCOL, getSecurityProtocol());
        print(SSL_TRUSTSTORE_LOCATION, getSslTruststoreLocation());
        LOGGER.info(System.lineSeparator());

        LOGGER.info("For Rest");
        print(DUMMY_MESSAGING_REST_ENDPOINT_URL, getDummyMessagingRestEndpointUrl());
        LOGGER.info(System.lineSeparator());
    }

    private void print(String key, Object value) {
        LOGGER.info(key + "=" + value);        
    }


}
