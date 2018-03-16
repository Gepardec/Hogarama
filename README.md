[![Build Status](https://travis-ci.org/Gepardec/Hogarama.svg?branch=master)](https://travis-ci.org/Gepardec/Hogarama) [![Quality Gate](https://sonarcloud.io/api/badges/gate?key=com.gepardec.hogajama:hogajama)](https://sonarcloud.io/dashboard/index/com.gepardec.hogajama:hogajama)

# Hogarama

## What does this project do?
Home and Garden Automation project designed to run on OpenShift platform. The server part is wirten in Java EE and use MongoDb to store sensor data. The data are collected from Raspberry Pi sensors and send to cloud using MQTT protocol. You can view status of all sensors in simple web UI.

## Why is this project useful?
This project can help you learn basics of Openshift application development and keep your plants alive.

## How do I get started?

### Dependencies 
* [git client](https://git-scm.com/)
* [Docker](https://www.docker.com/)
* [oc](https://www.openshift.org/download.html#oc-platforms)

### Prerequisites

You can use Docker or VirtualBox to run local OpenShift. 

#### Docker
* [Docker with IPv6](https://docs.docker.com/engine/userguide/networking/default_network/ipv6/)
 > Start docker daemon with `--ipv6` flag.
* [oc Client Tools](https://www.openshift.org/download.html) 

#### VirtualBox
* [VirtualBox](https://www.virtualbox.org/wiki/Downloads)
* [Minishift](https://docs.openshift.org/latest/minishift/getting-started/installing.html)

### Run it

Start OpenShift in Docker

```
oc cluster up --version=v3.6.1
```

or in VirtualBox

```
minishift start --vm-driver=virtualbox --profile minishift
```

> Default Openshifht accounts are `developer` and `admin`. You can log into both accounts with non empty password.

then and run:

```
git clone https://github.com/Gepardec/Hogarama.git
cd Hogarama/Templates/local_openshift
./startAll.sh
```

### Redeploy everything
```
./recreate.sh
```

### Reimport template
```
oc delete template hogarama-s2i
oc create -f hogarama_template_with_routes_s2i.yaml
```

### Clean all
```
oc delete all --all
oc cluster down
```

## Where can I get more help, if I need it?

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