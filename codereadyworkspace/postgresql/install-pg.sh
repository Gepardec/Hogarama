#!/bin/bash

source ${ENV_FILE}

helm repo add bitnami https://charts.bitnami.com/bitnami
helm upgrade -i postgresql bitnami/postgresql --version=9.3.2 \
    --set global.postgresql.postgresqlDatabase=management \
    --set global.postgresql.postgresqlUsername=hogajama \
    --set global.postgresql.postgresqlPassword=${POSTGRESQL_PASSWORD} \
    --set persistence.enabled=false \
    --set securityContext.enabled=false
