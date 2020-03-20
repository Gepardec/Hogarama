#!/bin/bash

function commons () {
  local git_branch=${1}
  local namespace=${2}
  local oc_admin_token=${3}
  local oc_cluster=${4}

  oc-create-resource "admin" ${namespace} "resources/commons/jboss-eap/jboss-eap-7-is.json"
  oc-create-resource "admin" ${namespace} "resources/commons/openjdk/openjdk-8-is.json"
  oc-create-resource "admin" ${namespace} "resources/commons/redhat-sso/redhat-sso-7-is.json"
  oc-create-resource "admin" ${namespace} "resources/commons/jboss-eap/jboss-eap-7-bc.yml"
  oc-create-resource "admin" ${namespace} "resources/commons/jboss-eap/jboss-eap-7-patched-is.yml"

}
readonly -f commons
[ "$?" -eq "0" ] || return $?
