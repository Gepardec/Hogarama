#!/bin/sh

cd $SETUP
git clone https://github.com/Gepardec/JBSS.git
env JB_HOME=/opt/wildfly ./JBSS/bin/setup.sh -i hogajama -b /usr/bin -r wildfly-latest
echo export ENV_FILE=$SETUP/local_env/hogarama_local.env >> ~/.hogajamarc

# Configure WildFly
hogajama configure /tmp/setup/config-all/

# Create a link to the deployments directory
ln -sf /deployments /opt/wildfly/standalone/deployments
