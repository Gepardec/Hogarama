Raspberry PI Mocks GUI provides the graphical user interface to the Mock CLI tool, that imitates messages Rapberries
send to ActiveMQ using MQTT and allows testing without connecting sensors and real Raspberries.

## Build

## Prerequisites

* JDK 8
* Maven >=3.3.9

## Build

Clone Hogarama and build Habarama/raspberry-pi-mocks/raspberry-pi-mocks-gui with

```
mvn clean install
```

This will create an Wilfdly Swarm executable jar under
Habarama/raspberry-pi-mocks/raspberry-pi-mocks-gui/target/raspberry-pi-mocks-gui-swarm.jar

## Run

To start on a local machnine execute

```
java -jar raspberry-pi-mocks-gui-thorntail.jar
```

This application requires following environment variables:

* AMQ_HOST (e.g. http://broker-amq-mqtt:1883, https://localhost:8883 or another AMQ host url)
* KEYCLOAK_AUTH_SERVER_URL

Other optional environment variables:

* SWARM_PORT_OFFSET (Use this port offset if another server is already running on port 8080)
* AMQ_USER
* AMQ_PASSWDORD
* AMQ_TOPICS

# Configurable parameters in GUI

* Current value
* Sensor type
* Sensor name

After click on "Run" the application sends messages with given parameters to AMQ every 3 seconds. Click on "Stop" in
order to stop sending.
