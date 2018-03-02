#!/bin/sh

set -x

oc login -u system:admin
oc create -n openshift -f red_hat_middleware_imagestreams.yml
oc login -u developer
oc create serviceaccount eap-service-account
oc policy add-role-to-user view system:serviceaccount:$(oc project -q):eap-service-account
oc create -f hogarama_template_with_routes_s2i.yaml
oc process hogarama-s2i | oc create -f -
oc patch dc/broker-amq --type=json \
-p '[{"op": "add", "path": "/spec/template/spec/serviceAccountName", "value": "eap-service-account"}]'
