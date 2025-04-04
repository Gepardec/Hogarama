This page gives some hints how you can troubleshoot various problems with Hogarama

## Website shows "Could not load data. Please try again later."

The error says that JBoss didn't find any data in the Mongodb. There can be several reasons for this.

* The Client didn't send data
* The Kafka-Conncetor didn't forward the data from ActiveMQ to Kafka
* Hogajama can't read the data from Kafka
* The connection from Hogajama (JBoss) to MongoDB is lost
* MongoDB is down

### Test with Mock GUI

The easiest way to check whether the server part is functional is to use
the [Raspberry-PI-Mocks-GUI](Raspberry-PI-Mocks-GUI). Just open the Url and run a few values. It sends data to AMQ. If
it works the OpenShift part works correctly and its probably a client issue.

### Test the pump ###

Send a GET Request to ```<Actor RS URL>?location=...&actorName=...&duration=...```

### Check Logs

First thing is to check the Logs of the pods from the Fluentd, Hogajama and Mongodb. In Hogajama you should see
something like this:

```
12:17:58,600 INFO  [com.gepardec.hogarama.service.WateringKafkaEndpoint] (vert.x-eventloop-thread-0) Receive message from habarama-in: {"sensorName": "DIYGepard1", "type": "sparkfun", "value": 525, "location": "Wien", "version": 1 }
```

If something is wrong, you most likely see some Error messages in the logs

### Do we get data in Kafka?

See [[Kafka]] and use the following:

```
kafka-console-consumer --bootstrap-server https://gepardec-kafka-cluster-kafka-bootstrap-hogarama-kafka.apps.play.gepaplexx.com:443 --topic habarama --consumer-property security.protocol=SSL --consumer-property ssl.truststore.password=password --consumer-property ssl.truststore.location=Hogajama/configuration/local_configuration/kafka-truststore.jks 
```

You should see something like:

```
{"sensorName": "verspielterGepard", "type": "sparkfun", "value": 631, "location": "Wien", "version": 1 }
{"sensorName": "DIYGepard1", "type": "sparkfun", "value": 523, "location": "Wien", "version": 1 }
{"sensorName": "verspielterGepard", "type": "sparkfun", "value": 630, "location": "Wien", "version": 1 }
{"sensorName": "DIYGepard1", "type": "sparkfun", "value": 524, "location": "Wien", "version": 1 }
```

### Check for data in MongoDB

[Login-to-mongodb](Login-to-mongodb)
Then read the data:

```
> show collections                                                                                                                                                                                
habarama                                                                                                                                                                                          
watering                                                                                                                                                                                          
wateringConfig                                                                                                                                                                                    
> db.habarama.find()                                                                                                                                                                              
{ "_id" : ObjectId("5bafc64371a07b000b392391"), "sensorName" : "Mock Sensor", "type" : "wasser", "value" : 50, "location" : "Wien", "version" : 0, "time" : ISODate("2018-09-29T18:36:48.840Z") } 
{ "_id" : ObjectId("5bafc64671a07b000b392392"), "sensorName" : "Mock Sensor", "type" : "wasser", "value" : 50, "location" : "Wien", "version" : 0, "time" : ISODate("2018-09-29T18:36:51.833Z") } 
{ "_id" : ObjectId("5bafc64971a07b000b392393"), "sensorName" : "Mock Sensor", "type" : "wasser", "value" : 70, "location" : "Wien", "version" : 0, "time" : ISODate("2018-09-29T18:36:54.830Z") } 
{ "_id" : ObjectId("5bb0743171a07b000b392394"), "sensorName" : "GruenerGepard", "type" : "sparkfun", "value" : 33, "location" : "Wien", "version" : 1, "time" : ISODate("2018-09-30T06:58:54.807Z"
) }                                                                                                                                                                                               
{ "_id" : ObjectId("5bb0744271a07b000b392395"), "sensorName" : "GruenerGepard", "type" : "sparkfun", "value" : 26, "location" : "Wien", "version" : 1, "time" : ISODate("2018-09-30T06:59:11.826Z"
) }                                                                                                                                                                                               
{ "_id" : ObjectId("5bb0745371a07b000b392396"), "sensorName" : "GruenerGepard", "type" : "sparkfun", "value" : 29, "location" : "Wien", "version" : 1, "time" : ISODate("2018-09-30T06:59:28.846Z"
```

## Pump doesn't work

Again there can be several reasons on server- or on client-side. Therefore it is important to limit the possible cause.

### Check for watering requests

At regular intervals Hogajama checks whether it should activate the pump.
See [Using-Hogarama#configure-the-watering-strategy](Using-Hogarama#configure-the-watering-strategy). If the rule
triggers a watering request, you should see a little drop in the Hogajama chart (GUI) and a message in the Hogajama
Logs:

```
20:08:10,001 INFO  [com.gepardec.hogarama.service.schedulers.WateringScheduler] (EJB default - 4) Check for watering
20:08:10,002 INFO  [com.gepardec.hogarama.service.schedulers.SensorsScheduler] (EJB default - 3) Load the sensorNames from the database
20:08:10,026 INFO  [com.gepardec.hogarama.service.ActorServiceImpl] (EJB default - 4) sendActorMessage: location: Wien, actorName: Mock Sensor, duration: 5
20:08:10,059 INFO  [com.gepardec.hogarama.mocks.cli.MqttClient] (EJB default - 4) Publising to ssl://broker-amq-mqtt-ssl-57-hogarama.cloud.itandtel.at:443
20:08:10,282 INFO  [com.gepardec.hogarama.mocks.cli.MqttClient] (EJB default - 4) Published 1 of 1
```

You can also find these in mongodb:
[Login-to-mongodb](Login-to-mongodb)
Then read the data:

```
> db.watering.find()                                                                                                                                                                              
{ "_id" : ObjectId("5bb074b620808801b9e850be"), "className" : "com.gepardec.hogarama.domain.watering.WateringData", "time" : ISODate("2018-09-30T07:01:10.046Z"), "name" : "GruenerGepard", "locat
ion" : "Wien", "duration" : 5 }                                                                                                                                                                   
{ "_id" : ObjectId("5bb074f220808801b9e850bf"), "className" : "com.gepardec.hogarama.domain.watering.WateringData", "time" : ISODate("2018-09-30T07:02:10.007Z"), "name" : "GruenerGepard", "locat
ion" : "Wien", "duration" : 5 }                                                                                                                                                                   
```

### Check the Actor Topic in AMQ Pod

The watering request will be sent to a Topic with name `actor.location.actorname` of the broker-amq pod.
Go to the pod, locate and follow the link `Open Java Console`. It will open the Jolokia JMX console. Go e.g. to
`Topic->actor/Wien/GruenerGepard` You see values for enqueued and dequeued messages and number of consumers. There
should be at least one consumer (Raspi) and the same value for enqueued and dequeued messages (number of watering
requests).