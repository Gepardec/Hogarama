#!/bin/bash

readonly PROGNAME=`basename "$0"`
readonly SCRIPT_DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
readonly TOPLEVEL_DIR=$( cd ${SCRIPT_DIR}/../.. > /dev/null && pwd )
readonly RESOURCE_ORDER=("keycloak-commons"
                         "keycloak"
                         "hogarama-commons"
                         "amq"
                         "fluentd"
                         "prometheus"
                         "grafana"
                         "mongodb"
                         "hogajama"
                        )
readonly COMMANDS=( "template"
                    "install"
                    "upgrade"
                    "uninstall"
                    "replace-secrets"
                    "help"
                  )

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

main() {
    local resources=()
    local command=${1}
    local input=($@)
    local all_opts=(${input[@]:1})
    local extravars=""

    local namespace_hogarama=hogarama
    local namespace_keycloak=gepardec

    if [[ ! " ${COMMANDS[@]} " =~ " ${command} " ]] || [[ ${command} == "help" ]]; then
       usage_message
    fi

     # getopts
    local opts=`getopt -o fqdr:e: --long force,dryrun,quiet,resource:,ns-hogarama:,ns-keycloak: -- "${all_opts[@]}"`
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
        -d | --dryrun)
            FLAG_DRYRUN=true
            shift 1
            ;;
        -q | --quiet)
            FLAG_QUIET=true
            shift 1
            ;;
        -f | --force)
            FLAG_FORCE=true
            shift 1
            ;;
        -e )
            extravars="${extravars} -e ${2}"
            shift 2
            ;;
        --ns-hogarama)
            namespace_hogarama="${2,,}"
            shift 2
            ;;
        --ns-keycloak)
            namespace_keycloak="${2,,}"
            shift 2
            ;;
        *)
          break
          ;;
        esac
    done

    #####
    ### CHECK FOR SECRETS FILE
    #####

    FILE=${TOPLEVEL_DIR}/helm/secrets/secrets.yaml
    if [[ ! -f "$FILE" ]]; then
        echo "#########################################################################"
        echo "$FILE does not exist. Please go to Gepardec GDrive and download the file"
        echo "GEPARDEC-DRIVE/Projekte/Intern/Sonstiges/HOGARAMA-Resources/secrets.yaml"
        echo "#########################################################################"
        usage_message
        exit 1
    fi

    rc="$(oc whoami > /dev/null 2>&1  ;echo $?)"
    if [[ rc -gt 0 ]]; then
        echo "You are not logged in to the OpenShift Cluster, please login and try again"
        exit 1
    fi

    if [[ ${command} == "replace-secrets" ]];then
        j2-template "${TOPLEVEL_DIR}" "helm" "${extravars}"
        exit 0
    fi
}
readonly -f main
[ "$?" -eq "0" ] || return $?

main $@