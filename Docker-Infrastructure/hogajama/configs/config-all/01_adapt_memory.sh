touch $JBOSS_HOME/bin/standalone.conf.bak
perl -wpi.bak -e 's/-Xms1303m -Xmx1303m/-Xms256m -Xmx256m/' $JBOSS_HOME/bin/standalone.conf
touch $JBOSS_HOME/bin/standalone.conf.bat.bak
perl -wpi.bak -e 's/-Xms1G -Xmx1G/-Xms256m -Xmx256m/' $JBOSS_HOME/bin/standalone.conf.bat

