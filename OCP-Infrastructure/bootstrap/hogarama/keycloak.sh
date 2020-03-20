#!/bin/bash

function keycloak () {
  local git_branch=${1}
  local namespace=${2}
  local oc_admin_token=${3}
  local oc_cluster=${4}

  oc-create-resource "admin" ${namespace} "resources/keycloak/operator-group.yml"
  oc-create-resource "admin" ${namespace} "resources/keycloak/subscription.yml"
  oc-create-resource "admin" ${namespace} "resources/keycloak/keycloak-crd.yml"
  oc-create-resource "admin" ${namespace} "resources/keycloak/realm-gepardec-crd.yml"
  oc-create-resource "admin" ${namespace} "resources/keycloak/backup-crd.yml"
  oc-create-resource "admin" ${namespace} "resources/keycloak/client-hogarama-crd.yml"

}
readonly -f keycloak
[ "$?" -eq "0" ] || return $?
