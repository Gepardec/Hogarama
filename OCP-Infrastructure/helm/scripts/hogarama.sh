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
readonly REVERSE_RESOURCE_ORDER=("hogajama"
                         "mongodb"
                         "grafana"
                         "fluentd"
                         "amq"
                         "prometheus"
                         "hogarama-commons"
                         "keycloak"
                         "keycloak-commons"
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
    $PROGNAME COMMAND --resource RESOURCE [--resource RESOURCE] [OPT ..]
        available commands:
            install)                ... installs selected resource(s) in chosen namespace
            upgrade)                ... upgrades selected resource(s) in chosen namespace
            uninstall)              ... uninstalls selected resource(s) in chosen namespace
            template)               ... executes helm template for selected resource(s)
            replace-secrets)        ... creates values.yaml file with secrets provided in secrets/secrets.yaml
            help)                   ... this help menu

        availaible options:
            -r | --resource)        ... multiple definitions possible
                                        special resources: hogarama-all, keycloak-all, all
            -f | --force)           ... overwrites existing resources/executes helm upgrade if installation fails
            -d | --dryrun)          ... dryrun
            -q | --quiet)           ... quiet
            -e | --extravars)       ... multiple definitions possible. Add additional/overwrite variabls in values.yaml or secret.yaml\
            -w | --write-template)  ... helm template output will be written to secrets working directory
            --ns-hogarama)          ... namespace to/from which hogarama resources will be installed/uninstalled
                                        default-value: hogarama
            --ns-keycloak)          ... namespace to/from which keycloak resources will be installed/uninstalled
                                        default-value: gepardec
        """
}
readonly -f usage_message
[ "$?" -eq "0" ] || return $?

main() {
    local resources=()
    local command=${1}
    local input=($@)
    local all_opts=(${input[@]:1})
    local extravars=""
    local namespace=""
    local print_output=false

    local namespace_hogarama=hogarama
    local namespace_keycloak=gepardec

    if [[ ! " ${COMMANDS[@]} " =~ " ${command} " ]] || [[ ${command} == "help" ]]; then
       usage_message
       exit 0
    fi

     # getopts
    local opts=`getopt -o fqdr:e:w --long force,dryrun,quiet,resource:,ns-hogarama:,ns-keycloak,write-template: -- "${all_opts[@]}"`
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
        --resource | -r)
            resources+=${2,,}
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
        --write-template | -w)
            print_output=true
            shift 1
            ;;
        *)
          break
          ;;
        esac
    done

    ## CHECK FOR SECRETS FILE
    FILE=${TOPLEVEL_DIR}/helm/secrets/secrets.yaml
    if [[ ! -f "$FILE" ]]; then
        echo "#########################################################################"
        echo "$FILE does not exist. Please go to Gepardec GDrive and download the file"
        echo "GEPARDEC-DRIVE/Projekte/Intern/Sonstiges/HOGARAMA-Resources/secrets.yaml"
        echo "#########################################################################"
        usage_message
        exit 1
    fi

    if [[ ! -f "${HOME}/.kube/config" ]]; then
        export KUBECONFIG=/.kube/config
    fi

    ## CHECK LOGGED-IN STATUS ON CLUSTER
    rc="$(oc whoami > /dev/null 2>&1  ;echo $?)"
    if [[ rc -gt 0 ]]; then
        echo "You are not logged in to the OpenShift Cluster, please login and try again"
        exit 1
    fi

    ## REPLACE SECRETS
    if [[ ${command} == "replace-secrets" ]];then
        execute "j2-template ${TOPLEVEL_DIR} helm ${extravars}"
        exit 0
    fi

    ## INSTALL
    if [[ ${command} == "install" ]];then
        helm-install install resources[@]
        exit 0
    fi

    ## UPGRADE
    if [[ ${command} == "upgrade" ]];then
        helm-install upgrade resources[@]
        exit 0
    fi

    ## TEMPLATE
    if [[ "${command}" == "template" ]]; then
        helm-template resources[@] ${print_output}
        exit 0
    fi

    ## UNINSTALL
    if [[ "${command}" == "uninstall" ]]; then
        helm-uninstall resources[@]
        exit 0
    fi

    echo "unexpectedly reached end of script."
    exit 1
}
readonly -f main
[ "$?" -eq "0" ] || return $?

main $@