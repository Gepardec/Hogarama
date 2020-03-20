#!/bin/bash

####################### 
# READ ONLY VARIABLES #
#######################

readonly PROGNAME=`basename "$0"`
readonly SCRIPT_DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
readonly TOPLEVEL_DIR=$( cd ${SCRIPT_DIR}/../.. > /dev/null && pwd )

####################
# GLOBAL VARIABLES #
####################

FLAG_DRYRUN=false
FLAG_QUIET=false
FLAG_FORCE=false

##########
# SOURCE #
##########

for functionFile in ${TOPLEVEL_DIR}/bootstrap/functions/*.active;
  do source ${functionFile}
done

##########
# SCRIPT #
##########

usage_message () {
  echo """Usage:
    $PROGNAME [OPT ..]
      --resource)                    ... the resource that should be created, e.g. amq, mongodb.
      --namespace)                   ... openshift project or kubernetes namespace
      --oc-admin-token)              ... token priveleged enough to execute oc new-project, e.g. admin role
      --oc-cluster)                  ... e.g. https://api.crc.testing:6443
      --git-branch)                  ... specify the git branch for oc resource references

      -f | --force)                  ... recreate without prompt
      -d | --dryrun)                 ... only print commands that would be executed
      -q | --quiet)                  ... quiet
      -h | --help)                   ... print this help text
    """
}
# readonly definition of a function throws an error if another function
# with the same name is defined a second time
readonly -f usage_message
[ "$?" -eq "0" ] || return $?

main () {
  # initial values
  local resource=""
  local git_branch=""
  local namespace=""
  local oc_admin_token=""
  local oc_cluster=""

  # getopts
  local opts=`getopt -o hfr:q --long git-branch:,oc-admin-token:,oc-cluster:,namespace:,help,force,dryrun,resource:,quiet -- "$@"`
  local opts_return=$?
  if [[ ${opts_return} != 0 ]]; then
      echo
      (>&2 echo "failed to fetch options via getopt")
      echo
      return ${opts_return}
  fi
  eval set -- "$opts"
  while true ; do
      case "$1" in
      --resource)
          resource=${2,,}
          shift 2
          ;;
      --oc-admin-token)
          oc_admin_token=$2
          shift 2
          ;;
      --oc-cluster)
          oc_cluster=$2
          shift 2
          ;;
      --namespace)
          namespace=$2
          shift 2
          ;;
      --git-branch)
          git_branch=$2
          shift 2
          ;;
      -h | --help)
          usage_message
          exit 0
          ;;
      -f | --force)
          FLAG_FORCE=true
          shift
          ;;
      --dryrun)
          FLAG_DRYRUN=true
          shift
          ;;
      -q | --quiet)
        FLAG_QUIET=true
        shift
        ;;
      *)
          break
          ;;
      esac
  done

  ####
  # CHECK INPUT
  # check if all required options are given

  if [ -z "${git_branch}" ] || [ -z "${namespace}" ] || [ -z "${oc_admin_token}" ] || [ -z "${resource}" ] || [ -z "${oc_cluster}" ]; then
      echo
      (>&2 echo "please provide all required options")
      echo
      usage_message
      return 1
  fi


  ####
  # CORE LOGIC

  oc --kubeconfig=/home/.admin login --token=${oc_admin_token} --insecure-skip-tls-verify=true ${oc_cluster} > /dev/null

  if ! oc --kubeconfig=/home/.admin get projects -o name | grep -E "${namespace}\$" > /dev/null; then
    if ${FLAG_FORCE}; then
      execute "oc --kubeconfig=/home/.admin new-project ${namespace} > /dev/null"
    else
      echo "it seems the project \"${namespace}\" does not exist. Do you want to create it?"
      read -p "Are you sure? [y/N]: " -r
      if [[ ${REPLY} =~ ^[Yy]$ ]]; then
          execute "oc --kubeconfig=/home/.admin new-project ${namespace} > /dev/null"
      else
        echo "project not created. Exiting"
        exit 0
      fi
    fi
  fi

  set -e
  source ${TOPLEVEL_DIR}/bootstrap/hogarama/${resource}.sh
  ${resource} ${git_branch} ${namespace} ${oc_admin_token} ${oc_cluster}
  set +e
}
readonly -f main
[ "$?" -eq "0" ] || return $?

main $@