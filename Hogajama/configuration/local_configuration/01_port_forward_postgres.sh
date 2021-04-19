POD=$(oc get pods -l app.kubernetes.io/instance=postgresql  --no-headers=true | grep Running | cut -d" " -f1)
oc port-forward $POD 5432 &

