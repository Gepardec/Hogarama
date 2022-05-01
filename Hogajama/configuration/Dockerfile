FROM hogarama-build-artefacts as artefacts
WORKDIR /deployments
RUN echo Hello

FROM quay.io/wildfly/wildfly:23.0.2.Final
COPY --from=artefacts /deployments   /tmp/setup/deployments
COPY configure_ocp_jboss.sh  /tmp/setup/
COPY local_configuration /tmp/setup/local_configuration
COPY bin /tmp/setup/bin
USER 0
RUN chmod a+x /tmp/setup/configure_ocp_jboss.sh /tmp/setup/bin/jboss7
RUN { env ENV_FILE=tmp_environment /tmp/setup/configure_ocp_jboss.sh 2>&1; RTC=$?; }; test $RTC || exit $RTC;  
RUN chgrp -R 0 /opt/jboss/wildfly && chmod g=u /opt/jboss/wildfly

