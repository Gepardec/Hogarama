FROM hogarama-build-artefacts as artefacts
WORKDIR /deployments
RUN echo Hello building wildfly:29.0.1.Final-jdk17

FROM quay.io/wildfly/wildfly:29.0.1.Final-jdk17

ARG SETUP=/tmp/setup
ARG ENV_FILE

COPY --from=artefacts /deployments   /tmp/setup
COPY configure_ocp_jboss.sh  $SETUP
COPY local_configuration $SETUP/local_configuration
COPY bin $SETUP/bin
USER 0
RUN yum -y install unzip
RUN chmod a+x /tmp/setup/configure_ocp_jboss.sh /tmp/setup/bin/jboss7
RUN env ENV_FILE=$SETUP/$ENV_FILE $SETUP/configure_ocp_jboss.sh
RUN chgrp -R 0 /opt/jboss/wildfly && chmod g=u /opt/jboss/wildfly

