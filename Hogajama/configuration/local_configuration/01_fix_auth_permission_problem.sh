#!/bin/sh

# Wildfly container has $JBOSS_HOME/standalone/tmp/auth set with 'drwxrw---- 2 jboss root 4096 Apr 27  2021 auth'
# That leads to probem with local authentication in the container with arbitrary uid.

rm -r $JBOSS_HOME/standalone/tmp
