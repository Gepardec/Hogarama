import paho.mqtt.client as paho
import time
import os
import json
import socket, ssl
import RPi.GPIO as GPIO
import Adafruit_GPIO.SPI as SPI
import Adafruit_MCP3008

from threading import Thread

def on_publish(client, userdata, mid):
    print("Publish returned result: {} {} {}".format(client, userdata, mid))
    
def on_message(client, userdata, message):
    print("Message received\r\nTopic: {}\r\nPayload: {}".format(message.topic, message.payload))
    
    payload = json.loads(message.payload)
    
    # Payload:
    #
    # {
    #   "name": "MathiasJ",
    #   "type": "pump",
    #   "location": "vorarlberg",
    #   "duration": 5
    # }
    
    for actor in actors:
        if message.topic == "{0}.{1}.{2}".format (actor['type'], actor['location'], actor['name']):
            
            # set actor in different thread to not block the main thread
            thread = Thread(target = set_actor, args =(actor, payload['duration']))
            thread.start()  
            break
    
def set_actor(actor, duration):
    try:
        print "Turning {} on".format(actor['name'])
        GPIO.output(actor['pin'], 0)
        time.sleep(duration)
    finally:
        GPIO.output(actor['pin'], 1)
        print "{} turned off".format(actor['name'])
    


# Hardware SPI configuration:
SPI_PORT   = 0
SPI_DEVICE = 0
mcp = Adafruit_MCP3008.MCP3008(spi=SPI.SpiDev(SPI_PORT, SPI_DEVICE))

# Setup measuring
with open('habarama.json') as data_file:    
    data = json.load(data_file)
brokerUrls = data['brokerUrls']
sensors = data['sensors']
actors = data['actors']
waitInterval = 0
sampleInterval = 3

# Setup pins
GPIO.setmode(GPIO.BCM)
for sensor in sensors:
    GPIO.setup(sensor['pin'], GPIO.OUT)
    
for actor in actors:
    GPIO.setup(actor['pin'], GPIO.OUT)
    GPIO.output(actor['pin'], 1)

# Setup Hogarama connection
clients = []
for index,brokerUrl in enumerate(brokerUrls):
    client = paho.Client(clean_session=True)
    client.on_publish = on_publish
    client.on_message = on_message
    ssl_ctx = ssl.create_default_context(cafile='broker.pem')
    ssl_ctx.check_hostname = False
    client.tls_set_context(ssl_ctx)
    client.username_pw_set("mq_habarama", "mq_habarama_pass")
    
    client.connect(brokerUrl, 443, 60)
    
    for actor in actors:
        topicName = "{0}.{1}.{2}".format (actor['type'], actor['location'], actor['name'])
        print "{0} subscribe to {1}".format (brokerUrl, topicName)
        (result, mid) = client.subscribe(topicName, 0)
    
    clients.append(client)


# Main program loop.
while True:
    for client in clients:
        client.loop()
    
    try:
        for sensor in sensors:
            GPIO.output(sensor['pin'], 1)
            time.sleep(sampleInterval)
            watterLevel = mcp.read_adc(sensor['channel'])
            percent = 100 - int(round(watterLevel/10.24))
            print "ADC Output: {0:4d} Percentage: {1:3}%".format (watterLevel,percent)
            payload = '{{"sensorName": "{}", "type": "{}", "value": {}, "location": "{}", "version": 1 }}'
            payload = payload.format(sensor['name'],sensor['type'],percent,sensor['location'])
            for client in clients:
                client.publish("habarama", payload=payload, qos=0, retain=False)
            GPIO.output(sensor['pin'], 0)
    except:
        print "Oops! Something wrong. Trying luck in next iteration."
    time.sleep(waitInterval)
