#!/bin/bash

function mongodb () {
  local git_branch=${1}
  local namespace=${2}
  local oc_admin_token=${3}
  local oc_cluster=${4}

  oc-create-resource "admin" ${namespace} "secrets/mongodb/mongodb-credentials.yml"
  oc-create-resource "admin" ${namespace} "resources/mongodb/imagestream.yml"
  #oc-create-resource "admin" ${namespace} "resources/mongodb/pvc.yml"
  oc-create-resource "admin" ${namespace} "resources/mongodb/deploymentconfig.yml"
  oc-create-resource "admin" ${namespace} "resources/mongodb/service.yml"
}
readonly -f mongodb
[ "$?" -eq "0" ] || return $?
