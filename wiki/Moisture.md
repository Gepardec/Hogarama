This page describes how to connect a moisture sensor with analog data output. If you need a digital output you can
modify the setup easily.

A sketch how to connect moisture sensor with the raspberry:
![RasberryPi wireing](https://github.com/Gepardec/Hogarama/blob/master/Habarama/raspberry/Sketch.png?raw=true)

See also e.g. [RasberryPi 3 pin layout](https://image.tubefr.com/upload/b/93/b93af0f8a390fab1753d9d8ffb337ba3.jpg) for
information about the pin numbers.

Client configuration:

```
```

[More about client configuration.](Raspberry)

# About hardware

The moisture sensor consists of two parts, YL-69 (probe) and YL-39 (logic).

YL-39 has two pins on one side that is connected to YL-69 and four pins on the other side:

- VCC: 3.3V (connected to Raspi 3.3 VDC Power)
- GND (connected to Raspi Ground)
- A0 : analog output (connected to CH0 on MCP3008)
- D0 : digital output that goes LOW or HIGH only (can be optionally connected to MCP3008).

The [MCP3008 chip](https://learn.adafruit.com/assets/1222) is connected to Raspi using the following scheme:

- VDD: 3.3V
- VREF: 3.3V
- AGND: Ground
- CLK: Pin #23 (SCLK)
- DOUT: Pin #21 (MISO)
- DIN: Pin #19 (MOSI)
- CS/SHDN: Pin #24 (CE0)
- DGND: Ground

Once wired, see [pi4j module](https://github.com/Gepardec/Hogarama/tree/rasPDaddy/Habarama/pi4j) for the software
implementation.
