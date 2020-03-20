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
  
  # getopts
  local opts=`getopt -o hdq --long help,dryrun,quiet -- "$@"`
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
      -h | --help)
          usage_message
          exit 0
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

  ####
  # CORE LOGIC
  execute "docker run --rm -it \
  -v ${TOPLEVEL_DIR}:/mnt/hogarama \
  fhochleitner/oc-helm:latest \
  helm template /mnt/hogarama/helm/hogarama"


}
readonly -f main
[ "$?" -eq "0" ] || return $?

main $@