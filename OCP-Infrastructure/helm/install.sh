#!/bin/sh

for n in hogajama mongodb amq keycloak grafana; do
  ( cd $n && ./install.sh )
done
