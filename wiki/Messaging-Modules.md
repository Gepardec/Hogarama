Hogajama uses messaging to recieve sensor data from (Habarama-) clients. This page has developer focus, like
configuration and testing in a local environment.

# messaging-mdb

This modules uses an MDB to read sensor data from an ActiveMQ queue.

## Compile

`(cd Hogajama && mvn clean install -P security-dummy,messaging-mdb)`

## Local Testing

If you have access to a Kubernetes/OpenShift cluster, the easiest way to obtain an ActiveMQ installation is to install
the resources from the helm chart and forward the port to the local environment:

```
oc new-project hogarama-pg
cd OCP-Infrastructure/helm/amq
./install.sh | oc apply -f -

POD=$(oc get pods -l app=amq  --no-headers=true | grep Running | cut -d" " -f1)
oc port-forward $POD 61616 &
oc port-forward $POD 8883 &
cd ../../..
```

Install Wildfly and deploy Hogajama as described
in [Local JBoss Development -> Setup Guide](Local-JBoss-Development#setup-guide).
Then you can start the test-script that sends data to ActiveMQ:

```
./Hogajama/raspberry-pi-mocks/raspberry-pi-mocks-cli/mock-cli-amq.sh 
```

Observe in the Wildfly logfile that sensor-data is recieved:

```
Receive sensorData: SensorData [id=null, time=null, sensorName=Pflanze, type=wasser, value=5.0, location=Wien, version=0]
```

# messaging-kafka

This module uses Kafka to read sensor data.

## Compile

`(cd Hogajama && mvn clean install -P security-dummy,messaging-kafka)`

## Local Testing

If you have access to a Kafka installation, you can share it with different Hogajama instances by using a prefix for the
topics.
Set

```
export KAFKA_TOPIC_PREFIX="mytest"
```

in your environment file (e.g. `hogajama.env`).

Install Wildfly and deploy Hogajama as described
in [Local JBoss Development -> Setup Guide](Local-JBoss-Development#setup-guide).

Look at `./Hogajama/raspberry-pi-mocks/raspberry-pi-mocks-cli/examples/kafkaTestConfigWithCluster.prop` and set
`brokerTopic=mytesthabarama`
together with the other properties.

Then copy `kafkaTestConfigWithCluster.prop` to
`./Hogajama/raspberry-pi-mocks/raspberry-pi-mocks-cli/examples/kafkaTestConfig.prop` and execute

```
./Hogajama/raspberry-pi-mocks/raspberry-pi-mocks-cli/mock-cli-kafka.sh
```

Observe in the Wildfly logfile that sensor-data is recieved:

```
Receive sensorData: SensorData [id=null, time=null, sensorName=Pflanze, type=wasser, value=5.0, location=Wien, version=0]
```

# messaging-rest

This module uses a REST service to read sensor data. It is meant for testing only.

## Compile

`(cd Hogajama && mvn clean install -P security-dummy,messaging-rest)`

## Local Testing

Install Wildfly and deploy Hogajama as described
in [Local JBoss Development -> Setup Guide](Local-JBoss-Development#setup-guide).

```
./Hogajama/raspberry-pi-mocks/raspberry-pi-mocks-cli/mock-cli-rest.sh
```

Observe in the Wildfly logfile that sensor-data is recieved:

```
Receive sensorData: SensorData [id=null, time=null, sensorName=Pflanze, type=wasser, value=5.0, location=Wien, version=0]
```