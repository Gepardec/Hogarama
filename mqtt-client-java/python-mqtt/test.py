import paho.mqtt.client as paho
import time
import socket, ssl

def on_connect(client, userdata, rc):
    global loop_flag
    print("Connection returned result: {}"+rc)
    loop_flag=0

def on_publish(client, userdata, mid):
    print("Publish returned result: {} {} {}".format(client, userdata, mid))

try:
    client = paho.Client(clean_session=True)
    client.on_publish = on_publish
    ssl_ctx = ssl.create_default_context(cafile='/Users/adrian/Documents/git/Hogarama/mqtt-client-java/python-mqtt/client.pem')
    ssl_ctx.check_hostname = False
    client.tls_set_context(ssl_ctx)
    client.username_pw_set("mq_habarama", "mq_habarama_pass")
    client.connect("broker-amq-mqtt-ssl-57-hogarama.cloud.itandtel.at", 443, 60)
    #client.loop_start()
    client.publish("habarama", payload="{\"pubmsg\": 45 }", qos=0, retain=False)
    client.disconnect()
    #client.loop_stop()
except Exception as e:
    print("Exception: {}".format(e))