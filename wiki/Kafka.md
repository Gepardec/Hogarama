# Apache Kafka in Hogarama

This page is intended to gather some information for Kafka in Hogarama. It is for internal use of the Learning Friday
Kafka team.

## Connect to OpenShift Kafka from local host

See https://strimzi.io/blog/2019/04/30/accessing-kafka-part-3/ for explanation.

```
oc login XXXXXXX
oc project hogarama
oc extract secret/gepardec-kafka-cluster-cluster-ca-cert --keys=ca.crt --to=- > ca.crt
keytool -import -trustcacerts -alias root -file ca.crt -keystore kafka-truststore.jks -storepass password -noprompt
kafka-console-consumer --bootstrap-server https://gepardec-kafka-cluster-kafka-bootstrap-hogarama.apps.steppe.gepaplexx.com:443 --topic habarama --consumer-property security.protocol=SSL --consumer-property ssl.truststore.password=password --consumer-property ssl.truststore.location=./kafka-truststore.jks --from-beginning
```

For `kafka-topic` you can create a file `conf`:

```
cat > conf <<EOF
security.protocol=SSL
ssl.truststore.password=password
ssl.truststore.location=./kafka-truststore.jks
EOF
```

and use this e.g with

```
kafka-topics --bootstrap-server https://gepardec-kafka-cluster-kafka-bootstrap-kafka.apps.p.aws.ocp.gepardec.com:443  --command-config conf  --list
```

# Local Testing (Linux, MAC)

Start local Postgres DB and MongoDB:

```
docker run -d  --name my-postgres -p 5432:5432 -e POSTGRES_DB=management -e POSTGRES_USER=hogajama -e POSTGRES_PASSWORD=changepassword postgres
docker run -d  --name my-mongo -p 27017:27017  -e MONGODB_USERNAME=hogajama -e MONGODB_PASSWORD=changepassword -e MONGODB_DATABASE=hogajamadb -e ALLOW_EMPTY_PASSWORD=YES bitnami/mongodb:latest
```

---

Start Kafka depending on your local setup:

**Installed Kafka**

```
zookeeper-server-start config/zookeeper.properties &
sleep 10
kafka-server-start config/server.properties &
```

**Kafka with docker-compose**

see [Local Kafka with Docker](Local-Kafka-with-Docker)

**Setup local Wildfly**

Clone JBoss Setup Scripts and setup JBSS environment (adapt):

```
git clone git@github.com:Gepardec/JBSS.git
./JBSS/bin/setup.sh -i hogajama -r wildfly-23.0.1.Final
echo export ENV_FILE=`pwd`/Hogarama/codereadyworkspace/hogarama_local.env >> ~/.hogajamarc
```

Configure your local Wildfly

```
cd  Hogarama/Hogajama/configuration/local_configuration/
hogajama configure .
```

Use `hogajama help` to find out more

You may stop the unused MDB to prevent warnings in jboss-cli:
`/deployment=hogajama-rs-0.0.1-SNAPSHOT.war/subsystem=ejb3/message-driven-bean=WateringMDB:stop-delivery`

## Run Test-Script

```
Hogarama/Hogajama/raspberry-pi-mocks/raspberry-pi-mocks-cli/mock-cli-kafka.sh
```

## Turn off security

If you have no Keycloak available or you don't want to use security in a development environment you can turn off
security in `hogarama_local.env` with the feature `NOSECURITY`:

```
FEATURES="DEPLOY NOSECURITY"
``` 