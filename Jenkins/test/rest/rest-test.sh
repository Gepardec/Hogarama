#!/usr/bin/bash

PATH_OK="https://hogajama-57-hogarama.cloud.itandtel.at/hogajama-rs/rest/sensor/allData?maxNumber=2&sensor=Pflanze"
PATH_FAILURE="https://hogajama-57-hogarama.cloud.itandtel.at/hogajama-rs/rest/sensor/allData?maxNumber=2&sensor=Meowmeow"

function test {
  INPUT=$1
  EXPECTED=$2
  RESULT=$(curl $INPUT)

  echo OUTPUT: $RESULT
  if [[ "$RESULT" =~ $EXPECTED ]] 
  then 
    echo "TEST OK :)"
  else
    echo TEST FAILURE! $INPUT should return $EXPECTED! 
    exit -1
  fi
}

test $PATH_FAILURE \\[\\] 
test $PATH_OK \\[\\{\"id\":.*\\},\\{\"id\":.*\\}\\] 
