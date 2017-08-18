#!/bin/sh

set -x

oc cluster up
oc login -u system:admin
oc policy add-role-to-user system:image-pusher developer
oc create -f alltemplates.yaml -n openshift
oc login -u developer
oc new-project hogarama
oc create is hogajama
oc create is fluentd
oc process -f hogaramaOhneHost.yaml | oc create -f -
