package com.gepardec.hogarama.mocks.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class RestClient {

    public static final Logger LOGGER = LoggerFactory.getLogger(RestClient.class);

    public static void execute(RunConfiguration runConfiguration) throws InterruptedException {

        for (String valueToSend : runConfiguration.getMockMessages()) {
            try {
                sendPostRequest(runConfiguration.getDummyMessagingRestEndpointUrl(), valueToSend);
            } catch (Exception e) {
                LOGGER.error("Exception while sending {}. Execution will be interrupted", valueToSend, e);
                throw new RuntimeException(e);
            }
            TimeUnit.MILLISECONDS.sleep(runConfiguration.getDelayMs());
        }

        LOGGER.info(System.lineSeparator() + "=================== Hogarama Mock Cli REST Client Finished =================");
    }

    private static void sendPostRequest(String url, String payload) throws Exception {
        LOGGER.info("Send " + payload + " on " + url);
        URL object = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) object.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");

        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(payload);
        outputStream.flush();
        outputStream.close();

        int responseCode = connection.getResponseCode();
        LOGGER.info("Response Code : " + responseCode);

        String response;
        if (responseCode != 200) {
            response = getResponseString(connection.getErrorStream());
        } else {
            response = getResponseString(connection.getInputStream());
        }
        LOGGER.info("Response Body : " + response + System.lineSeparator());
    }

    private static String getResponseString(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        }
    }
}
