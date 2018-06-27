MONGODB=$(oc get pods -o=custom-columns=NAME:.metadata.name -l name=mongodb --no-headers=true)
oc port-forward $MONGODB 27017 &

