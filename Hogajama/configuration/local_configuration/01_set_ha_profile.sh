StandaloneXml=$JBOSS_HOME/standalone/configuration/standalone.xml
TargetXml=$JBOSS_HOME/standalone/configuration/standalone-full-ha.xml

if [ ! -e ${StandaloneXml}_orig ]; then
	mv $StandaloneXml ${StandaloneXml}_orig
fi

cp $TargetXml $StandaloneXml 
