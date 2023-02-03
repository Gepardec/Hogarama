package com.gepardec.hogarama.mocks.cli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private String broker;
    private String host;
	private String user;
	private String password;
	private String topic;
	private long delayMs;
	private List<String> mockMessages;
    private CommandLine cliArguments;
    private Properties properties;

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
	
	public boolean useKafka() {
	    return "kafka".equals(broker);
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
        LOGGER.info("Broker: " + getBroker());
        LOGGER.info("Host: " + getHost());
        LOGGER.info("User: " + getUser());
        LOGGER.info("Password: " + getPassword());
        LOGGER.info("Topic: " + getTopic());
        LOGGER.info("Delay ms: " + getDelayMs() + System.lineSeparator());
    }


}
