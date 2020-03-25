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

for functionFile in ${TOPLEVEL_DIR}/helm/scripts/functions/*.active;
  do source ${functionFile}
done

##########
# SCRIPT #
##########

usage_message () {
  echo """Usage:
    $PROGNAME --resource RESOURCE [--resource RESOURCE] [OPT ..]
      -r | --resource) ... multiple definitions possible

      -f | --force)    ... process template even if target file exists
      -d | --dryrun)   ... dryrun
      -q | --quiet)    ... quiet

      -h | --help)     ... help"""
}
readonly -f usage_message
[ "$?" -eq "0" ] || return $?

main () {
  # INITIAL VALUES
  local resources=()
  local options=""
  local extravars=""

  # GETOPT
  OPTS=`getopt -o dhtfe:q --long dryrun,help,force,quiet -- "$@"`
  if [ $? != 0 ]; then
    echo "failed to fetch options via getopt"
    exit $EXIT_FAILURE
  fi
  eval set -- "$OPTS"
  while true ; do
    case "$1" in
      -d | --dryrun)
        FLAG_DRYRUN=true
        shift
        ;;
      -q | --quiet)
        FLAG_QUIET=true
        shift
        ;;
      -f | --force)
        FLAG_FORCE=true
        shift
        ;;
      -h | --help)
        usage_message
        exit 0
        ;;
      -e )
        extravars="${extravars} -e ${2}"
        shift 2
        ;;
      *)
        break
        ;;
    esac
  done


  ####
  # CORE LOGIC
  set -e
    j2-template "${TOPLEVEL_DIR}" "helm" "${extravars}"
  set +e
}

main $@
