package com.gepardec.hogarama.mocks.cli;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MockCli {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(MockCli.class);
	
	public static void main(String[] args) 	{
		
		RunConfiguration runConfiguration = createRunConfigurationFromArguments(args);
		printRunConfiguration(runConfiguration);
		
		MqttClient mqttClient = new MqttClient().
				withHost(runConfiguration.getHost()).
				withUser(runConfiguration.getUser()).
				withPassword(runConfiguration.getPassword()).
				withTopic(runConfiguration.getTopic()).
				build(); 
		
		mqttClient.connectAndPublish(runConfiguration.getMockMessages(), runConfiguration.getDelayMs());
		
		
		LOGGER.info(System.lineSeparator() + "=================== Hogarama Mock Cli Finished =================");
	}
	

	protected static RunConfiguration createRunConfigurationFromArguments(String[] args) {
		CommandLine cliArguments = parseCliArguments(args);
		List<String> testMessages = getTestMessages(cliArguments);
		Properties properties = loadProperties(cliArguments.getOptionValue(RunConfiguration.PATH_TO_CONFIG));
		long delayMs = Long.parseLong(getConfigParam(RunConfiguration.DELAY_MS, "3000", cliArguments, properties));
		String host = getConfigParam(RunConfiguration.BROKER_HOST, "https://broker-amq-mqtt-ssl-hogarama.10.0.75.2.nip.io", cliArguments, properties);
		String user = getConfigParam(RunConfiguration.BROKER_USERNAME, "mq_habarama", cliArguments, properties);
		String password = getConfigParam(RunConfiguration.BROKER_PASSWORD, "mq_habarama_pass", cliArguments, properties);
		String topic = getConfigParam(RunConfiguration.BROKER_TOPIC, "habarama", cliArguments, properties);
		
		RunConfiguration configuration = new RunConfiguration();
		configuration.setDelayMs(delayMs);
		configuration.setHost(host);
		configuration.setPassword(password);
		configuration.setTopic(topic);
		configuration.setUser(user);
		configuration.setMockMessages(testMessages);
		
		return configuration;
	}


	private static Options createOptions() {
		Options options = new Options();
		options.addOption(new Option( "h", "help", false, "print this message" ));
		options.addRequiredOption("t", RunConfiguration.PATH_TO_TEST_DATA, true, "Absolute path to file with test data");
		options.addOption("c", RunConfiguration.PATH_TO_CONFIG, true, "Absolute path to configuration file. Shoud contains key, value pairs in form key=value");
		options.addOption(new Option( "d", RunConfiguration.DELAY_MS, true, "Delay between publishings in ms. It overrides the parameter delay in configuration file. Default value is 3000." ));
		options.addOption(new Option( "host", RunConfiguration.BROKER_HOST, true, "Url to AMQ Broker. Overrides the parameter in configuration file." ));
		options.addOption(new Option( "u", RunConfiguration.BROKER_USERNAME, true, "Username. Overrides the parameter in configuration file." ));
		options.addOption(new Option( "p", RunConfiguration.BROKER_PASSWORD, true, "Password. Overrides the parameter in configuration file." ));
		options.addOption(new Option( "topic", RunConfiguration.BROKER_TOPIC, true, "Topic name. Overrides the parameter in configuration file." ));
		return options;
	}


	private static CommandLine parseCliArguments(String[] args) {
		Options options = createOptions();
		CommandLineParser parser = new DefaultParser();
	    try {
	        CommandLine cli = parser.parse( options, args );
	        if(cli.hasOption("help")) {
	        	printHelp(options);
	        	return null;
	        } else {
	        	return cli;
	        }
	    }
	    catch( ParseException exp ) {
	        System.err.println( exp.getMessage() );
	        printHelp(options);
	        return null;
	    }
	}

	private static List<String> getTestMessages(CommandLine cliArguments) {
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
			LOGGER.error("Exception occured while getting test messages", e);
			System.exit(1);
			return null;
		}
	}
	
	private static Properties loadProperties(String filepath) {
		Properties prop = new Properties();
		InputStream input = null;
		if(filepath == null) {
			return prop;
		}
		try {
			input = new FileInputStream(filepath);
			prop.load(input);
			return prop;
		} catch (IOException ex) {
			LOGGER.error("An expcetion occured while loading properties", ex);
			System.exit(1);
			return null;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					LOGGER.error("Exception occured while closing inputstream", e);
					System.exit(1);
				}
			}
		}

	}
	
	private static String getConfigParam(String paramName, String defaultValue, CommandLine cli, Properties properties) {
		if(cli.hasOption(paramName)) {
			return cli.getOptionValue(paramName);
		} else if(properties.containsKey(paramName)) {
			return properties.getProperty(paramName);
		} else {
			return defaultValue;
		}
	}
	
	private static void printRunConfiguration(RunConfiguration configuration) {
		LOGGER.info("=================== Hogarama Mock Cli Config =================");
		LOGGER.info("Host: " + configuration.getHost());
		LOGGER.info("User: " + configuration.getUser());
		LOGGER.info("Password: " + configuration.getPassword());
		LOGGER.info("Topic: " + configuration.getTopic());
		LOGGER.info("Delay ms: " + configuration.getDelayMs() + System.lineSeparator());
	}
	
	private static void printHelp(Options options) {
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp("java -jar hogarama-mock-cli.jar -c <arg> -t <arg>", options);
		System.exit(0);
	}
}
