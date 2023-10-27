#!/bin/sh

PRG=`readlink -e $0`
DIR=`dirname $PRG`
HOGAJAMA_HOME=$DIR/..

cd $HOGAJAMA_HOME
 . $ENV_FILE
echo "Build Artefact Container-Image hogarama-build-artefacts in $HOGAJAMA_HOME with $MAVEN_ARGS"
s2i build -e "MAVEN_ARGS=$MAVEN_ARGS" . quay.io/wildfly/wildfly-s2i-jdk17 hogarama-build-artefacts --incremental --loglevel=1
