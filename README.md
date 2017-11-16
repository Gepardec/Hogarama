[![Build Status](https://travis-ci.org/Gepardec/Hogarama.svg?branch=master)](https://travis-ci.org/Gepardec/Hogarama) [![Quality Gate](https://sonarcloud.io/api/badges/gate?key=com.gepardec.hogajama:hogajama)](https://sonarcloud.io/dashboard/index/com.gepardec.hogajama:hogajama)

# Hogarama
Home and Garden Automation

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
