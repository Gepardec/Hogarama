#!/bin/sh

cd $SETUP
git clone https://github.com/Gepardec/JBSS.git
env JB_HOME=/usr/jboss-hogajama ./JBSS/bin/setup.sh -i hogajama -b /usr/bin -r wildfly-26.1.1.Final
echo export ENV_FILE=$SETUP/local_env/hogarama_local.env >> ~/.hogajamarc
hogajama configure /tmp/setup/config-all/
