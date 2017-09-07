#!/bin/bash

arguments=$@
n_arguments=$#

if [ $n_arguments -eq 0 ]
then
  echo "No instance names supplied!"
  echo "Use with: make-hogarama [your-instance-name] [...]"
  exit
fi

for instance in "${arguments[@]}"
do
  #TODO: create new project
  #TODO: check if project creation has been successfull
  oc project 57-hogarama-schulung-2
  OPENSHIFT_TOKEN=$(oc whoami -t)
  oc process -f hogarama_template_with_routes.yaml STAGE=$instance OPENSHIFT_AUTH_TOKEN=$OPENSHIFT_TOKEN | oc create -f -
done

