#!/usr/bin/python

import paho.mqtt.client as paho
import time
import os
import json
import socket, ssl
import RPi.GPIO as GPIO
import Adafruit_GPIO.SPI as SPI
import Adafruit_MCP3008
import sys

from threading import Thread

def log(message):
    sys.stderr.write(message+"\n")

def on_publish(client, userdata, mid):
    log(("Publish returned result: {} {} {}".format(client, userdata, mid)))

def on_message(client, userdata, message):
    log(("Message received\r\n  Topic: {}\r\n  Payload: {}".format(message.topic, message.payload)))

    payload = json.loads(message.payload)

    actorFound = False
    for actor in actors:
        if actor.has_topic(message.topic):

            if actor.type == "gpio":
                # set actor in different thread to not block the main thread
                thread = Thread(target = actor.do_water, args =(payload['duration'],))
                thread.start()
            elif actor.type == "console":
                log("Turning console actor {} on for {}s".format(actor.name, payload['duration']))
            else:
                log("Actor type {} of {} is not supported".format(actor.type, actor.name))

            actorFound = True
            break
    if not actorFound:
        log("ERROR: No actor found for topic {}!".format(message.topic))

def reconnect(client):
    while True:
        log("Try connect again in a few seconds")
        time.sleep(5)
        try:
            client.reconnect()
            return True
        except Exception as ex:
            log("error on reconnect: " + str(ex))

def on_disconnect(client, userdata, rc):
    log(("Disconnect event occured: {} {} {}".format(client, userdata, rc)))
    reconnect(client)

    for actor in actors:
        actor.subscribe_to_topic(client)

def initialize_actors(actorConfigs):
    actors = []
    for config in actorConfigs:
        actors.append(Actor(config))
    return actors

class Actor:
    def __init__(self, config):
        self.name = config['name']
        self.type = config['type']
        self.topicName  = "actor.{}.{}".format (config['location'], config['name'])
        self.topicName2 = "actor/{}/{}".format (config['location'], config['name'])
        self.config = config
        self.active = False

    def has_topic(self, topicName):
        return topicName == self.topicName or topicName == self.topicName2

    def do_water(self, duration):
        pin = self.config['pin']
        if self.active:
            log("Actor {} is active. Ignore message!".format(self.name))
            return
        try:
            self.active = True
            log("Turning actor {} on for duration {}!".format(self.name, duration))
            GPIO.output(pin, 0)
            time.sleep(duration)
        finally:
            self.active = False
            GPIO.output(pin, 1)
            log("{} turned off".format(actor.name))

    def setup_pin(self):
        pin = self.config['pin']
        if actor.type == "gpio":
            GPIO.setup(pin, GPIO.OUT)
            GPIO.output(pin, 1)
        elif actor.type == "console":
            log("Setting up actor {} as console actor".format(actor.name))
        else:
            log("Actor type {} of {} is not supported!".format(actor.type, actor.name))

    def subscribe_to_topic(self, client):
        log("subscribe to {}".format (self.topicName))
        (result, mid) = client.subscribe(self.topicName, 0)


# Hardware SPI configuration:
SPI_PORT   = 0
SPI_DEVICE = 0
mcp = Adafruit_MCP3008.MCP3008(spi=SPI.SpiDev(SPI_PORT, SPI_DEVICE))

# Setup measuring
with open('habarama.json') as data_file:
    data = json.load(data_file)
brokerUrls = data['brokerUrls']
sensors = data['sensors']
actors = initialize_actors(data['actors'])

waitInterval = 15
sampleInterval = 2

# Setup pins
GPIO.setmode(GPIO.BCM)
for sensor in sensors:
    GPIO.setup(sensor['pin'], GPIO.OUT)

for actor in actors:
    actor.setup_pin()


# Setup Hogarama connection
clients = []
for index,brokerUrl in enumerate(brokerUrls):
    client = paho.Client(clean_session=True)
    client.on_publish = on_publish
    client.on_message = on_message
    client.on_disconnect = on_disconnect
    ssl_ctx = ssl.create_default_context(cafile='broker.pem')
    ssl_ctx.check_hostname = False
    client.tls_set_context(ssl_ctx)
    client.username_pw_set("mq_habarama", "mq_habarama_pass")

    client.connect(brokerUrl, 443, 60)

    for actor in actors:
        actor.subscribe_to_topic(client)

    clients.append(client)

# Main program loop.
while True:
    for client in clients:
        client.loop()

    try:
        for sensor in sensors:
            GPIO.output(sensor['pin'], 1)
            time.sleep(sampleInterval)
            waterLevel = mcp.read_adc(sensor['channel'])
            percent = int(round(waterLevel/10.24))
            log("ADC Sensor: '{2}' Output: {0:4d} Percentage: {1:3}%".format (waterLevel,percent,sensor['name']))
            payload = '{{"sensorName": "{}", "type": "{}", "value": {}, "location": "{}", "version": 1 }}'
            payload = payload.format(sensor['name'],sensor['type'],waterLevel,sensor['location'])
            for client in clients:

                client.publish("habarama", payload=payload, qos=0, retain=False)
            GPIO.output(sensor['pin'], 0)
    except Exception as e:
        log("ERROR: " +str(e))
        log("Oops! Something went terribly wrong, let us attempt exactly the same thing again!")
        time.sleep(10)
    time.sleep(waitInterval)
