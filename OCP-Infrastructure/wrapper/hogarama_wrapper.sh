#!/bin/bash

####################### 
# READ ONLY VARIABLES #
#######################

readonly SCRIPT_DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
readonly TOPLEVEL_DIR=$( cd ${SCRIPT_DIR}/.. > /dev/null && pwd )
ENGINE=${CONTAINER_ENGINE}

main () {

  if [[ "x${ENGINE}" == "x" ]]; then
    ENGINE=docker
  fi

  set -e
  ${ENGINE} run --rm -it \
    -v ${TOPLEVEL_DIR}/:/mnt/hogarama \
    -v ~/.kube/config/:/.kube/config \
    gepardec/hogarama-bootstrap:1.0.0\
    /mnt/hogarama/helm/scripts/hogarama.sh ${@}
  set +e
}
 
main $@