#!/bin/bash

VERSION=$1
PLUGINS=("https://repo.maven.apache.org/maven2/org/apache/camel/kafkaconnector/camel-activemq-kafka-connector/1.0.0/camel-activemq-kafka-connector-1.0.0-package.tar.gz"
         "https://repo.maven.apache.org/maven2/org/apache/camel/kafkaconnector/camel-sjms2-kafka-connector/1.0.0/camel-sjms2-kafka-connector-1.0.0-package.tar.gz")


if [ -x "VERSION" ]; then
  echo "Version not set. Defaulting to 1.0.0"
  VERSION=1.0.0
fi

echo "Refreshing plugins"

find my-plugins/ -maxdepth 1 -type f  -delete

for plugin in "${PLUGINS[@]}";
do
  wget $plugin -P my-plugins/
done

find my-plugins/ -maxdepth 1 -type f -exec tar -xzvf {} --directory my-plugins/  \;
rm my-plugins/*.tar.gz

echo "Building gepardec kafka-connect-cluster image with Version: $VERSION"
docker build -t gepardec/kafka-connect-cluster:${VERSION} .
echo "Pushing gepardec kafka-connect-cluster image with Version: $VERSION"
docker push gepardec/kafka-connect-cluster:${VERSION}
