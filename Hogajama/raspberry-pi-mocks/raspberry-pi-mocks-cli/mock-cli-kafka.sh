#!/bin/sh

DIR=`dirname $0`
DIR=${DIR:=./}

DATA=$DIR/examples

java -jar $DIR/target/hogarama-mock-cli.jar -b kafka -t $DATA/testData.json -c $DATA/kafkaTestConfig.prop --delayMs 5000
