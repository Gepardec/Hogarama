#!/bin/sh

BRANCH=$(git branch | grep ^* | cut -d ' ' -f2)

echo "Using BRANCH=$BRANCH" >&2

oc delete all -l template=hogarama-fluentd
oc delete persistentvolumeclaim -l template=hogarama-fluentd
oc delete secrets -l template=hogarama-fluentd
oc process -f hogarama-fluentd.yaml BRANCH=$BRANCH | oc create -f -
