MONGODB=$(oc get pods -l app.kubernetes.io/component=mongodb --no-headers=true | grep Running | cut -d" " -f1)
oc port-forward $MONGODB 27017 &

