This type of actor turns a GPIO pin on and after a certain amount of time off. This duration gets specified by the
cloud.

## Configuration

The property `pin` is additional required.

    "actors": [
      {
        "name": "Pump 1",
        "location": "Wien",
        "type": "gpio",
        "pin": 4
      }
    ]

[More about client configuration.](https://github.com/Gepardec/Hogarama/wiki/Raspberry)

## Hardware

A sketch how to connect relais with a water pump to the raspberry:
![raspberry pi wireing](https://raw.githubusercontent.com/Gepardec/Hogarama/master/Habarama/raspberry/Sketch.png)