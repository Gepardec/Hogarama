#!/bin/bash

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
    h) echo -e $help;;
    *) echo -e $help;;
  esac

done

echo $source


if [ -z $source ]
then
  echo "No source directory is specified"
  echo -e $help
  exit
fi

s2i build $source registry.access.redhat.com/jboss-eap-7/eap70-openshift 172.30.1.1:5000/hogarama/hogajama 
OPENSHIFT_TOKEN=$(oc whoami -t)
docker login -u developer -p ${OPENSHIFT_TOKEN} 172.30.1.1:5000
docker push 172.30.1.1:5000/hogarama/hogajama 


