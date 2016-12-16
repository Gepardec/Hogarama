# Hogajama

Hogajama is the Java/Maven part of the Hogarama Project. It is a Java EE application supposed to be deployed in Openshift(r) environment that provides backend services for R3.14 and frontend for end user.

## Prerequisites

 * Running docker daemon

## Build
mvn clean install -P docker

## Run
For test : docker run --rm -it -p 8080:8080 -p 9990:9990 hogajama
For prod : docker run -p 8080:8080 -p 9990:9990 -d hogajama

## Stop
docker rm $(docker stop $(docker ps -a -q --filter ancestor=hogajama --format="{{.ID}}"))

## URL
http://localhost:8080/hogajama-frontend

## REST-Services
http://localhost:8080/hogajama-rs/rest/helloworld
