echo 'JAVA_OPTS="$JAVA_OPTS -Djboss.as.reactive.messaging.experimental=true"' >> $JBOSS_HOME/bin/standalone.conf
echo 'set "JAVA_OPTS=%JAVA_OPTS% -Djboss.as.reactive.messaging.experimental=true"' >> $JBOSS_HOME/bin/standalone.conf.bat

