import paho.mqtt.client as paho
import time
import os
import json
import socket, ssl
import random

def on_publish(client, userdata, mid):
    print("Publish returned result: {} {} {}".format(client, userdata, mid))

# Setup measuring
brokerUrls = ['hogajama-broker-amq-mqtt-ssl-myproject.127.0.0.1.nip.io']
waitInterval = 5
sampleInterval = 3

# Setup Hogarama connection
client = paho.Client(clean_session=True)
client.on_publish = on_publish
ssl_ctx = ssl.create_default_context(cafile='broker.pem')
ssl_ctx.check_hostname = False
client.tls_set_context(ssl_ctx)
client.username_pw_set("mq_habarama", "mq_habarama_pass")

# Main program loop.
while True:
    for brokerUrl in brokerUrls: 
        try:
            client.connect(brokerUrl, 443, 60)
            watterLevel = 20
            percent = random.randrange(0, 101, 2)
            print "ADC Output: {0:4d} Percentage: {1:3}%".format (watterLevel,percent)
            payload = '{{"sensorName": "{}", "type": "{}", "value": {}, "location": "{}", "version": 1 }}'
            payload = payload.format('dummy','wasser',percent,'mac')
            client.publish("habarama", payload=payload, qos=0, retain=False)
            client.disconnect()
        except:
            print "Oops! Something wrong. Trying luck in next iteration."
    time.sleep(waitInterval)
