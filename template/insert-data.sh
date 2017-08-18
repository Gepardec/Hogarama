pod=$(oc get pod | grep mongodb | awk '{print $1}')
echo $pod

oc rsync testdata/ $pod:/tmp/testdata
oc rsh $pod sh /tmp/testdata/insert-data.sh
