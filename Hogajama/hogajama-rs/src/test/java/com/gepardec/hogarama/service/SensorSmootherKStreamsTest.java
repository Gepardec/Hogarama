package com.gepardec.hogarama.service;

import com.gepardec.hogarama.domain.sensor.SensorData;
import com.gepardec.hogarama.domain.sensor.SensorNormalizer;
import com.gepardec.hogarama.domain.unitmanagement.cache.SensorCache;
import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import com.gepardec.hogarama.domain.unitmanagement.entity.SensorType;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SensorSmootherKStreamsTest {

  private static final String INPUT_TOPIC = "input";
  private static final String OUTPUT_TOPIC = "output";

  private TopologyTestDriver td;
  private TestInputTopic<String, String> inputTopic;
  private TestOutputTopic<String, String> outputTopic;

  private Topology topology;
  private final Properties config;

  @InjectMocks
  private SensorNormalizer sensorNormalizer;

  @Mock
  private SensorCache sensorDao;


  public SensorSmootherKStreamsTest() {

    config = new Properties();
    config.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "test");
    config.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "foo:1234");
    config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    config.put("sensor.rawtopic", INPUT_TOPIC);
    config.put("sensor.smoothtopic", OUTPUT_TOPIC);
  }

  @Before
  public void initMockSensorNormalizer() {


    Sensor sensor = new Sensor();
    sensor.setSensorType(new SensorType());
    Optional<Sensor> optSensor = Optional.of(sensor);

    Mockito.when(sensorDao.getByDeviceId(Mockito.any())).thenReturn(optSensor);
  }

  @After
  public void tearDown() {
    td.close();
  }

  @Test
  public void test() throws IOException {

    HashMap<String, Double> testCache = new HashMap<>();

    topology = SensorSmootherKStreams.createStreamTopology(sensorNormalizer, testCache, config);
    td = new TopologyTestDriver(topology, config);

    inputTopic = td.createInputTopic(INPUT_TOPIC, Serdes.String().serializer(), Serdes.String().serializer());
    outputTopic = td.createOutputTopic(OUTPUT_TOPIC, Serdes.String().deserializer(), Serdes.String().deserializer());

    assertThat(outputTopic.isEmpty(), is(true));

    inputTopic.pipeInput(testValues(20.0));
    assertEquals(outputTopic.readValue(), "{\"id\":\"test1\",\"time\":1577840461000,\"sensorName\":\"test1\",\"type\":\"IDENTITY\",\"value\":0.2,\"location\":\"Wien\",\"version\":\"1\"}");
    inputTopic.pipeInput(testValues(50.0));
    assertEquals(outputTopic.readValue(), "{\"id\":\"test1\",\"time\":1577840461000,\"sensorName\":\"test1\",\"type\":\"IDENTITY\",\"value\":0.3,\"location\":\"Wien\",\"version\":\"1\"}");
    inputTopic.pipeInput(testValues(60.0));
    assertEquals(outputTopic.isEmpty(), false);
    assertEquals(outputTopic.readValue(), "{\"id\":\"test1\",\"time\":1577840461000,\"sensorName\":\"test1\",\"type\":\"IDENTITY\",\"value\":0.4,\"location\":\"Wien\",\"version\":\"1\"}");
    assertEquals(outputTopic.isEmpty(), true);
  }

  private String testValues(Double value) throws IOException {
    SensorData test1 = getTestSensorDataValues(value);

    return new ObjectMapper().writeValueAsString(test1);
  }

  private SensorData getTestSensorDataValues(Double value) {
    return new SensorData("test1", Date.from(LocalDateTime.of(2020,1,1,1,1,1,1).toInstant(ZoneOffset.UTC)), "test1", "IDENTITY", value, "Wien", "1");
  }


}
