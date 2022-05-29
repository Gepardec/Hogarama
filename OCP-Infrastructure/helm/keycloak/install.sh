#!/bin/sh

helm template helm-keycloak . -f ../secrets/values.yaml
