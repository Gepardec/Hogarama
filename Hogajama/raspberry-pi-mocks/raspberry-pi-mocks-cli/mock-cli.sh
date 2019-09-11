#!/bin/sh

DIR=`dirname $0`
DIR=${DIR:=./}

DATA=$DIR/examples

java -jar $DIR/target/hogarama-mock-cli.jar -t $DATA/testData.json -c $DATA/testConfig.prop --delayMs 5000
