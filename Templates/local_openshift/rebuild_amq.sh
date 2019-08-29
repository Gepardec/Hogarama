#!/bin/sh

oc delete service broker-amq-mqtt 
oc delete service broker-amq-mqtt-ssl
oc delete deploymentconfigs broker-amq
oc delete persistentvolumeclaims broker-amq-claim
oc delete secrets amq-app-secret
oc delete routes broker-amq-mqtt-ssl

oc process -f amq.yaml | oc create -f -
