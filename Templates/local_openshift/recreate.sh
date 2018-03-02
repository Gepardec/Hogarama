#!/bin/sh

oc delete all --all
oc delete secret amq-app-secret
oc delete pvc broker-amq-claim fluentd-log-claim mongodb
oc delete template hogarama-s2i
oc create -f hogarama_template_with_routes_s2i.yaml
oc process hogarama-s2i | oc create -f -