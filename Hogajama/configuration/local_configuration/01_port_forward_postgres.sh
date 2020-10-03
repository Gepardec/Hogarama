POD=$(oc get pods --no-headers=true | grep postgres | grep Running | cut -d" " -f1)
oc port-forward $POD 5432 &

