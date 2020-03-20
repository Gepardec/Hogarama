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

##########
# SCRIPT #
##########

main () {
  # INITIAL VALUES

  # GETOPT

  ####
  # CHECK INPUT
  # check if all required options are given

  ####
  # CORE LOGIC
  
  set -e
  execute "docker run --rm -it \
    -v ${TOPLEVEL_DIR}:/mnt/hogarama \
    gepardec/j2cli:latest \
    hogarama/bootstrap/scripts/hogarama_template.sh ${*}"
  set +e
}
 
main $@