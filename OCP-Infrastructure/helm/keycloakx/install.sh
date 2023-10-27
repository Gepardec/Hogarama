#!/bin/sh

cat 00-keycloak-config.yaml
helm template hogarama-sso bitnami/keycloak -f 01-keycloak-values.yaml
