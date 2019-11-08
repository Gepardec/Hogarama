#include <ESP8266WiFi.h>            //ESP8266 Core WiFi Library
#include <DNSServer.h>              //Local DNS Server used for redirecting all requests to the configuration portal
#include <WiFiClient.h>             //Local WebServer used to serve the configuration portal
#include <EEPROM.h>
#include <ESP8266WebServer.h>
#include <ESP8266LLMNR.h>
//#include <WiFiManager.h>          //WiFi Configuration Magic - https://github.com/tzapu/WiFiManager
#include <PubSubClient.h>           // Client to connect to cloud via mqtt - https://github.com/plapointe6/EspMQTTClient
#include <ArduinoJson.h>            // Library to parse Json. Used for payload of mqtt ATTENTION: Version 5 is required at the moment. Support for v6 in the Future
#include <stdio.h>

// configuration for sensor
#define sensor_name "Blubb"
#define sensor_type "sparkfun"
#define sensor_location "Gruenbach"

// configuration for mqtt server
#define mqtt_server "broker-amq-mqtt-ssl-57-hogarama.cloud.itandtel.at"
#define mqtt_server_port 443
#define mqtt_user "mq_habarama"
#define mqtt_password "mq_habarama_pass"
#define isSensor true
#define isActor false
#define waitIntervall 5000
#define waitIntervallWifi 3000

// configuration clear EEPROM/WiFiSettings
#define sizeBytes 4096
// change it for lower or higher endByte (Default for ESP8266_deauther = 4096)
// normaly it's the same as sizeBytes
#define endByte 4096
// change it for lower or higher startByte (Default = 0)
#define startByte 0


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

//configuration for button.
const int buttonPin = D3;     // the number of the pushbutton pin
int buttonState = 0;         // variable for reading the pushbutton status

const int sensorPin = 0;

long lastSensorDataSent = 0;
long lastReconnect = 0;

const IPAddress apIP(192, 168, 1, 1);
const char* apSSID = "MINIRAMA_SETUP";
boolean settingMode;
String ssidList;

DNSServer dnsServer;
ESP8266WebServer webServer(80);

//EEPROM clearing
unsigned long ok = 0;
unsigned long nok = 0;
unsigned long tok = 0;

void setup() {
  Serial.begin(115200);

  pinMode(buttonPin, INPUT);  //    initialize digital pin buttonPin -D3 as an input.
  pinMode(LED_BUILTIN, OUTPUT);  // initialize digital pin LED_BUILTIN as an output.

  EEPROM.begin(512);
  delay(10);
  if (restoreConfig()) {
    if (checkConnection()) {
      settingMode = false;
      if (WiFi.status() == WL_CONNECTED) {
      initializeMqtt();
      return;
      }
    }
  }
  settingMode = true;
  setupMode();
}

void loop() {
  if (settingMode) {
    dnsServer.processNextRequest();
  }
  webServer.handleClient();


  if (WiFi.status() == WL_CONNECTED) {
    if (!client.connected()) {
      reconnect();
      delay(waitIntervallWifi);
    }

    if (isActor) {
    // set pin of actor to low when ticks_until passed.
      handleActor();
    }

    if (isSensor) {
      handleSensor();
    }
    pressButtonClearEEPROM();
    delay(waitIntervall);
    Serial.println("LOOP COMPLETED");
  }

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


void pressButtonClearEEPROM(){
  // read the state of the pushbutton value:
  buttonState = digitalRead(buttonPin);

  // check if the pushbutton is pressed. If it is, the buttonState is HIGH:
  if (buttonState == LOW) {
    blinkForSetup();
    Serial.println("BUTTON IS PRESSED!");
    clearEEPROM();
  } else {
    Serial.println("Button is NOT pressed");
  }
}

void setGpio(actor_type *actor, long duration) {
  Serial.print("Setting actor to hight: ");
  Serial.println(actor->topic);

  actor->ticks_until = millis() + duration;

  digitalWrite(actor->pin, HIGH);
}

boolean restoreConfig() {
  Serial.println("Reading EEPROM...");
  String ssid = "";
  String pass = "";
  if (EEPROM.read(0) != 255) {  // empty EEPROM is 255 instead of 0
    for (int i = 0; i < 32; ++i) {
      ssid += char(EEPROM.read(i));
    }
    Serial.print("SSID: ");
    Serial.println(ssid);
    for (int i = 32; i < 96; ++i) {
      pass += char(EEPROM.read(i));
    }
    Serial.print("Password: ");
    Serial.println(pass);
    WiFi.begin(ssid.c_str(), pass.c_str());
    delay(500);
    return true;
  }
  else {
    Serial.println("Config not found.");
    return false;
  }
}

boolean checkConnection() {
  int count = 0;
  Serial.print("Waiting for Wi-Fi connection");
  while ( count < 30 ) {
    if (WiFi.status() == WL_CONNECTED) {
      Serial.println();
      Serial.println("Connected!");
      return (true);
    }
    delay(500);
    Serial.print(".");
    count++;
  }
  Serial.println("Timed out.");
  return false;
}

void startWebServer() {
  if (settingMode) {
    Serial.print("Starting Web Server at ");
    Serial.println(WiFi.softAPIP());
    webServer.on("/settings", []() {
      String s = "<h1>Wi-Fi Settings</h1><p>Please enter your password by selecting the SSID.</p>";
      s += "<form method=\"get\" action=\"setap\"><label>SSID: </label><select name=\"ssid\">";
      s += ssidList;
      s += "</select><br>Password: <input name=\"pass\" length=64 type=\"password\"><input type=\"submit\"></form>";
      webServer.send(200, "text/html", makePage("Wi-Fi Settings", s));
    });
    webServer.on("/setap", []() {
      for (int i = 0; i < 96; ++i) {
        EEPROM.write(i, 0);
      }
      String ssid = urlDecode(webServer.arg("ssid"));
      Serial.print("SSID: ");
      Serial.println(ssid);
      String pass = urlDecode(webServer.arg("pass"));
      Serial.print("Password: ");
      Serial.println(pass);
      Serial.println("Writing SSID to EEPROM...");
      for (int i = 0; i < ssid.length(); ++i) {
        EEPROM.write(i, ssid[i]);
      }
      Serial.println("Writing Password to EEPROM...");
      for (int i = 0; i < pass.length(); ++i) {
        EEPROM.write(32 + i, pass[i]);
      }
      EEPROM.commit();
      Serial.println("Write EEPROM done!");
      String s = "<h1>Setup complete.</h1><p>device will be connected to \"";
      s += ssid;
      s += "\" after the restart.";
      webServer.send(200, "text/html", makePage("Wi-Fi Settings", s));
      ESP.restart();
    });
    webServer.onNotFound([]() {
      String s = "<h1>AP mode</h1><p><a href=\"/settings\">Wi-Fi Settings</a></p>";
      webServer.send(200, "text/html", makePage("AP mode", s));
    });
  }
  else {
    Serial.print("Starting Web Server at ");
    Serial.println(WiFi.localIP());
    webServer.on("/", []() {
      String s = "<h1>STA mode</h1><p><a href=\"/reset\">Reset Wi-Fi Settings</a></p>";
      webServer.send(200, "text/html", makePage("STA mode", s));
    });
    webServer.on("/reset", []() {
      for (int i = 0; i < 96; ++i) {
        EEPROM.write(i, 0);
      }
      EEPROM.commit();
      String s = "<h1>Wi-Fi settings was reset.</h1><p>Please reset device.</p>";
      webServer.send(200, "text/html", makePage("Reset Wi-Fi Settings", s));
    });
  }
  webServer.begin();
}

void setupMode() {
  WiFi.mode(WIFI_STA);
  WiFi.disconnect();
  delay(100);
  int n = WiFi.scanNetworks();
  delay(100);
  Serial.println("");
  for (int i = 0; i < n; ++i) {
    ssidList += "<option value=\"";
    ssidList += WiFi.SSID(i);
    ssidList += "\">";
    ssidList += WiFi.SSID(i);
    ssidList += "</option>";
  }
  delay(100);
  WiFi.mode(WIFI_AP);
  WiFi.softAPConfig(apIP, apIP, IPAddress(255, 255, 255, 0));
  WiFi.softAP(apSSID);
  dnsServer.start(53, "*", apIP);
  startWebServer();
  Serial.print("Starting Access Point at \"");
  Serial.print(apSSID);
  Serial.println("\"");
}

String makePage(String title, String contents) {
  String s = "<!DOCTYPE html><html><head>";
  s += "<meta name=\"viewport\" content=\"width=device-width,user-scalable=0\">";
  s += "<title>";
  s += title;
  s += "</title></head><body>";
  s += contents;
  s += "</body></html>";
  return s;
}

String urlDecode(String input) {
  String s = input;
  s.replace("%20", " ");
  s.replace("+", " ");
  s.replace("%21", "!");
  s.replace("%22", "\"");
  s.replace("%23", "#");
  s.replace("%24", "$");
  s.replace("%25", "%");
  s.replace("%26", "&");
  s.replace("%27", "\'");
  s.replace("%28", "(");
  s.replace("%29", ")");
  s.replace("%30", "*");
  s.replace("%31", "+");
  s.replace("%2C", ",");
  s.replace("%2E", ".");
  s.replace("%2F", "/");
  s.replace("%2C", ",");
  s.replace("%3A", ":");
  s.replace("%3A", ";");
  s.replace("%3C", "<");
  s.replace("%3D", "=");
  s.replace("%3E", ">");
  s.replace("%3F", "?");
  s.replace("%40", "@");
  s.replace("%5B", "[");
  s.replace("%5C", "\\");
  s.replace("%5D", "]");
  s.replace("%5E", "^");
  s.replace("%5F", "-");
  s.replace("%60", "`");
  return s;
}

void sendSensorData() {

  /*Create json payload.
     Example:
     {
       "sensorName": "<name of sensor>",
       "type": "<type of sensor>",
       "value": <value read from sensor (0 - 1023)>,
       "location": "<location of sensor>",
       "version": <version of this sensor>
       "macAddress" : <mac address of wifi-module>
      }
  */


  String clientMac = WiFi.macAddress();
  //clientMac.replace(":", "");
  //clientMac.remove(8, 10);

  String payload;
  // read sensor data
  StaticJsonBuffer <300> jsonBufferPayload ;
  JsonObject& root = jsonBufferPayload.createObject();
  root["sensorName"] = sensor_name;
  root["type"] = sensor_type;
  root["value"] = analogRead(sensorPin);;
  root["location"] = sensor_location;
  root["version"] = 2;
  root["deviceId"] = clientMac;

  root.printTo(payload);

  Serial.print("Sending: ");
  char payloadChar[payload.length() + 20];
  Serial.println(payload);
  payload.toCharArray(payloadChar, payload.length() + 20);

  bool result = client.publish("habarama", payloadChar, true);
  Serial.println(result);
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

void handleSensor() {
  if (client.connected()) {
    client.loop();

    // send each 60s sensor data to cloud.
    long now = millis();
    if (now - lastSensorDataSent > 1000) {
      lastSensorDataSent = now;

      sendSensorData();
    }
  }
}

void handleActor() {
  for (byte i = 0; i < (sizeof(actors) / sizeof(*actors)); i++) {
    if (actors[i].ticks_until < millis() && digitalRead(actors[i].pin)) {
      Serial.print("Setting actor to low: ");
      Serial.println(actors[i].topic);
      digitalWrite(actors[i].pin, LOW);
    }
  }
}

void clearEEPROM(){
  EEPROM.begin(sizeBytes);

  delay(100);

  Serial.println("**********************************************************************************************************");
  Serial.println("");
  Serial.print("    Write a char(255) / hex(FF) from byte ");
  Serial.print(startByte);
  Serial.print(" to ");
  Serial.print(endByte - 1);
  Serial.print(" into the EEPROM with a defined size of ");
  Serial.print("");
  Serial.print(sizeBytes);
  Serial.println(" Bytes");
  Serial.println("");
  Serial.println("**********************************************************************************************************");
  Serial.println("");

  Serial.println("             testing EEPROM for written bytes");
  Serial.println("");

  for (int i = startByte; i < endByte; ++i)
  {
    if (EEPROM.read(i) == 255) {
      ++ok;
    } else {
      ++nok;
    }
  }

  Serial.printf("               empty bytes: %6d\r\n", ok);
  Serial.printf("           not empty bytes: %6d\r\n", nok);
  Serial.println("");
  Serial.println("**********************************************************************************************************");
  Serial.println("");

  Serial.println("**********************************************************************************************************");
  Serial.println("");
  Serial.println("             Start clearing EEPROM... - Please wait!!!");
  Serial.println("");
  Serial.println("**********************************************************************************************************");

  delay(1000);

  // write a char(255) / hex(FF) from startByte until endByte into the EEPROM
  for (int i = startByte; i < endByte; ++i) {
    EEPROM.write(i, -1);
  }

  EEPROM.commit();

  delay(1000);

  Serial.println("");
  Serial.println("             testing EEPROM for clearing");
  Serial.println("");

  String test;
  for (int i = startByte; i < endByte; ++i)
  {
    if (EEPROM.read(i) == 255) {
      ++tok;
    }
  }
  Serial.println("**********************************************************************************************************");
  Serial.println("");
  if (tok = endByte - startByte) {
    Serial.println("             EEPROM killed correctly");
  } else
    Serial.println("             EEPROM not killed - ERROR !!!");

  Serial.println("");
  Serial.println("**********************************************************************************************************");
  Serial.println("");
  Serial.println("             Ready - You can remove your ESP8266 / LoLin");
  Serial.println("");
  Serial.println("**********************************************************************************************************");
  Serial.println("Start Setup");
  setup();

}

void blinkForSetup(){
   for(int i = 0; i < 5; i++){
       digitalWrite(LED_BUILTIN, HIGH);   // turn the LED on (HIGH is the voltage level)
       Serial.println("blink");
       delay(500);
       digitalWrite(LED_BUILTIN, LOW);   // turn the LED on (HIGH is the voltage level)
       delay(500);
       digitalWrite(LED_BUILTIN, HIGH);   // turn the LED on (HIGH is the voltage level)
   }
}




