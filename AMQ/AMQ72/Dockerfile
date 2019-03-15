#docker image build -t hogarama/amq72:latest .
#docker run -it --rm --env AMQ_USER=amq --env AMQ_PASSWORD=amq@123 --env KEYCLOAK_AUTH_SERVER_URL=https://secure-sso-57-hogarama.cloud.itandtel.at/auth/ -p 11883:1883 -p 8161:8161 hogarama/amq72

FROM registry.access.redhat.com/amq-broker-7/amq-broker-72-openshift

LABEL maintainer Egor Erofeev <egor.erofeev@gepardec.com> Christian Salmhofer <christian.salmhofer@gepardec.com>
LABEL Description="AMQ 7.2 docker image with Openshift SSO support" Vendor="Gepardec IT Services GmbH" Version="1.0"

# Set Timezone
ENV TZ=Europe/Vienna

ENV AMQ_USER="amq" 
ENV AMQ_PASSWORD="amq@123" 
#RUN /opt/amq/bin/launch.sh nostart

COPY cfg /home/jboss/cfg
COPY entrypoint.sh /bin/
USER root
RUN chmod +x /bin/entrypoint.sh
USER jboss

ENTRYPOINT ["/bin/entrypoint.sh"]
CMD exec /home/jboss/broker/bin/artemis run