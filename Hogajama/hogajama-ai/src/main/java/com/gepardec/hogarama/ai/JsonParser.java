package com.gepardec.hogarama.ai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class JsonParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonParser.class);

    private JsonParser() {
    }

    public static Map<String, Object> parseJsonToMap(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = null;
        try {
            jsonMap = objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            LOGGER.error("Error at parsing json to map: " + e.getMessage());
        }
        return jsonMap;
    }
}
