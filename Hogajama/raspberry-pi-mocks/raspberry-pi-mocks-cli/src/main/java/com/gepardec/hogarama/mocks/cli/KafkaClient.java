package com.gepardec.hogarama.mocks.cli;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

public class KafkaClient {

  public static final Logger LOGGER = LoggerFactory.getLogger(MockCli.class);

  public static void execute(RunConfiguration runConfiguration) throws InterruptedException {

    Producer<Long, String> customerProducer = createKafkaProducer(runConfiguration);

    for(String valueToSend : runConfiguration.getMockMessages()) {
      sendToKafka(customerProducer, valueToSend, runConfiguration.getTopic());
      TimeUnit.MILLISECONDS.sleep(runConfiguration.getDelayMs());
    }

    LOGGER.info(System.lineSeparator() + "=================== Hogarama Mock Cli Kafka Finished =================");
  }

  private static Producer<Long, String> createKafkaProducer(RunConfiguration RunConfiguration) {
    Properties props = new Properties();
    props.put(BOOTSTRAP_SERVERS_CONFIG, RunConfiguration.getHost());
    props.put(ACKS_CONFIG, "all");
    props.put(RETRIES_CONFIG, 0);
    props.put(KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
    props.put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

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
