package com.gepardec.hogarama.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gepardec.hogarama.domain.sensor.SensorData;
import com.gepardec.hogarama.domain.sensor.SensorNormalizer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

@ApplicationScoped
public class SensorSmootherKStreams {

  private static final String propFileName = "kafka.properties";

  private static final Logger log = LoggerFactory.getLogger(SensorSmootherKStreams.class);

  private HashMap<String, Double> sensorDataCache = new HashMap<>();

  private KafkaStreams streams;

  @Inject
  private SensorNormalizer sensorNormalizer;

  private Properties kafkaProperties = new Properties();

  @PostConstruct
  public void startKstream() throws IOException {
    loadKafkaProperties();

    Properties streamConfiguration = new Properties(kafkaProperties);
    streamConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

    streams = new KafkaStreams(createStreamTopology(sensorNormalizer, sensorDataCache, kafkaProperties), streamConfiguration);
    streams.start();
  }

  static Topology createStreamTopology(SensorNormalizer localSensorNormalizer, HashMap<String, Double> localSensorDataCache, Properties properties) {
    StreamsBuilder streamsBuilder = new StreamsBuilder();

    KStream<Long, String> rawSensorValues = streamsBuilder.stream(properties.getProperty("sensor.rawtopic"));

    KStream<Long, String> smoothSensorValues = rawSensorValues.mapValues(rawValue -> transformValue(localSensorNormalizer, localSensorDataCache, rawValue));

    smoothSensorValues.to(properties.getProperty("sensor.smoothtopic"));

    return streamsBuilder.build();
  }

  private void loadKafkaProperties() throws IOException {
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

    if (inputStream != null) {
      kafkaProperties.load(inputStream);
    } else {
      throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
    }
  }

  @PreDestroy
  public void tearDown() {
    if(streams != null) {
      streams.close();
    }
  }

  static String transformValue(SensorNormalizer localSensorNormalizer, HashMap<String, Double> localSensorDataCache, String rawValue) {
    try {

      ObjectMapper mapper = new ObjectMapper();
      SensorData sensorData = localSensorNormalizer.normalize(mapper.readValue(rawValue, SensorData.class));
      String sensorName = sensorData.getSensorName();
      double sensorValue = sensorData.getValue();

      localSensorDataCache.putIfAbsent(sensorName, sensorValue);

      double avgSensorValue = computeAverageAndUpdateCache(localSensorDataCache, sensorName, sensorValue);
      sensorData.setValue(avgSensorValue);

      return mapper.writeValueAsString(sensorData);

    } catch (IOException e) {
      throw new RuntimeException("Error handling sensor data!", e);
    }
  }

  static double computeAverageAndUpdateCache(HashMap<String, Double> localSensorDataCache, String sensorName, double newValue) {
    Double cachedValue = localSensorDataCache.get(sensorName);

    double avgValue = computeAverage(newValue, cachedValue);

    updateSensorCache(localSensorDataCache, sensorName, avgValue);

    return avgValue;
  }

  static double computeAverage(double newValue, Double cachedValue) {
    if (null == cachedValue) {
      cachedValue = newValue;
    }

    return Math.round(((cachedValue * 2 + newValue) / 3) * 10000d) /10000d;
  }

  static void updateSensorCache(HashMap<String, Double> localSensorDataCache, String sensorName, double avgValue) {
    localSensorDataCache.put(sensorName, avgValue);
  }
}
