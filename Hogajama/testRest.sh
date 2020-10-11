#!/bin/sh

echo "This works only when you switch the KEYCLOAK authentication for BASIC authentication

CURL="curl -v -u testuser:testuser@123"
URL=http://localhost:8080/hogajama-rs/rest/unitmanagement

$CURL -H 'Accept: application/json' -H 'Content-Type: application/json' -X GET $URL/rule

$CURL -H 'Accept: application/json' -H 'Content-Type: application/json' -X PUT $URL/rule \
 -d '{"name":"Pflanze_Pumpe50","sensorId":10,"actorId":10,"unitId":20,"waterDuration":20,"lowWater":0.5}'

$CURL -H 'Accept: application/json' -H 'Content-Type: application/json' -X PATCH $URL/rule/256 \
  -d '{"id":256, "name":"Pflanze_Pumpe60","sensorId":10,"actorId":10,"unitId":20,"waterDuration":20,"lowWater":0.5}'
