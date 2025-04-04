Supported sensors:

* [Moisture](Moisture)

If you didn't find the sensor you need, please feel free to contribute to our project. 👍

More sensors will be supported.

# Custom sensor

## Single pin input

All you need to connect a sensor with single pin digital or analog output is to define sensor connection in host
settings. Host settings are located in: `$project-repository/Habarama/ansible/inventory.yml`.

### Host settings example

```
"sensors": [
    {
      "name": "Plant1",
      "type": "water",
      "channel": 0,
      "pin": 21
    },
    {
      "name": "Plant1",
      "type": "light",
      "channel": 0,
      "pin": 22
    },
    {
      "name": "Plant2",
      "type": "water",
      "channel": 0,
      "pin": 20
    }
  ]
```

Supported syntax format is .yml and .ini

[More about host settings.](Raspberry)

## Complex input

Currently no complex input is supported. To read multi pin input you need to modify client.

# Adding new sensor types

Our concept is to do as much as possible on server side. Therefore we send raw data from the sensor to the cloud server
and save it as-is in the database. When we read the data we map it depending on the sensor type to a [0..1] interval. We
have different algorithms to map the sensor values. The most common are linear mappings from [0..1024] to [0..1] or
inverse from [1024..0] to 0..1]. In code these algorithms are represented by com.gepardec.hogarama.domain.MappingType
you can connect a sensor type to an mapping algorithm by editing com.gepardec.hogarama.domain.SensorType.  
