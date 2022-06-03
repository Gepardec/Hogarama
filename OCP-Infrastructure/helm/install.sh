#!/bin/sh

for n in hogajama mongodb amq keycloak; do
  ( cd $n && ./install.sh )
done
