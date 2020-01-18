#!/bin/sh

oc delete all -l template=hogarama-fluentd
oc delete persistentvolumeclaim -l template=hogarama-fluentd
oc delete secrets -l template=hogarama-fluentd
oc process -f hogarama-fluentd.yaml BRANCH=master | oc create -f -
