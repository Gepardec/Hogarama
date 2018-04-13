package com.gepardec.hogarama.mocks.cli;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.fusesource.hawtdispatch.transport.SslProtocolCodec.ClientAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockCli {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(MockCli.class);
	
	public static void main(String[] args) {
		
		CommandLine cliArguments = parseCliArguments(args);
		List<String> testMessages = getTestMessages(cliArguments);
		Properties properties = loadProperties(cliArguments.getOptionValue(Configuration.PATH_TO_CONFIG));
		long delayMs = Long.parseLong(getConfigParam(Configuration.DELAY_MS, "3000", cliArguments, properties));
		String host = getConfigParam(Configuration.BROKER_HOST, "https://broker-amq-mqtt-ssl-hogarama.10.0.75.2.nip.io", cliArguments, properties);
		String user = getConfigParam(Configuration.BROKER_USERNAME, "mq_habarama", cliArguments, properties);
		String password = getConfigParam(Configuration.BROKER_PASSWORD, "mq_habarama_pass", cliArguments, properties);
		String topic = getConfigParam(Configuration.BROKER_TOPIC, "habarama", cliArguments, properties);
		
		printConfig(host, user, password, topic, delayMs);
		
		MqttClient mqttClient = new MqttClient().
				withHost(host).
				withUser(user).
				withPassword(password).
				withTopic(topic).
				build();
		
		mqttClient.connectAndPublish(testMessages, delayMs);
		
		
		LOGGER.info(System.lineSeparator() + "=================== Hogarama Mock Cli Finished =================");
	}
	

	


	private static Options createOptions() {
		Options options = new Options();
		options.addOption(new Option( "h", "help", false, "print this message" ));
		options.addRequiredOption("t", Configuration.PATH_TO_TEST_DATA, true, "Absolute path to file with test data");
		options.addOption("c", Configuration.PATH_TO_CONFIG, true, "Absolute path to configuration file. Shoud contains key, value pairs in form key=value");
		options.addOption(new Option( "d", Configuration.DELAY_MS, true, "Delay between publishings in ms. It overrides the parameter delay in configuration file. Default value is 3000." ));
		options.addOption(new Option( "host", Configuration.BROKER_HOST, true, "Url to AMQ Broker. Overrides the parameter in configuration file." ));
		options.addOption(new Option( "u", Configuration.BROKER_USERNAME, true, "Username. Overrides the parameter in configuration file." ));
		options.addOption(new Option( "p", Configuration.BROKER_PASSWORD, true, "Password. Overrides the parameter in configuration file." ));
		options.addOption(new Option( "topic", Configuration.BROKER_TOPIC, true, "Topic name. Overrides the parameter in configuration file." ));
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
			String pathToTestData = cliArguments.getOptionValue(Configuration.PATH_TO_TEST_DATA);
			String content = new String(Files.readAllBytes(Paths.get(pathToTestData)));
			List<String> messages = new ArrayList<>();
			Matcher m = Pattern.compile("\\{[^\\{\\}]*\\}").matcher(content);
			while (m.find()) {
				messages.add(m.group());
			}
			return messages;
			
		} catch (IOException e) {
			e.printStackTrace();
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
			ex.printStackTrace();
			System.exit(1);
			return null;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
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
	
	private static void printConfig(String host, String user, String password, String topic, long delayMs) {
		LOGGER.info("=================== Hogarama Mock Cli Config =================");
		LOGGER.info("Host: " + host);
		LOGGER.info("User: " + user);
		LOGGER.info("Password: " + password);
		LOGGER.info("Topic: " + topic);
		LOGGER.info("Delay ms: " + delayMs + System.lineSeparator());
	}
	
	private static void printHelp(Options options) {
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp("java -jar hogarama-mock-cli.jar -c <arg> -t <arg>", options);
		System.exit(0);
	}
}
