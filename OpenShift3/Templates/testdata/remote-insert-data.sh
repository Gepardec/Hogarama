#!/bin/sh

# Insert test-data on remote mongodb pod.

DIR=`dirname $0`
DIR=${DIR-./}


POD=`oc get pods | grep Running | grep mongodb | cut -d " " -f1`

echo "Copy directory $DIR to pod $POD"
cd $DIR
oc rsync . $POD:/tmp/testdata
oc exec $POD /tmp/testdata/insert-data.sh
