#!/bin/bash

BUILDER_IMAGE=registry.access.redhat.com/amq-broker-7/amq-broker-72-openshift
TARGET_IMAGE=172.30.1.1:5000/hogarama/broker-amq

source=""
help="\n
Builds jboss-eap-7 image for openshift with local source.\n
-s   path to source-directory, eg. Hogajama \n
-h   display help
"

while getopts s:h option
do
  case "${option}"
  in
    s) source=${OPTARG};;
    *) echo -e $help;;
  esac

done

echo "Using source directory: $source" >&2


if [ -z $source ]
then
  echo "No source directory is specified"
  echo -e $help
  exit
fi

s2i build $source $BUILDER_IMAGE $TARGET_IMAGE
OPENSHIFT_TOKEN=$(oc whoami -t)
docker login -u developer -p ${OPENSHIFT_TOKEN} 172.30.1.1:5000
docker push $TARGET_IMAGE


