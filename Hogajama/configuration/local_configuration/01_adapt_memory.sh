perl -wpi.bak -e 's/-Xms1303m -Xmx1303m/-Xms128m -Xmx128m/' $JBOSS_HOME/bin/standalone.conf
