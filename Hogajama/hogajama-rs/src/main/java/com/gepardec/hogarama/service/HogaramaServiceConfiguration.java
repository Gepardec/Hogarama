package com.gepardec.hogarama.service;

public class HogaramaServiceConfiguration {

    public boolean useKafkaWatering() {
        return "kafka".equalsIgnoreCase(System.getenv("HOGAJAMA_WATERING_SOURCE"));
    }
    
    public boolean useAMQWatering() {
        return !useKafkaWatering();
    }

}
