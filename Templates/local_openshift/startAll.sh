#!/bin/sh

PRG=`basename $0`

KEYCLOAK_AUTH_SERVER_URL="https://secure-sso-57-hogarama.cloud.itandtel.at/auth/"

#####################################################################
##                              print_usage
#####################################################################
print_usage(){
cat <<EOF 1>&2

usage: $PRG [-hSX] [-s sso_url] 

Options:
    S: Don't install SSO-Server locally. Should be used together with option -s
    s sso_url: URL for SSO authenticaton. Only used with option -S 
        Default: $KEYCLOAK_AUTH_SERVER_URL
    X: Don't Execute, just echo commands
    h: This help

Function:
    Installs Hogarama locally with "oc cluster up"

EOF
}

DO_SSO=True
EXEC=""

######################   Optionen bestimmen ###################

while getopts "Ss:Xh" option
do
    case $option in
      S)
        DO_SSO=False;;
      s)
        KEYCLOAK_AUTH_SERVER_URL=$OPTARG;;
      X)
        EXEC=echo;;
      *)
        print_usage
        exit 1
        ;;
    esac
done

shift `expr $OPTIND - 1`



set -x

$EXEC oc cluster up --skip-registry-check=true
$EXEC oc login -u system:admin
$EXEC oc policy add-role-to-user system:image-pusher developer
$EXEC oc create -f alltemplates.yaml -n openshift
$EXEC oc login -u developer
$EXEC oc new-project hogarama
$EXEC oc create is hogajama
$EXEC oc create is fluentd

OPENSHIFT_TOKEN=$(oc whoami -t)
HOGARAMA_VARS="OPENSHIFT_AUTH_TOKEN=$OPENSHIFT_TOKEN"

if [ x$DO_SSO != xTrue ]; then
  HOGARAMA_VARS="$HOGARAMA_VARS KEYCLOAK_AUTH_SERVER_URL=$KEYCLOAK_AUTH_SERVER_URL"
fi
$EXEC oc process -f hogaramaOhneHost.yaml $HOGARAMA_VARS | $EXEC oc create -f -

if [ x$DO_SSO = xTrue ]; then
	$EXEC oc process -f ../sso/sso-app-secret.yaml | $EXEC oc create -f -
	$EXEC oc process -f ../sso/sso-service-account.yaml | $EXEC oc create -f -
	$EXEC oc process -f ../sso/sso.yaml | $EXEC oc create -f -
fi
