#!/bin/sh

LABEL="app.kubernetes.io/name=postgresql"
oc delete all -l $LABEL
oc delete persistentvolumeclaim -l $LABEL
oc delete persistentvolumeclaim data-postgresql-postgresql-0
oc delete secret -l $LABEL
oc process -f hogarama-postgres.yaml  | oc create -f -
./install-pg.sh
