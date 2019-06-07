// TODOS:
//    * send real sensor values
//    * implement actor logic


#include <ESP8266LLMNR.h>

#include <ESP8266WiFi.h>          //ESP8266 Core WiFi Library

#include <DNSServer.h>            //Local DNS Server used for redirecting all requests to the configuration portal
#include <ESP8266WebServer.h>     //Local WebServer used to serve the configuration portal
#include <WiFiManager.h>          //WiFi Configuration Magic - https://github.com/tzapu/WiFiManager

#include <PubSubClient.h>         // Client to connect to cloud via mqtt - https://github.com/plapointe6/EspMQTTClient
#include <ArduinoJson.h>          // Library to parse Json. Used for payload of mqtt ATTENTION: Version 5 is required at the moment. Support for v6 in the Future
#include <stdio.h>

// configuration for sensor
#define sensor_name "GruenerArduino"
#define sensor_type "sparkfun"
#define sensor_location "Simba"

// configuration for mqtt server
#define mqtt_server "broker-amq-mqtt-ssl-57-hogarama.cloud.itandtel.at"
#define mqtt_server_port 443
#define mqtt_user "mq_habarama"
#define mqtt_password "mq_habarama_pass"

WiFiClientSecure espClient;
PubSubClient client(espClient);

typedef struct {
  byte pin; // the pin where the actor is connected to
  long ticks_until; // the ticks until the pin should be triggered. Should be initialized with 0.
  String topic; // the topic this actor should listen to
} actor_type;

// configuration for actor.
actor_type actors[] = {
   {D0, 0, "actor.Wien.GruenerGepard"},
   {D1, 0, "actor.Wien.GruenerArduino"}
};

long lastSensorDataSent = 0;
long lastReconnect = 0;

void setup() {
  Serial.begin(115200);

  initializeWifi();

  //initializeActors();

  initializeMqtt();
}


void initializeWifi() {
  //WiFiManager wifiManager;
  Serial.println("WiFi Manager intiliazing");
  WiFi.begin("Gepardec Gast", "gast@gepard");
  while(WiFi.status() != WL_CONNECTED){
    delay(500);
    Serial.println("working on wifi connection");
  }
  // wifiManager.resetSettings(); // This can be used to rest wifi settings.
  // wifiManager.autoConnect("Gepardec", "ansoMenOrkSmuelUE");
  Serial.println("WiFi Manager DONE");
}

void initializeActors() {
  Serial.println("ACTORS intiliazing");
  for (byte i = 0; i < (sizeof(actors) / sizeof(*actors)); i++) {
    actors[i].ticks_until = 0;
    pinMode(actors[i].pin, OUTPUT);
    digitalWrite(actors[i].pin, LOW);
  }
  Serial.println("ACTORS DONE");
}

void initializeMqtt() {
  Serial.println("MQTT intiliazing");
  espClient.allowSelfSignedCerts();
  client.setServer(mqtt_server, mqtt_server_port);
  client.setCallback(mqtt_callback);
  Serial.println("MQTT DONE");
}

void mqtt_callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("mqtt topic: ");
  Serial.println(topic);

  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& root = jsonBuffer.parseObject(payload);

  for (byte i = 0; i < (sizeof(actors) / sizeof(*actors)); i++) {

    // Check if topic matches to actor
    if (strcmp(topic, actors[i].topic.c_str()) == 0) {
      Serial.println("actor found");

      if (!root.success()) {
        Serial.println("Could not parse payload from mqtt");
        return;
      }

      const char* type = root["type"];
      long duration = root["duration"];

      if (strcmp(type, "gpio") == 0) {
        setGpio(&actors[i], duration);
      } else {
        Serial.println("Unknown actor type");
        return;
      }
    }
  }
}

void setGpio(actor_type *actor, long duration) {
  Serial.print("Setting actor to hight: ");
  Serial.println(actor->topic);

  actor->ticks_until = millis() + duration;

  digitalWrite(actor->pin, HIGH);
}

void reconnect() {
  // reconnect when not connected and 5s passed since last try.
  if (!client.connected() && (lastReconnect == 0 || (millis() - lastReconnect) > 5000)) {
    Serial.print("Attempting MQTT connection...");

    // Attempt to connect
    if (client.connect("ESP8266Client", mqtt_user, mqtt_password)) {
      Serial.println("connected");

      subscribeToMqtt();
    } else {
      Serial.print("failed, rc=");
      Serial.println(client.state());
    }
  }
}

void subscribeToMqtt() {

  // subscribe all actors to mqtt
  for (byte i = 0; i < (sizeof(actors) / sizeof(*actors)); i++) {
    if (actors[i].topic.length()) {
      if (client.subscribe(actors[i].topic.c_str())) {
        Serial.println("Successfully subscribed");
      }
      else {
        Serial.println("Failed to subscribe....");
      }
    }
  }

}

void loop() {
   Serial.println("SETUP DONE");
   Serial.println("ENTERING LOOP");
  // set pin of actor to low when ticks_until passed.
  /*for (byte i = 0; i < (sizeof(actors) / sizeof(*actors)); i++) {
    if (actors[i].ticks_until < millis() && digitalRead(actors[i].pin)) {
      Serial.print("Setting actor to low: ");
      Serial.println(actors[i].topic);
      digitalWrite(actors[i].pin, LOW);
    }
  }
  */
  if (!client.connected()) {
    reconnect();
  }

  if (client.connected()) {
    client.loop();

    // send each 60s sensor data to cloud.
    long now = millis();
    if (now - lastSensorDataSent > 1000) {
      lastSensorDataSent = now;

      sendSensorData();
    }
  }
  delay(2000);
  Serial.println("LOOP COMPLETED");
}

void sendSensorData() {
  String payload;

  // read sensor data
  int val = analogRead(0);

  /*Create json payload.
     Example:
     {
       "sensorName": "<name of sensor>",
       "type": "<type of sensor>",
       "value": <value read from sensor (0 - 1023)>,
       "location": "<location of sensor>",
       "version": <version of this sensor>
      }
  */

  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& root = jsonBuffer.createObject();
  root["sensorName"] = sensor_name;
  root["type"] = sensor_type;
  root["value"] = 800;
  root["location"] = sensor_location;
  root["version"] = 1;

  root.printTo(payload);

  Serial.print("Sending: ");
  Serial.println(payload);
  char payloadChar[payload.length() + 1];

  payload.toCharArray(payloadChar, payload.length() + 1);

  client.publish("habarama", payloadChar, true);
}
