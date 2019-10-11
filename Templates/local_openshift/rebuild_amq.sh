#!/bin/sh

oc delete all -l template=hogarama-amq
oc delete persistentvolumeclaim -l template=hogarama-amq
oc delete secrets -l template=hogarama-amq
oc process -f hogarama-amq.yaml BRANCH=master | oc create -f -
