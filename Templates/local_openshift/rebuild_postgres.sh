#!/bin/sh

oc delete all -l app=postgresql
oc delete persistentvolumeclaim -l app=postgresql
oc delete secrets -l name=postgresql
oc process -f hogarama-postgres.yaml  | oc create -f -
./install-pg.sh
