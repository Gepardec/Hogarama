FROM ubuntu:latest

RUN apt-get update \
    && apt-get install -y unzip \
    && apt-get install -y curl \
    && apt-get install -y default-jre

RUN apt-get install -y net-tools

COPY entrypoint.sh /usr/local/bin/entrypoint.sh
COPY config.cli /usr/local/bin/config.cli
COPY postgres.zip /usr/local/bin/postgres.zip
COPY keycloak.zip /usr/local/bin/keycloak.zip

ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]

