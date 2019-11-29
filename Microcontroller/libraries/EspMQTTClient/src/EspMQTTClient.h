#ifndef ESP_MQTT_CLIENT_H
#define ESP_MQTT_CLIENT_H

#include <PubSubClient.h>

#ifdef ESP8266

  #include <ESP8266WiFi.h>
  #include <ESP8266WebServer.h>
  #include <ESP8266mDNS.h>
  #include <ESP8266HTTPUpdateServer.h>
  
  #define WebServer ESP8266WebServer
  #define ESPmDNS ESP8266mDNS
  #define ESPHTTPUpdateServer ESP8266HTTPUpdateServer

#else // for ESP32

  #include <WiFiClient.h>
  #include <WebServer.h>
  #include <ESPmDNS.h>
  #include "ESP32HTTPUpdateServer.h"

  #define ESPHTTPUpdateServer ESP32HTTPUpdateServer

#endif

#define MAX_TOPIC_SUBSCRIPTION_LIST_SIZE 10
#define MAX_DELAYED_EXECUTION_LIST_SIZE 10
#define CONNECTION_RETRY_DELAY 10 * 1000

void onConnectionEstablished(); // MUST be implemented in your sketch. Called once everythings is connected (Wifi, mqtt).

typedef void(*ConnectionEstablishedCallback) ();
typedef void(*MessageReceivedCallback) (const String &message);
typedef void(*DelayedExecutionCallback) ();

class EspMQTTClient 
{
private:
  // Wifi related
  bool mWifiConnected;
  unsigned long mLastWifiConnectionAttemptMillis;
  unsigned long mLastWifiConnectionSuccessMillis;
  const char* mWifiSsid;
  const char* mWifiPassword;
  WiFiClient mWifiClient;

  // MQTT related
  bool mMqttConnected;
  unsigned long mLastMqttConnectionMillis;
  const char* mMqttServerIp;
  const short mMqttServerPort;
  const char* mMqttUsername;
  const char* mMqttPassword;
  const char* mMqttClientName;
  bool mMqttCleanSession;
  char* mMqttLastWillTopic;
  char* mMqttLastWillMessage;
  bool mMqttLastWillRetain;

  PubSubClient mMqttClient;

  struct TopicSubscriptionRecord {
    String topic;
    MessageReceivedCallback callback;
  };
  TopicSubscriptionRecord mTopicSubscriptionList[MAX_TOPIC_SUBSCRIPTION_LIST_SIZE];
  byte mTopicSubscriptionListSize;

  // HTTP update server related
  char* mUpdateServerAddress;
  char* mUpdateServerUsername;
  char* mUpdateServerPassword;
  WebServer* mHttpServer;
  ESPHTTPUpdateServer* mHttpUpdater;

  // Delayed execution related
  struct DelayedExecutionRecord {
    unsigned long targetMillis;
    DelayedExecutionCallback callback;
  };
  DelayedExecutionRecord mDelayedExecutionList[MAX_DELAYED_EXECUTION_LIST_SIZE];
  byte mDelayedExecutionListSize = 0;

  // General behaviour related
  ConnectionEstablishedCallback mConnectionEstablishedCallback;
  bool mEnableSerialLogs;
  bool mShowLegacyConstructorWarning;


public:
  // Wifi + MQTT with no MQTT authentification
  EspMQTTClient(
    const char* wifiSsid, 
    const char* wifiPassword, 
    const char* mqttServerIp, 
    const char* mqttClientName = "ESP8266",
    const short mqttServerPort = 1883);

  // Wifi + MQTT with MQTT authentification
  EspMQTTClient(
    const char* wifiSsid,
    const char* wifiPassword,
    const char* mqttServerIp,
    const char* mqttUsername,
    const char* mqttPassword,
    const char* mqttClientName = "ESP8266",
    const short mqttServerPort = 1883);

  // Only MQTT handling (no wifi), with MQTT authentification
  EspMQTTClient(
    const char* mqttServerIp,
    const short mqttServerPort,
    const char* mqttUsername,
    const char* mqttPassword,
    const char* mqttClientName = "ESP8266");

  // Only MQTT handling without MQTT authentification
  EspMQTTClient(
    const char* mqttServerIp,
    const short mqttServerPort,
    const char* mqttClientName = "ESP8266");

  // Legacy constructor for version 1.3 - WILL BE DELETED SOON OR LATER
  EspMQTTClient(
    const char* wifiSsid, 
    const char* wifiPassword,
    ConnectionEstablishedCallback connectionEstablishedCallback,
    const char* mqttServerIp, 
    const short mqttServerPort = 1883,
    const char* mqttUsername = NULL, 
    const char* mqttPassword = NULL, 
    const char* mqttClientName = "ESP8266",
    const bool enableWebUpdater = true, 
    const bool enableSerialLogs = true);

  // Legacy constructor for version <= 1.2 - WILL BE DELETED SOON OR LATER
  EspMQTTClient(
    const char* wifiSsid, 
    const char* wifiPassword, 
    const char* mqttServerIp,
    const short mqttServerPort, 
    const char* mqttUsername, 
    const char* mqttPassword,
    const char* mqttClientName, 
    ConnectionEstablishedCallback connectionEstablishedCallback,
    const bool enableWebUpdater = true, 
    const bool enableSerialLogs = true);

  ~EspMQTTClient();

  // Optionnal functionnalities
  void enableDebuggingMessages(const bool enabled = true); // Allow to display usefull debugging messages. Can be set to false to disable them during program execution
  void enableHTTPWebUpdater(const char* username, const char* password, const char* address = "/"); // Activate the web updater, must be set before the first loop() call.
  void enableHTTPWebUpdater(const char* address = "/"); // Will set user and password equal to mMqttUsername and mMqttPassword
  void enableMQTTPersistence(); // Tell the broker to establish a persistant connection. Disabled by default. Must be called before the fisrt loop() execution
  void enableLastWillMessage(const char* topic, const char* message, const bool retain = false); // Must be set before the first loop() call.

  // Main loop, to call at each sketch loop()
  void loop();

  // MQTT related
  void publish(const String &topic, const String &payload, bool retain = false);
  void subscribe(const String &topic, MessageReceivedCallback messageReceivedCallback);
  void unsubscribe(const String &topic);   //Unsubscribes from the topic, if it exists, and removes it from the CallbackList.

  // Other
  void executeDelayed(const long delay, DelayedExecutionCallback callback);

  inline bool isConnected() const { return mWifiConnected && mMqttConnected; };
  inline void setOnConnectionEstablishedCallback(ConnectionEstablishedCallback callback) { mConnectionEstablishedCallback = callback; }; // Default to onConnectionEstablished, you might want to override this for special cases like two MQTT connections in the same sketch

private:
  void connectToWifi();
  void connectToMqttBroker();
  void mqttMessageReceivedCallback(char* topic, byte* payload, unsigned int length);
};

#endif