package com.gepardec.hogarama.mocks.cli;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MockCli {

    public static final Logger LOGGER = LoggerFactory.getLogger(MockCli.class);

    public static void main(String[] args) throws InterruptedException {

        RunConfiguration runConfiguration = createRunConfigurationFromArguments(args);
        runConfiguration.print();

        if (runConfiguration.getBroker().equals("kafka")) {
            KafkaClient.execute(runConfiguration);
        } else if (runConfiguration.getBroker().equals("amq")) {
            MqttClient.execute(runConfiguration);
        } else if (runConfiguration.getBroker().equals("rest")) {
            RestClient.execute(runConfiguration);
        } else {
            LOGGER.error("Broker {} not supported", runConfiguration.getBroker());
        }

        LOGGER.info(System.lineSeparator() + "=================== Hogarama Mock Cli Finished =================");
    }

    protected static RunConfiguration createRunConfigurationFromArguments(String[] args) {

        CommandLine cliArguments = parseCliArguments(args);
        Properties properties = loadProperties(cliArguments.getOptionValue(RunConfiguration.PATH_TO_CONFIG));
        RunConfiguration configuration = new RunConfiguration(cliArguments, properties);

        return configuration;
    }

    private static Options createOptions() {

        Options options = new Options();
        options.addOption(new Option("h", "help", false, "print this message"));
        options.addRequiredOption("t", RunConfiguration.PATH_TO_TEST_DATA, true,
                "Absolute path to file with test data");
        options.addOption("c", RunConfiguration.PATH_TO_CONFIG, true,
                "Absolute path to configuration file. Shoud contains key, value pairs in form key=value");
        options.addOption(new Option("d", RunConfiguration.DELAY_MS, true,
                "Delay between publishings in ms. It overrides the parameter delay in configuration file. Default value is 3000."));
        options.addOption(new Option("host", RunConfiguration.BROKER_HOST, true,
                "Url to AMQ Broker. Overrides the parameter in configuration file."));
        options.addOption(new Option("u", RunConfiguration.BROKER_USERNAME, true,
                "Username. Overrides the parameter in configuration file."));
        options.addOption(new Option("p", RunConfiguration.BROKER_PASSWORD, true,
                "Password. Overrides the parameter in configuration file."));
        options.addOption(new Option("topic", RunConfiguration.BROKER_TOPIC, true,
                "Topic name. Overrides the parameter in configuration file."));
        options.addOption(new Option("b", RunConfiguration.BROKER, true,
                "Broker to use: amq (default), kafka or rest"));
        return options;
    }

    private static CommandLine parseCliArguments(String[] args) {

        Options options = createOptions();
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cli = parser.parse(options, args);
            if (cli.hasOption("help")) {
                printHelp(options);
                return null;
            } else {
                return cli;
            }
        } catch (ParseException exp) {
            LOGGER.error(exp.getMessage());
            printHelp(options);
            return null;
        }
    }

    private static Properties loadProperties(String filepath) {

        Properties prop = new Properties();
        InputStream input = null;
        if (filepath == null) {
            return prop;
        }
        try {
            input = new FileInputStream(filepath);
            prop.load(input);
            return prop;
        } catch (IOException ex) {
            LOGGER.error("An exception occured while loading properties", ex);
            System.exit(1);
            return new Properties();
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

    private static void printHelp(Options options) {

        HelpFormatter formater = new HelpFormatter();
        formater.printHelp("java -jar hogarama-mock-cli.jar -c <arg> -t <arg>", options);
        System.exit(0);
    }
}
