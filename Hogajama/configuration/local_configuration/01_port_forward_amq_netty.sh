POD=$(oc get pods -l app=amq  --no-headers=true | grep Running | cut -d" " -f1)
echo oc port-forward $POD 61616
oc port-forward $POD 61616 &