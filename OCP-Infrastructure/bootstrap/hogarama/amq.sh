#!/bin/bash

function amq () {
  local git_branch=${1}
  local namespace=${2}
  local oc_admin_token=${3}
  local oc_cluster=${4}

  oc-create-resource-from "cm" "amq-configs" "--from-file=${TOPLEVEL_DIR}/configs/amq/broker.xml --from-file=${TOPLEVEL_DIR}/configs/amq/entrypoint.sh"
  oc-create-resource-from "secret" "amq-secret" "generic --from-file=${TOPLEVEL_DIR}/secrets/amq/broker.ks"
  oc-create-from-template "admin" ${namespace} "resources/amq/imagestream.yml"
  oc-create-resource "admin" ${namespace} "secrets/amq/amq-credentials.yml"
  oc-create-resource "admin" ${namespace} "resources/amq/amq-deploymentconfig.yml"
  oc-create-resource "admin" ${namespace} "resources/amq/service.yml"
  oc-create-resource "admin" ${namespace} "resources/amq/mqtt-route.yml"
  oc-create-resource "admin" ${namespace} "resources/amq/console-route.yml"
}
readonly -f amq
[ "$?" -eq "0" ] || return $?
