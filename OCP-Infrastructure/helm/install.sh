#!/bin/sh

for n in amq mongodb keycloakx hogajama; do
  ( cd $n && ./install.sh )
done
