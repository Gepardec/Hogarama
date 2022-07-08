#!/bin/bash

echo "-------------"
echo "create broker"
echo "-------------"
/opt/amq/bin/artemis create \
  --allow-anonymous \
  --nio \
  --name broker \
  --no-amqp-acceptor \
  --no-stomp-acceptor \
  --no-hornetq-acceptor \
  --user amq \
  --password amq \
  --role admin \
  --ssl-key /etc/amq-secret-volume/broker.ks \
  --ssl-key-password 765rjvb5rtzh8i7587fg \
  --ssl-trust /etc/amq-secret-volume/broker.ks \
  --ssl-trust-password 765rjvb5rtzh8i7587fg \
  /opt/amq/instances/broker

echo "----------------"
echo " replace config "
echo "----------------"
cp -rf /etc/amq-secret-volume/broker.xml /opt/amq/instances/broker/etc
cp -rf /etc/amq-secret-volume/bootstrap.xml /opt/amq/instances/broker/etc
echo "DONE!"

echo "----------------"
echo "  start broker  "
echo "----------------"
/opt/amq/instances/broker/bin/artemis run
