POD=$(oc get pods -l app=amq  --no-headers=true | grep Running | cut -d" " -f1)
echo oc port-forward $POD 8883
oc port-forward $POD 8883 &
