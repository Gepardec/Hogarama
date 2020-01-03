#!/bin/bash

CREATE_USER=true
WILDFLY_USERNAME="hogarama"
WILDFLY_PASSWORD="hogarama"
WILDFLY_PORT=9990

SCRIPT_PATH="/usr/local/bin"
INSTALL_PATH="/mnt"

cd $SCRIPT_PATH
echo ">> Using pwd: $SCRIPT_PATH"
echo $(ls -A  $INSTALL_PATH)

# 1. Download & Extract WildFly if it is an empty dir
if [ -z "$(ls -A  $INSTALL_PATH)" ]; then
    echo ">> Mounted dir is empty. Installing Wildfly 17.0.1"
    
    tempZipFile=$INSTALL_PATH/wildfly.zip

    curl https://download.jboss.org/wildfly/17.0.1.Final/wildfly-17.0.1.Final.zip --output $tempZipFile
    unzip $tempZipFile -d $INSTALL_PATH
    
    rm -f $tempZipFile
    WILDFLY_FOLDER="$INSTALL_PATH/$(ls -1 $INSTALL_PATH)"

    mv ${WILDFLY_FOLDER}/* $INSTALL_PATH
    rm -rf ${WILDFLY_FOLDER}
else
    echo ">> Mounted dir is filled. Using that server"
    # Shutdown the server, if running
    $INSTALL_PATH/bin/jboss-cli.sh --connect "shutdown"
fi

# 2. Extract keycloak & postgres zip in modules
echo ">> Extracting keycloak and postgres modules into the install folder"

unzip keycloak.zip -d $INSTALL_PATH/modules
unzip postgres.zip -d $INSTALL_PATH/modules

# 3. Configure Wildfly
echo ">> Configuring user, profile and conf"
# Add new user
if [ CREATE_USER = true ]; then
    $INSTALL_PATH/bin/add-user.sh -u $WILDFLY_USERNAME -p $WILDFLY_PASSWORD ManagementRealm
fi

# Use full-ha profile
standaloneXml=$INSTALL_PATH/standalone/configuration/standalone.xml
targetXml=$INSTALL_PATH/standalone/configuration/standalone-full-ha.xml

if [ ! -e ${standaloneXml}_orig ]; then
	mv $standaloneXml ${standaloneXml}_orig
fi

cp $targetXml $standaloneXml 

# Configure Env vars
confFile=$INSTALL_PATH/bin/standalone.conf
#perl -wpi.bak -e 's/-Xms1303m -Xmx1303m/-Xms128m -Xmx128m/' $INSTALL_PATH/bin/standalone.conf

if ! grep -q "KEYCLOAK_AUTH_SERVER_URL" "$confFile"; then
    echo ">> KEYCLOAK_AUTH_SERVER_URL does not exist in $confFile -> Adding now"
    echo "export KEYCLOAK_AUTH_SERVER_URL=https://secure-sso-57-hogarama.cloud.itandtel.at/auth/" >> $confFile
fi
if ! grep -q "JAVA_OPTS=\"\$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,address=8787,server=y,suspend=n\" #" "$confFile"; then
    echo ">> JAVA_OPTS does not exist in $confFile -> Adding now"
    echo 'JAVA_OPTS="$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,address=8787,server=y,suspend=n" #' >> $confFile
fi
if ! grep -q "MONGODB_PW" "$confFile"; then
    echo ">> MONGODB_PW does not exist in $confFile -> Adding now"
    echo "export MONGODB_PW=hogajama@mongodb" >> $confFile
fi
if ! grep -q "AMQ_HOST" "$confFile"; then
    echo ">> AMQ_HOST does not exist in $confFile -> Adding now"
    echo "export AMQ_HOST=https://broker-amq-mqtt-ssl-hogarama.127.0.0.1.nip.io" >> $confFile
fi

# 4. Start Wildfly in background
echo ">> Starting Wildfly"

$INSTALL_PATH/bin/standalone.sh &

timeout=120
while true #check if the port is ready
do  
    if netstat -an | grep $WILDFLY_PORT | grep LISTEN; then
        break
    fi
    timeout=timeout-1
    if [ timeout = 0 ]; then
        echo ">> Error: Nothing is running on port $WILDFLY_PORT"
        exit 1
    fi
    sleep 1
done
timeout=10
while true  #check if the server start success
do  
    if $INSTALL_PATH/bin/jboss-cli.sh --connect command=':read-attribute(name=server-state)' | grep running; then
        break
    fi
    timeout=timeout-1
    if [ timeout = 0 ]; then
        echo ">> Error: Cant retrieve status 'RUNNING' from WildFly"
        exit 1
    fi
    sleep 1
done

echo ">> Wildfly Running"

# 5. Install postgres & keycloak via CLI
echo ">> Installing Postgres & Keycloak modules in standalone"
$INSTALL_PATH/bin/jboss-cli.sh --connect --file=$SCRIPT_PATH/config.cli

# Shutdown
echo ">> Shutting Wildfly down"
$INSTALL_PATH/bin/jboss-cli.sh --connect "shutdown"
echo ">> Your Wildfly is now ready to use with the Hogarama project"