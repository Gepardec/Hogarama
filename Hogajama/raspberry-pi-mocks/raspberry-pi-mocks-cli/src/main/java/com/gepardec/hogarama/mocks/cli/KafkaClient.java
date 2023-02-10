package com.gepardec.hogarama.mocks.cli;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class KafkaClient {

  public static final Logger LOGGER = LoggerFactory.getLogger(KafkaClient.class);

  public static void execute(RunConfiguration runConfiguration) throws InterruptedException {

    Producer<Long, String> customerProducer = createKafkaProducer(runConfiguration);

    for(String valueToSend : runConfiguration.getMockMessages()) {
      sendToKafka(customerProducer, valueToSend, runConfiguration.getTopic());
      TimeUnit.MILLISECONDS.sleep(runConfiguration.getDelayMs());
    }

    LOGGER.info(System.lineSeparator() + "=================== Hogarama Mock Cli Kafka Finished =================");
  }

  private static Producer<Long, String> createKafkaProducer(RunConfiguration runConfiguration) {
    Properties props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, runConfiguration.getHost());
    props.put(ProducerConfig.ACKS_CONFIG, "all");
    props.put(ProducerConfig.RETRIES_CONFIG, 0);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    
    props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, runConfiguration.getSecurityProtocol());
    props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, runConfiguration.getSslTruststoreLocation());
    props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, runConfiguration.getSslTruststorePassword());

    return new KafkaProducer<>(props);
  }

  private static boolean sendToKafka(Producer<Long, String> producer, String valueToSend, String topic) {
    boolean sendingSuccess = true;

    ProducerRecord<Long, String> record = new ProducerRecord<>(topic, valueToSend);

    try {
      RecordMetadata metadata = producer.send(record).get();
      System.out.println("Record \"" + valueToSend + "\" sent to partition " + metadata.partition()
          + " with offset " + metadata.offset());
    } catch (InterruptedException | ExecutionException e) {
      System.out.println("Error in sending record");
      e.printStackTrace();
      sendingSuccess = false;
    }
    return sendingSuccess;
  }
}
