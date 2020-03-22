#!/bin/sh

PRG=`basename $0`

KEYCLOAK_AUTH_SERVER_URL="https://secure-sso-57-hogarama.cloud.itandtel.at/auth/"
DEFAULT_DATA_DIR=/tmp/Hogarama_data
BRANCH=master

#####################################################################
##                              print_usage
#####################################################################
print_usage(){
cat <<EOF 1>&2

usage: $PRG [-hSXD] [-s sso_url] [-b branch]

Options:
    S: Don't install SSO-Server locally. Should be used together with option -s
    s sso_url: URL for SSO authenticaton. Only used with option -S
        Default: $KEYCLOAK_AUTH_SERVER_URL
    D: Use persistent data for etcd in $DEFAULT_DATA_DIR. Restart keeps configuration.
    X: Don't Execute, just echo commands
    b branch: Use branch for builds. Default is $BRANCH
    h: This help

Function:
    Installs Hogarama locally with "oc cluster up"

EOF
}

DO_SSO=True
EXEC=""
CLUSTER_UP_OPTIONS=""

######################   Optionen bestimmen ###################

while getopts "DSs:Xb:h" option
do
    case $option in
      D)
        CLUSTER_UP_OPTIONS="$CLUSTER_UP_OPTIONS --host-data-dir $DEFAULT_DATA_DIR --use-existing-config";;
      S)
        DO_SSO=False;;
      s)
        KEYCLOAK_AUTH_SERVER_URL=$OPTARG;;
      b)
        BRANCH=$OPTARG;;
      X)
        EXEC=echo;;
      *)
        print_usage
        exit 1
        ;;
    esac
done

shift `expr $OPTIND - 1`



# set -x

$EXEC oc cluster up $CLUSTER_UP_OPTIONS
$EXEC oc login -u system:admin
$EXEC oc policy add-role-to-user system:image-pusher developer
$EXEC oc create -f alltemplates.yaml -n openshift
$EXEC oc login -u developer -p dev
$EXEC oc new-project hogarama
$EXEC oc create is hogajama

OPENSHIFT_TOKEN=$(oc whoami -t)
HOGARAMA_VARS="OPENSHIFT_AUTH_TOKEN=$OPENSHIFT_TOKEN BRANCH=$BRANCH"

if [ x$DO_SSO != xTrue ]; then
  HOGARAMA_VARS="$HOGARAMA_VARS KEYCLOAK_AUTH_SERVER_URL=$KEYCLOAK_AUTH_SERVER_URL"
fi
$EXEC oc process -f hogarama-amq.yaml BRANCH=$BRANCH | $EXEC oc create -f -
$EXEC oc process -f hogarama-fluentd.yaml BRANCH=$BRANCH | $EXEC oc create -f -
$EXEC oc process -f hogaramaOhneHost.yaml $HOGARAMA_VARS | $EXEC oc create -f -
$EXEC oc process -f hogarama-jboss.yaml $HOGARAMA_VARS | $EXEC oc create -f -
$EXEC oc process -f hogarama-postgres.yaml | $EXEC oc create -f -
$EXEC ./install-pg.sh

if [ x$DO_SSO = xTrue ]; then
	$EXEC oc process -f ../sso/sso-app-secret.yaml | $EXEC oc create -f -
	$EXEC oc process -f ../sso/sso-service-account.yaml | $EXEC oc create -f -
	$EXEC oc process -f ../sso/sso.yaml | $EXEC oc create -f -
fi
