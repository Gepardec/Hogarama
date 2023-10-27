#!/bin/sh

# Zuerst schema löschen: echo 'drop schema hogajama cascade;' | oc rsh helm-hogajama-postgresql-0  bash -c 'env PGPASSWORD=$POSTGRES_PASSWORD psql -U hogajama management'

# Aufruf mit postgres_restore.sh < dumpfile
oc rsh helm-hogajama-postgresql-0  bash -c 'env PGPASSWORD=$POSTGRES_PASSWORD psql -U hogajama management'
