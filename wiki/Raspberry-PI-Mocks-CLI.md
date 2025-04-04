Raspberry PI Mocks CLI imitates messages, that Raspberries send to ActiveMQ using MQTT, directly to the Kafka Cluster or
via HTTP to a REST-Endpoint (bypassing amq and kafka) and allows testing without connecting sensors and real
Raspberries.

## Prerequisites

* JDK 8
* Maven >=3.3.9

## Build

Clone Hogarama and build Habarama/raspberry-pi-mocks/raspberry-pi-mocks-cli with

```
mvn clean install
```

This will create an executable jar under Habarama/raspberry-pi-mocks/raspberry-pi-mocks-cli/target/hogarama-mock-cli.jar

## Usage

```
java -jar hogarama-mock-cli.jar [params]
```

Parameters:

* `-c,--configuration <arg>` **required**: Absolute path to configuration file. The file shoud contain key-value pairs
  in form _key=value_.
* `-t,--testData <arg>` **required**: Absolute path to file with test messages.
* `-d,--delayMs <arg>`: Delay between publishings of single messages in ms. It overrides the parameter _delayMs_ in
  configuration file. Default value is 3000.
* `-host,--brokerHost <arg>`: Url to AMQ broker. Overrides the parameter _brokerHost_ in configuration file.
* `-u,--brokerUsername <arg>`: AMQ username. Overrides the parameter _brokerUsername_ in configuration file.
* `-p,--brokerPassword <arg>`: AMQ password. Overrides the parameter _brokerPassword_ in configuration file.
* `-topic,--brokerTopic <arg>`: AMQ topic name. Overrides the parameter _brokerTopic_ in configuration file.
* `-h,--help`: Print help message.

Example call for AMQ:

```
java -jar hogarama-mock-cli.jar -t /tmp/testData.json -c /tmp/testConfig.prop --delayMs 5000
```

Example call for Kafka:

```
java -jar hogarama-mock-cli.jar -b kafka -t /tmp/testData.json -c /tmp/kafkaTestConfig.prop --delayMs 5000
```

Example call for REST:

```
java -jar hogarama-mock-cli.jar -b rest -t /tmp/testData_small.json -c /tmp/restTestConfig.prop --delayMs 5000
```

Example content of data file:

```

[{
    "sensorName" : "Pflanze",
    "type" : "wasser",
    "value" : 55.3,
    "location" : "Wien",
    "version" : 0
},
{
    "sensorName" : "Pflanze",
    "type" : "wasser",
    "value" : 65.3,
    "location" : "Wien",
    "version" : 0
},
{
    "sensorName" : "Pflanze",
    "type" : "wasser",
    "value" : 75.3,
    "location" : "Wien",
    "version" : 0
}]
```

Example content of configuration file for AMQ:

```
brokerHost=https://broker-amq-mqtt-ssl-hogarama.10.0.75.2.nip.io
brokerUser=mq_habarama
brokerPassword=mq_habarama_pass
brokerTopic=habarama
```

Example content of configuration file for Kafka:

```
brokerHost=https://localhost:9092
brokerUser=mq_habarama
brokerPassword=mq_habarama_pass
brokerTopic=habarama
```

Example content of configuration file for REST:

```
dummyMessagingRestEndpointUrl=http://localhost:8080/hogajama-rs/rest/messaging/sensorData
```