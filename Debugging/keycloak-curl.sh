#!/bin/bash

# set -x
if [ $# -ne 5 ]; then
  echo 1>&2 "Usage: . $0 hostname realm username clientid"
  echo 1>&2 "  options:"
  echo 1>&2 "    hostname: localhost:8081"
  echo 1>&2 "    realm:keycloak-demo"
  echo 1>&2 "    clientid:demo"
  echo 1>&2 "    For verify ssl: use 'y' (otherwise it will send curl post with --insecure)"
  
  return
fi

HOSTNAME=$1
REALM_NAME=$2
USERNAME=$3
CLIENT_ID=$4
SECURE=$5
CLIENT_SECRET=$6
PASSWORD=$7



KEYCLOAK_URL=https://$HOSTNAME/auth/realms/$REALM_NAME/protocol/openid-connect/token



echo "Using Keycloak: $KEYCLOAK_URL"
echo "realm: $REALM_NAME"
echo "client-id: $CLIENT_ID"
echo "username: $USERNAME"
echo "secure: $SECURE"


if [[ $SECURE = 'y' ]]; then
	INSECURE=
else 
	INSECURE=--insecure
fi

if [[ ! x$CLIENT_SECRET = 'x' ]]; then
	CLIENT_SECRET="-d client_secret=$CLIENT_SECRET"
fi

if [[ x$PASSWORD = 'x' ]]; then
	echo -n Password: 
	read -s PASSWORD
fi


export TOKEN=$(curl -sX POST "$KEYCLOAK_URL" "$INSECURE" \
 -H "Content-Type: application/x-www-form-urlencoded" \
 -d "username=$USERNAME" \
 -d "password=$PASSWORD" \
 -d 'grant_type=password' \
 $CLIENT_SECRET \
 -d "client_id=$CLIENT_ID" | jq -r '.access_token')

echo $TOKEN 

if [[ $(echo $TOKEN) != 'null' ]]; then
	export KEYCLOAK_TOKEN=$TOKEN
fi
