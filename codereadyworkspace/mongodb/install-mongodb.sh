#!/bin/bash

source ${ENV_FILE}

helm repo add bitnami https://charts.bitnami.com/bitnami
helm upgrade -i mongodb bitnami/mongodb --version=8.3.2 \
    --set auth.enabled=true \
    --set auth.rootPassword=${MONGODB_ROOT_PASSWORD} \
    --set auth.username=${MONGOODB_USERNAME} \
    --set auth.password=${MONGODB_PASSWORD} \
    --set auth.database=${MONGODB_DATABASE} \
    --set replicaCount=1 \
    --set podSecurityContext.enabled=false \
    --set containerSecurityContext.enabled=false \
    --set persistence.enabled=false \
    --set arbiter.enabled=false \
    --set serviceAccount.create=false

