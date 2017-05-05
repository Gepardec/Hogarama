import paho.mqtt.client as paho
import time
import socket, ssl
import RPi.GPIO as GPIO
import Adafruit_GPIO.SPI as SPI
import Adafruit_MCP3008

def on_publish(client, userdata, mid):
    print("Publish returned result: {} {} {}".format(client, userdata, mid))

# Software SPI configuration:
CLK  = 18
MISO = 23
MOSI = 24
CS   = 25
mcp = Adafruit_MCP3008.MCP3008(clk=CLK, cs=CS, miso=MISO, mosi=MOSI)


# Setup pins
inPin = 22 # power for moisture sensor
sensorChannel = 5 # channel to listen on ADC  
GPIO.setmode(GPIO.BCM)
GPIO.setup(inPin, GPIO.OUT)

# Setup measuring
subjectName = "PflanzeWien"
waitInterval = 60*30
sampleInterval = 10

# Setup Hogarama connection
client = paho.Client(clean_session=True)
client.on_publish = on_publish
ssl_ctx = ssl.create_default_context(cafile='/home/pi/SensorScripts/client.pem')
ssl_ctx.check_hostname = False
client.tls_set_context(ssl_ctx)
client.username_pw_set("mq_habarama", "mq_habarama_pass")
client.connect("broker-amq-mqtt-ssl-57-hogarama.cloud.itandtel.at", 443, 60)
# client.disconnect()

# Main program loop.
while True:
   GPIO.output(inPin, 1)
   time.sleep(sampleInterval)
   watterLevel = mcp.read_adc(sensorChannel)
   print watterLevel
   payload = '{{"{}": {} }}'
   payload = payload.format(subjectName,watterLevel)
   client.publish("habarama", payload=payload, qos=0, retain=False)
   GPIO.output(inPin, 0)
   time.sleep(waitInterval)
