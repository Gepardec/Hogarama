#!/bin/bash

prometheus() {
  local git_branch=${1}
  local namespace=${2}
  local oc_admin_token=${3}
  local oc_cluster=${4}

  oc-create-resource "admin" ${namespace} "resources/prometheus/prometheus-subscription.yml"
  oc-create-resource-from "secret" "prometheus-scrape-config" "generic --from-file=${TOPLEVEL_DIR}/secrets/prometheus/scrape-config.yml"
  oc-create-resource "admin" ${namespace} "resources/prometheus/crd.yml"
  oc-create-resource "admin" ${namespace} "resources/prometheus/route.yml"
}
readonly -f prometheus
[ "$?" -eq "0" ] || return $?
