FROM jboss/wildfly:10.0.0.Final
MAINTAINER Gepardec

# Copy and configure artefacts
USER root
ADD inc /opt/jboss
RUN chown -R jboss:jboss /opt/jboss

USER jboss
RUN chmod a+x /opt/jboss/config/* && \
	chmod a+x /opt/jboss/bin/* && \ 
	cd /opt/jboss && \
	ln -s /opt/jboss/bin/jboss7 hogajama && \
	chmod a+x hogajama && \
	cp /opt/jboss/bin/hogajama.rc .hogajamarc && \
	/opt/jboss/bin/hogajama-install.sh


CMD rm -rf /opt/jboss/wildfly/standalone/configuration/standalone_xml_history/current && /opt/jboss/bin/hogajama start && /opt/jboss/bin/hogajama log
