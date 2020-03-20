#!/bin/sh

oc delete all -l template=amq-management

oc create -f - <<EOF
{
    "kind": "List",
    "apiVersion": "v1",
    "metadata": {},
    "items": [
        {
            "kind": "Route",
            "apiVersion": "v1",
            "metadata": {
                "name": "amq-management",
                "creationTimestamp": null,
                "labels": {
                    "app": "amq-management",
                    "template": "amq-management"
                },
                "annotations": {
                    "openshift.io/host.generated": "true"
                }
            },
            "spec": {
                "to": {
                    "kind": "Service",
                    "name": "amq-management",
                    "weight": 100
                },
                "port": {
                    "targetPort": "8161-tcp"
                },
                "tls": {
                    "termination": "passthrough"
                },
                "wildcardPolicy": "None"
            }
        },
        {
            "kind": "Service",
            "apiVersion": "v1",
            "metadata": {
                "name": "amq-management",
                "creationTimestamp": null,
                "labels": {
                    "app": "amq-management",
                    "template": "amq-management"
                }
            },
            "spec": {
                "ports": [
                    {
                        "name": "8161-tcp",
                        "protocol": "TCP",
                        "port": 8161,
                        "targetPort": 8161
                    }
                ],
                "selector": {
                    "deploymentConfig": "broker-amq"
                },
                "type": "ClusterIP",
                "sessionAffinity": "None"
            }
        }
    ]
}
EOF
