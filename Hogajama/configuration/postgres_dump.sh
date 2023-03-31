#!/bin/sh

oc rsh helm-hogajama-postgresql-0  bash -c 'env PGPASSWORD=$POSTGRES_PASSWORD pg_dump -U hogajama management'
