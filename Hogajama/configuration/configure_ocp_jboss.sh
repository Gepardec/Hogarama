#!/bin/sh

export JBOSS_DONT_UNPACK=true
export JBOSS_RELEASE_NAME=wildfly-23.0.1.Final
export JBOSS_OPTS="$JBOSS_OPTS --start-mode admin-only"

if [ -n "$ENV_FILE" ]; then
    tmp_env=`readlink -e $ENV_FILE`
    source ${tmp_env}
fi


echo "----------------------"
echo configure $0 in `pwd`
echo Environment:
set
echo "----------------------"

SETUP_HOME=`dirname $0`

$SETUP_HOME/bin/jboss7 configure $SETUP_HOME/local_configuration 

