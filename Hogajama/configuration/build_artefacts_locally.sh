#!/bin/sh

PRG=`readlink -e $0`
DIR=`dirname $PRG`
HOGAJAMA_HOME=$DIR/..

cd $HOGAJAMA_HOME

echo "Build Artefact Container-Image hogarama-build-artefacts in $HOGAJAMA_HOME"
s2i build . quay.io/wildfly/wildfly-s2i-jdk11 hogarama-build-artefacts --incremental
