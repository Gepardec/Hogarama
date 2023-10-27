#!/bin/sh

oc delete `oc get all -o name`
oc delete `oc get pvc -o name`
oc delete `oc get ingress -o name`
oc delete `oc get secret -o name | grep hoga`
oc delete `oc get secret -o name | grep amq`
oc delete `oc get secret -o name | grep mongodb`
