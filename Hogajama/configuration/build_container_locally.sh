#!/bin/sh

PRG=`readlink -e $0`
DIR=`dirname $PRG`
HOGARAMA_HOME=$DIR/../../


if [ ! -f "$ENV_FILE" ]; then
	ENV_FILE=$HOGARAMA_HOME/codereadyworkspace/hogarama_dev.env 
fi
if [ ! -f "$ENV_FILE" ]; then
    echo "ERROR: File $ENV_FILE doesn't exist! Something is wrong" 1>&2
    exit 1
fi
echo "Using $ENV_FILE as environment"

cp $ENV_FILE local_configuration/tmp_environment
docker build -t hogajama-run $DIR --build-arg ENV_FILE=tmp_environment
