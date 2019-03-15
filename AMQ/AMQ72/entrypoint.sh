#!/bin/sh

if [ -z "${KEYCLOAK_AUTH_SERVER_URL}" ] 
then 
echo "Required variable missing: KEYCLOAK_AUTH_SERVER_URL"
exit 1 
fi

/opt/amq/bin/launch.sh nostart

cp -r /home/jboss/cfg/lib/* /home/jboss/broker/lib
rm -f /home/jboss/broker/etc/broker.xml
rm -f /home/jboss/broker/etc/login.config

cp /home/jboss/cfg/etc/broker.xml /home/jboss/broker/etc/
cp /home/jboss/cfg/etc/login.config /home/jboss/broker/etc/
cp /home/jboss/cfg/etc/keycloak.json /home/jboss/broker/etc/
cp /home/jboss/cfg/etc/broker.jks /home/jboss/broker/etc/

cd /home/jboss/broker/etc/
sed 's/domain=\"activemq\"/domain=\"keycloak\"/g' bootstrap.xml > text.xml && rm bootstrap.xml && mv text.xml bootstrap.xml

exec /home/jboss/broker/bin/artemis run