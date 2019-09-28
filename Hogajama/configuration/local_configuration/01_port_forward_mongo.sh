MONGODB=$(oc get pods -l name=mongodb --no-headers=true | grep Running | cut -d" " -f1)
oc port-forward $MONGODB 27017 &

