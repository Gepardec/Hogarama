Hogajama
##########

Hogajama is the Java/Maven part of the Hogarama Project.
It is a Java EE application supposed to be deployed in Openshift(r) environment that provides backend services for R3.14 and frontend for end user.

Local Development
-------------------

You need:

 * Java 8
 * EAP7
 * MongoDB
 * oc client / docker (optional)

Environment variables
-----------------------

You have to set environment variable MONGODB_PW (set it an log in to your computer again.)

Configure EAP7
----------------

/system-property=mongo.host:add(value=<MONGODB_URL>)
/subsystem=undertow/server=default-server/host=default-host/location=\/:remove()

MongoDB
----------

Install it or you can get one with "oc cluster up".
Just execute ../template/local_openshift/startAll.sh and wait.
With "oc get pods" there should then be:
mongodb-1-*****      1/1       Running
Then forward the pods port with:

oc port-forward mongodb-1-***** 27017

You should then be able to connect to the MongoDB with <MONGODB_URL>=localhost


Build
---------

mvn clean install

Deployment
-----------

hogajama-frontend-*.war
hogajama-rs-*.war

URL
------

http://localhost:8080/

REST-Services
--------------

http://localhost:8080/hogajama-rs/rest/*
