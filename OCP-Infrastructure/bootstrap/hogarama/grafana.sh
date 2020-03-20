#!/bin/bash

function grafana () {
  local git_branch=${1}
  local namespace=${2}
  local oc_admin_token=${3}
  local oc_cluster=${4}

  oc-create-resource "admin" ${namespace} "resources/grafana/grafana-subscription.yml"
  oc-create-resource "admin" ${namespace} "resources/grafana/grafana-crd.yml"
  oc-create-resource "admin" ${namespace} "resources/grafana/grafana-dashboard.yml"
  oc-create-resource "admin" ${namespace} "resources/grafana/grafana-datasource.yml"
}
readonly -f grafana
[ "$?" -eq "0" ] || return $?

