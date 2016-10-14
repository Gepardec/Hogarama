# Hogajama

Hogajama is the Java/Maven part of the Hogarama Project. It is a Java EE application supposed to be deployed in Openshift(r) environment that provides backend services for R3.14 and frontend for end user.

## Prerequisites

 * wildfly-10.0.0.Final.zip near root pom.xml

## Build
mvn clear install -p docker

## Run
docker run --rm -it -p 8080:8080 -p 9990:9990 hogajama
