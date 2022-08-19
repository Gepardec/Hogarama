#!/bin/bash

PRG=`basename $0`

KEYCLOAK_AUTH_SERVER_URL="https://secure-sso-57-hogarama.cloud.itandtel.at/auth"
DEFAULT_DATA_DIR=/tmp/Hogarama_data
BRANCH=master

#####################################################################
##                              print_usage
#####################################################################
print_usage(){
cat <<EOF 1>&2
Installs Hogarama components. If not one of Options -cjamfps are used, all components are installed.
usage: $PRG [-hXD] [-S sso_url] [-b branch] [-cjamfps]

Options:
    D: Use persistent data for etcd in $DEFAULT_DATA_DIR. Restart keeps configuration.
    S sso_url: URL for SSO authenticaton. Only used when SSO is not installed locally.
        Default: $KEYCLOAK_AUTH_SERVER_URL
    X: Don't Execute, just echo commands
    b branch: Use branch for builds. Default is $BRANCH
    c: Do a "cluster up"
    j: Install JBoss
    a: Install AMQ
    f: Install Fluentd
    m: Install MongoDB
    p: Install Postgres
    s: Install SSO-Server locally. See also -S
    h: This help

Function:
    Installs Hogarama locally with "oc cluster up"

EOF
}

EXEC=""
CLUSTER_UP_OPTIONS=""
ALL=True
DO_CLUSTER_UP=False
DO_JBOSS=False
DO_AMQ=False
DO_MONGO=False
DO_FLUENTD=False
DO_POSTGRES=False
DO_SSO=False

######################   Optionen bestimmen ###################

while getopts "cjamfpDsS:Xb:h" option
do
    case $option in
      D)
        CLUSTER_UP_OPTIONS="$CLUSTER_UP_OPTIONS --host-data-dir $DEFAULT_DATA_DIR --use-existing-config";;
      S)
        KEYCLOAK_AUTH_SERVER_URL=$OPTARG;;
      b)
        BRANCH=$OPTARG;;
      X)
        EXEC=echo;;
      a)
		DO_AMQ=True; ALL=False;;
      c)
		DO_CLUSTER_UP=True; ALL=False;;
      f)
		DO_FLUENTD=True; ALL=False;;
      j)
		DO_JBOSS=True; ALL=False;;
      m)
		DO_MONGO=True; ALL=False;;
      p)
		DO_POSTGRES=True; ALL=False;;
      s)
		DO_SSO=True; ALL=False;;
      *)
        print_usage
        exit 1
        ;;
    esac
done

shift `expr $OPTIND - 1`



# set -x

if [ x$DO_CLUSTER_UP = xTrue ] || [ x$ALL = xTrue ]; then
  $EXEC oc cluster up $CLUSTER_UP_OPTIONS
  $EXEC oc login -u system:admin
  $EXEC oc policy add-role-to-user system:image-pusher developer
  $EXEC oc create -f alltemplates.yaml -n openshift
  $EXEC oc login -u developer -p dev
  $EXEC oc new-project hogarama
  $EXEC oc create is hogajama
fi

OPENSHIFT_TOKEN=$(oc whoami -t)
HOGARAMA_VARS="OPENSHIFT_AUTH_TOKEN=$OPENSHIFT_TOKEN BRANCH=$BRANCH"

if [ x$DO_SSO != xTrue ]; then
  HOGARAMA_VARS="$HOGARAMA_VARS KEYCLOAK_AUTH_SERVER_URL=$KEYCLOAK_AUTH_SERVER_URL"
fi

if [ x$DO_AMQ = xTrue ] || [ x$ALL = xTrue ]; then
  $EXEC oc process -f hogarama-amq.yaml BRANCH=$BRANCH | $EXEC oc create -f -
fi
if [ x$DO_FLUENTD = xTrue ] || [ x$ALL = xTrue ]; then
  $EXEC oc process -f hogarama-fluentd.yaml BRANCH=$BRANCH | $EXEC oc create -f -
fi

if [ x$DO_MONGO = xTrue ] || [ x$ALL = xTrue ]; then
  $EXEC oc process -f hogaramaOhneHost.yaml $HOGARAMA_VARS | $EXEC oc create -f -
fi

if [ x$DO_JBOSS = xTrue ] || [ x$ALL = xTrue ]; then
  $EXEC oc process -f hogarama-jboss.yaml $HOGARAMA_VARS | $EXEC oc create -f -
fi

if [ x$DO_POSTGRES = xTrue ] || [ x$ALL = xTrue ]; then
  $EXEC oc process -f hogarama-postgres.yaml | $EXEC oc create -f -
  $EXEC ./install-pg.sh
fi

if [ x$DO_SSO = xTrue ] || [ x$ALL = xTrue ]; then
	$EXEC oc process -f ../sso/sso-app-secret.yaml | $EXEC oc create -f -
	$EXEC oc process -f ../sso/sso-service-account.yaml | $EXEC oc create -f -
	$EXEC oc process -f ../sso/sso.yaml | $EXEC oc create -f -
fi
