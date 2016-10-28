# Hogajama

Hogajama is the Java/Maven part of the Hogarama Project. It is a Java EE application supposed to be deployed in Openshift(r) environment that provides backend services for R3.14 and frontend for end user.

## Prerequisites

 * wildfly-10.0.0.Final.zip in the same folder as the root pom.xml
 * Running docker daemon

## Build
mvn clean install &&  mvn -f hogajama-pkg/pom.xml -P docker clean install

## Run
docker run --rm -it -p 8080:8080 -p 9990:9990 hogajama

## URL
http://localhost:8080/hogajama-frontend
