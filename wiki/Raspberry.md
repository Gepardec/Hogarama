Raspberry takes care of sending data to the Cloud. The connection has following parts:

* RaspberryPi with sensor: Reads data from the real world.
* Client: Transforms input from Raspberry pins to Hogajama accepted format.
* MQTT Broker: MQTT is used as a transport protocol between the Client and Hogajama.
* Hogajama: Stores data und displays it to the users.

# Hogajama message format

## Sensor data

This data is sent from the Raspberry Pi to the cloud.

`MQTT Topic`: habarama

```
{
  "sensorName": <sensorName>,
  "type": <sensorType>,
  "value": <value>,
  "location": <location>,
  "version": <version>
}
```

## Actor command

This command is sent from the cloud to the Raspberry Pi.

`MQTT Topic`: actor.&lt;ActorLocation&gt;.&lt;ActorName&gt;

    {
       "name": "<ActorName>",
       "location": "<ActorLocation>",
       "duration": duration in seconds
    }

# Client

Located in `$project-repository/Habarama/client/python/plant.py`.

## Requirements

* Enabled hardware SPI
* Python dependencies:
    * build-essential
    * python-pip
    * python-dev
    * python-smbus
* [Adafruit_Python_GPIO](https://github.com/adafruit/Adafruit_Python_GPIO.git)
* [Adafruit_Python_MCP3008](https://github.com/adafruit/Adafruit_Python_MCP3008.git)
* [eclipse/paho.mqtt.python](https://github.com/eclipse/paho.mqtt.python.git)

## Configuration

The client reads configuration form `habarama.json` located in current working directory.

### habarama.json format

```
{
  "brokerUrls": ["broker1", "broker2"],
  "sensors": [
    {
      "name": "<sensor.name>",
      "type": "<sensor.type>",
      "location": "<sensor.location>",
      "channel": <sensor.channel>,
      "pin": <sensor.pin>
    }
  ],
  "actors": [
    {
      "name": "<actor.name>",
      "location": "<actor.location>",
      "type": "<actor.type>",
      "pin": "<actor.pin>"
    }
  ]
}
``` 

* `brokerUrls`: Array of MQTT broker URLs.
* `sensors`: Array of sensors connected to Raspberry Pi.
* `sensors.name`: Unique name for sensor for given location and type. Type: String
* `sensors.type`: Describes which kind of values the sensor reads. Type: String
* `sensors.location`: Where is sensor located. Type: String
* `sensors.channel`: The channel on MCP3008 chip. Type: Number
* `sensors.pin`: To which pin is the sensor connected. Type: Number
* `actors`: Array of actors connected to Raspberry Pi.
* `actors.name`: Unique name for actor for given location. Type: String
* `actors.location`: Where the actor is located. Type:String
* `actors.type`: Describes the kind of actor. Currently possible values are supported: gpio | console. Type: String
* `actors.pin`: To which pin the actor is connected. This is only required in case `actors.type` is `gpio`. Type: Number

# MQTT Broker connection