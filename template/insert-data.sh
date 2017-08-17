pod=$(oc get pod | grep mongodb | awk '{print $1}')
echo $pod

oc rsync testdata/ mongodb-1-f35hg:/tmp/testdata
oc rsh $pod sh /tmp/testdata/insert-data.sh
