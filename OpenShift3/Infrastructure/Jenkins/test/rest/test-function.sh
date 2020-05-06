#!/usr/bin/bash

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


