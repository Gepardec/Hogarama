#!/bin/sh

DIR=`dirname $0`
DIR=${DIR:=./}

DATA=$DIR/examples

java -jar $DIR/target/hogarama-mock-cli.jar -b rest -t $DATA/testData_small.json -c $DATA/restTestConfig.prop --delayMs 5000