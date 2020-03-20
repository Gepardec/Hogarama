#!/bin/bash

function fluentd () {
  local git_branch=${1}
  local namespace=${2}
  local oc_admin_token=${3}
  local oc_cluster=${4}

  oc-create-resource-from "secret" "fluentd-secret" "generic --from-file=${TOPLEVEL_DIR}/secrets/fluentd/fluent.conf"
  oc-create-resource "admin" ${namespace} "resources/fluentd/imagestream.yml"
  #oc-create-resource "admin" ${namespace} "resources/fluentd/pvc.yml"
  oc-create-from-template "admin" ${namespace} "resources/fluentd/fluentd-buildconfig.yml" "--param GIT_BRANCH=${git_branch}"
  oc-create-resource "admin" ${namespace} "resources/fluentd/fluentd-deploymentconfig.yml"
  oc-create-resource "admin" ${namespace} "resources/fluentd/service.yml"
}
readonly -f fluentd
[ "$?" -eq "0" ] || return $?
