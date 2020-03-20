#!/bin/sh

BRANCH=$(git branch | grep ^* | cut -d ' ' -f2)

echo "Using BRANCH=$BRANCH" >&2

oc delete all -l template=hogarama-amq
oc delete persistentvolumeclaim -l template=hogarama-amq
oc delete secrets -l template=hogarama-amq
oc process -f hogarama-amq.yaml BRANCH=$BRANCH | oc create -f -
