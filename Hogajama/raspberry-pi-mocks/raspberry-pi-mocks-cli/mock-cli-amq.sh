#!/bin/sh

DIR=`dirname $0`
DIR=${DIR:=./}

DATA=$DIR/examples

java -jar $DIR/target/hogarama-mock-cli.jar -t $DATA/testData.json -c $DATA/amqTestConfig.prop --delayMs 5000
