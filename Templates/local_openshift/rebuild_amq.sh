#!/bin/sh

oc delete all -l template=hogarama-amq
oc process -f hogarama-amq.yaml | oc create -f -
