#!/bin/sh

KEYCLOAK_AUTH_SERVER_URL="https://secure-sso-57-hogarama.cloud.itandtel.at/auth/"

BRANCH=$(git branch | grep ^* | cut -d ' ' -f2)
OPENSHIFT_TOKEN=$(oc whoami -t)
HOGARAMA_VARS="OPENSHIFT_AUTH_TOKEN=$OPENSHIFT_TOKEN BRANCH=$BRANCH"

HOGARAMA_VARS="$HOGARAMA_VARS KEYCLOAK_AUTH_SERVER_URL=$KEYCLOAK_AUTH_SERVER_URL"

echo "Using BRANCH=$BRANCH" >&2

oc delete all -l template=hogarama-jboss
oc delete persistentvolumeclaim -l template=hogarama-jboss
oc delete secrets -l template=hogarama-jboss
oc process -f hogarama-jboss.yaml $HOGARAMA_VARS  | oc create -f -
