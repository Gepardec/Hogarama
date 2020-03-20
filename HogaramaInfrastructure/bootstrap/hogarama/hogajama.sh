#!/bin/bash

function hogajama () {
  local git_branch=${1}
  local namespace=${2}
  local oc_admin_token=${3}
  local oc_cluster=${4}

  oc-create-resource "admin" ${namespace} "resources/hogajama/s2i-builder-maven-is.yml"
  oc-create-resource "admin" ${namespace} "resources/hogajama/maven-is.yml"
  oc-create-resource "admin" ${namespace} "resources/hogajama/hogajama-binary-is.yml"
  oc-create-resource "admin" ${namespace} "resources/hogajama/hogajama-binary-bc.yml"
  oc-create-resource "admin" ${namespace} "resources/hogajama/s2i-builder-maven-bc.yml"
  oc-create-resource "admin" ${namespace} "resources/hogajama/hogajama-binary-bc.yml" # "--param GIT_BRANCH=master" #TODO: change to template && change to ${git_branch} after merging back into hogarama repository
  oc-create-resource "admin" ${namespace} "resources/hogajama/hogajama-run-is.yml"
  oc-create-resource "admin" ${namespace} "resources/hogajama/hogajama-run-bc.yml"
  oc-create-resource-from "cm" "hogajama-standalone" "--from-file=${TOPLEVEL_DIR}/configs/hogajama/standalone-openshift.xml"  
  oc-create-resource "admin" ${namespace} "resources/hogajama/eap-subscription.yml"
  oc-create-resource "admin" ${namespace} "resources/hogajama/crd.yml"
  oc-create-resource "admin" ${namespace} "resources/hogajama/route.yml"

}
readonly -f hogajama
[ "$?" -eq "0" ] || return $?
