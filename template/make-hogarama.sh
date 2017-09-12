#!/bin/bash

n_arguments=$#

if [ $n_arguments -eq 0 ]
then
  echo "No instance names supplied!"
  echo "Use with: make-hogarama [your-instance-name] [...]"
  exit
fi

DIR=`dirname $0`
DIR=${DIR-./}
template=hogarama_template_with_routes.yaml
 
cd $DIR

if [ ! -f $template ]; then
  echo "Template $template wurde in $DIR nicht gefunden"
  exit
fi

for instance in "$@"
do
  result=$(oc get projects | awk '{print $1}' | grep -F 57-$instance)
  if [ "$result" == "57-$instance" ]
  then
    oc project 57-$instance
    OPENSHIFT_TOKEN=$(oc whoami -t)
    oc process -f $template STAGE=$instance OPENSHIFT_AUTH_TOKEN=$OPENSHIFT_TOKEN | oc create -f -
  else 
    echo "Projekt 57-$instance existiert nicht!"  
  fi
done

