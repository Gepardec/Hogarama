[![Build Status](https://travis-ci.org/Gepardec/Hogarama.svg?branch=master)](https://travis-ci.org/Gepardec/Hogarama) [![Quality Gate](https://sonarcloud.io/api/badges/gate?key=com.gepardec.hogajama:hogajama)](https://sonarcloud.io/dashboard/index/com.gepardec.hogajama:hogajama)

# Hogarama
Home and Garden Automation

## Install

### Dependencies 
* [git client](https://git-scm.com/)
* [Docker](https://www.docker.com/)
* [oc](https://www.openshift.org/download.html#oc-platforms)

## Prerequisites
* [Docker with IPv6](https://docs.docker.com/engine/userguide/networking/default_network/ipv6/)
 > Start docker daemon with `--ipv6` flag.

### Run it
```
git clone -b playground https://github.com/Gepardec/Hogarama.git
cd Hogarama
oc cluster up
cd Templates/local_openshift
oc login -u system:admin
oc create -n openshift -f red_hat_middleware_imagestreams.yml
oc login -u developer
oc create serviceaccount eap-service-account
oc policy add-role-to-user view system:serviceaccount:$(oc project -q):eap-service-account
oc create -f hogarama_template_with_routes_s2i.yaml
oc process hogarama-s2i | oc create -f -
oc patch dc/hogajama-broker-amq --type=json \
-p '[{"op": "add", "path": "/spec/template/spec/serviceAccountName", "value": "eap-service-account"}]'
```

### Reimport template
```
oc delete template hogarama-s2i
oc create -f template/hogarama_template_with_routes_s2i.yaml
```

### Clean all
```
oc delete all --all
oc cluster down
```



## Habarama
Habarama is the OpenHab/Raspberry part of the Hogarama Project.
It uses OpenHab to connect the Raspberry with each other and for collecting data from the sensors.

[More...](Habarama/README.md)

## Hogajama
Hogajama is the Java/Maven part of the Hogarama Project. It is a Java EE application supposed to be deployed in Openshift(r) environment that provides backend services for R3.14 and frontend for end user.

[More...](Hogajama/README.md)

## Hogarama-Jenkins
Jenkins docker image project. This docker image containing Jenkins for building and deployment of Hogarama project. The image provides Docker inside itself, which allows to run any Docker container in your Jenkins build script.

[More...](Jenkins/README.md)
