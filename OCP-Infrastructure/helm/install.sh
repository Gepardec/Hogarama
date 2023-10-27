#!/bin/sh

# Use e.g: ./install.sh | oc apply -f -

for n in amq mongodb keycloakx hogajama; do
  ( cd $n && ./install.sh )
done
