#!/bin/sh

set -x

oc cluster up
oc login -u system:admin
oc policy add-role-to-user system:image-pusher developer
oc create -f alltemplates.yaml -n openshift
oc create -f hogaramaimagestreams.yaml -n openshift
oc login -u developer
oc new-project hogarama
oc create is hogajama
oc create is fluentd
oc create is debian
oc process -f hogaramaOhneHost.yaml | oc create -f -
docker pull debian:stretch-slim
OPENSHIFT_TOKEN=$(oc whoami -t)
docker login -u developer -p ${OPENSHIFT_TOKEN} 172.30.1.1:5000
docker tag debian:stretch-slim 172.30.1.1:5000/hogarama/debian:stretch-slim
docker push 172.30.1.1:5000/hogarama/debian:stretch-slim
