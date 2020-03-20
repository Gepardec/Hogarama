#!/bin/bash

#######################
# READ ONLY VARIABLES #
#######################

# readonly PROGNAME=`basename "$0"`
# readonly SCRIPT_DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
# readonly TOPLEVEL_DIR=$( cd ${SCRIPT_DIR}/../.. > /dev/null && pwd )

##########
# SOURCE #
##########

# for functionFile in ${TOPLEVEL_DIR}/bootstrap/functions/*.active;do source ${functionFile}; done

############
# oc_setup #
############

function hogarama_commons () {
  local git_branch=${1}
  local namespace=${2}
  local oc_admin_token=${3}
  local oc_cluster=${4}

  oc-create-resource "admin" ${namespace} "resources/hogarama_commons/operator-group.yml"
  oc-create-resource "admin" ${namespace} "resources/hogarama_commons/anyuid-builder-serviceaccount.yml"
  oc-create-resource "admin" ${namespace} "resources/hogarama_commons/hogajama-anyuid.yml"
  execute "oc --kubeconfig=/home/.admin -n ${namespace} adm policy add-scc-to-user anyuid -z anyuid-builder"
  execute "oc --kubeconfig=/home/.admin -n ${namespace} policy add-role-to-user system:image-builder -z anyuid-builder"
  execute "oc --kubeconfig=/home/.admin -n ${namespace} adm policy add-scc-to-user anyuid -z hogajama-anyuid"
}
readonly -f hogarama_commons
[ "$?" -eq "0" ] || return $?
