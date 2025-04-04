# Running a minimal local Kafka with Docker

Note: This setup is following this guide: [Guide to Setting Up Apache Kafka Using Docker | Baeldung][1]

# Single Node

To run a local Kafka installation with docker, we need to start a Zookeeper server and also an Apache Kafka server.

## docker-compose.yml Configuration

The Zookeeper server needs to start first, so we configure this dependency in a docker-compose.yml file. This ensures
that the Zookeeper server starts before the Kafka server and also stops after it.

Create a docker-compose.yml file with two services (zookeeper and kafka)

```yml
version: '2'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:6.0.3
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    ports:
      - 22181:2181
  
  kafka:
    image: confluentinc/cp-kafka:6.0.3
    depends_on:
      - zookeeper
    ports:
      - 29092:29092
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092,PLAINTEXT_HOST://localhost:29092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
```

### Docker Image versions

For the Zookeeper and Kafka we use the image tags `6.0.3` due we use Kafka in version `2.6` and this tag of the
Confluent-Image is compatile with this Kafka-version.

For more information see: [Supported Versions and Interoperability | Confluent][3]

### Used Ports

**Zookeeper**

* Inside container: listening on port 2181 for the kafka service.
* Client running on the host: port 22181.

**Kafka**

* Inside container: port 9092
* Client running on the host: 29092

## Starting the Kafka server

```sh
docker-compose up -d
```

The Kafka server should now be available on Port 29092.

## Connecting to Kafka with KafDrop

To connect to the Kafka running in Docker, there is the possibility to use [**Kafdrop**][2], an UI Tool for Kafka.

1. Clone the repo
2. Build it with `mvn clean package`
3. Run it with the following command:

```
java -jar ./target/kafdrop-2.1.0.jar --zookeeper.connect=localhost:22181 --kafka.brokers=localhost:29092
```

Note: Kafdrop will show an error if there are no topics defined on the Kafka. (So maybe push some messages with the
Mock-Cli first)

[1]: https://www.baeldung.com/ops/kafka-docker-setup

[2]: https://github.com/csalmhof/Kafdrop

[3]: https://docs.confluent.io/platform/current/installation/versions-interoperability.html