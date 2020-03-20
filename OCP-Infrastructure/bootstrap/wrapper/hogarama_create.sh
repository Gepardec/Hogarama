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

########## 
# SOURCE #
##########

for functionFile in ${TOPLEVEL_DIR}/bootstrap/functions/*.active;
  do source ${functionFile}
done

source ${TOPLEVEL_DIR}/bootstrap/functions/hogarama_create.options

###############
# INIT VALUES #
###############

FLAG_DRYRUN=""
FLAG_FORCE=""
FLAG_QUIET=""
FLAG_HELP=""

##########
# SCRIPT #
##########

main () {

  # getopts
  local opts=`getopt -o hfr:q --long help,force,dryrun,resource:,quiet -- "$@"`
  local opts_return=$?
  if [ ${opts_return} != 0 ]; then
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
      -h | --help)
        FLAG_HELP=true
        ;;
      *)
          break
          ;;
      esac
  done

  local opts_name=OPTS_${resource^^}
  local options=${!opts_name}

  if [[ "x${options}" = "x" ]]; then
    options=${OPTS_DEFAULT}
  fi

  ## pipe through flags to underlying script ##
  if [[ "x${FLAG_FORCE}" != "x" ]]; then
    options="${options} --force"
  fi

  if [[ "x${FLAG_QUIET}" != "x" ]]; then
    options="${options} --quiet"
  fi

  if [[ "x${FLAG_DRYRUN}" != "x" ]]; then
    options="${options} --dryrun"
  fi

   if [[ "x${FLAG_HELP}" != "x" ]]; then
    options="${options} --help"
  fi

  set -e
  execute "docker run --rm -it \
    -v ${TOPLEVEL_DIR}:/mnt/hogarama \
    fhochleitner/oc-helm:latest \
    /mnt/hogarama/bootstrap/scripts/hogarama_create.sh --resource ${resource} ${options}"
  set +e
}
 
main $@