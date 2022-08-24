#!/bin/bash

echo "This works only when you switch the KEYCLOAK authentication for dummy-security authentication"

URL=http://localhost:8080/hogajama-rs/rest/unitmanagement
AUTH="Authorization: Dummy $(echo -ne '{"name":"Dummy","email":"dummy@nowhere","givenName":"Dummy","familyName":"Franz"}' | base64 -w 0)"
CURL="curl -i -w \"\n>>> Response code: %{http_code}\n\" -s -H \"$AUTH\""

testcall () {
CMD="$CURL -H \"Accept: application/json\" -H \"Content-Type: application/json\" -X $2 $URL/$3 $4"
cat <<EOF

============================================
Test '$1 with *DUMMY* user'
............................................
Calling $CMD
............................................
EOF
eval "$CMD"
cat <<EOF
============================================

EOF
}

testcall "read user" "GET" "user"
testcall "read watering rules" "GET" "rule"
testcall "write watering rules" "PUT" "rule" "-d '{\"name\":\"Pflanze_Pumpe50\",\"sensorId\":10,\"actorId\":10,\"unitId\":20,\"waterDuration\":20,\"lowWater\":0.5}'"