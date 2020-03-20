#!/bin/bash

/opt/amq/bin/launch.sh nostart
cp /home/jboss/cfg/* /home/jboss/broker/etc/
cp /home/jboss/lib/* /home/jboss/broker/lib/
/home/jboss/broker/bin/artemis run