oc rsh hogajama-0 /opt/jboss/wildfly/bin/jboss-cli.sh -c '/subsystem=logging/logger=org.jboss.security:add(level=TRACE)'
oc rsh hogajama-0 /opt/jboss/wildfly/bin/jboss-cli.sh -c '/subsystem=logging/console-handler=CONSOLE:write-attribute(name=level, value=TRACE)'
oc rsh hogajama-0 /opt/jboss/wildfly/bin/jboss-cli.sh -c '/subsystem=logging/logger=io.undertow.request.security:add(level=TRACE)'
oc rsh hogajama-0 /opt/jboss/wildfly/bin/jboss-cli.sh -c '/subsystem=logging/logger=org.wildfly.security:add(level=TRACE)'
