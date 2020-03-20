#!/bin/bash

mkdir -p /var/lib/jenkins/users/init
cp /usr/share/jenkins/users/init/config.xml /var/lib/jenkins/users/init

/usr/bin/supervisord > /tmp/jenkinsout 2>&1 &

echo "waiting until jenkins starts"
sleep 3
export logfile=`ls /var/log/supervisor/jenkins-stderr*`

while true
do
	chk=`grep "Jenkins is fully up and running" $logfile`

        if [ -z "$chk" ]; then
                echo -n "."
                sleep 3
        else
                echo "jenkins started. Just wait a bit until it wakes up."
		sleep 5
                break
        fi

done

function waitUntilInstalled {
	> $logfile
	echo "Installing $1"
	while true
	do
	
		chk=`grep "Installation successful" $logfile`
		if [ -z "$chk" ]; then
			echo -n "."
			sleep 1
		else 
			echo ""			
			echo "Installation successful $1"		
			break
		fi
	done
}

if [ ! -f /var/lib/jenkins/configured.ok ]; then
	wget http://localhost:8080/jnlpJars/jenkins-cli.jar -P /usr/share/jenkins/
	 
	java -jar /usr/share/jenkins/jenkins-cli.jar -s http://localhost:8080/ install-plugin copyartifact -deploy --username=init --password=init123 > /tmp/jenkinsout 2>&1 &
	waitUntilInstalled "copy artifact plugin"
	java -jar /usr/share/jenkins/jenkins-cli.jar -s http://localhost:8080/ install-plugin greenballs -deploy  --username=init --password=init123 > /tmp/jenkinsout 2>&1 &
	waitUntilInstalled "green balls plugin"
	java -jar /usr/share/jenkins/jenkins-cli.jar -s http://localhost:8080/ install-plugin parameterized-trigger -deploy  --username=init --password=init123 > /tmp/jenkinsout 2>&1 &
	waitUntilInstalled "parametrized trigger plugin"

	for f in `ls /usr/share/jenkins/initjobs`; do
		echo "creating job $f"
		java -jar /usr/share/jenkins/jenkins-cli.jar -s http://localhost:8080/ create-job $f < /usr/share/jenkins/initjobs/$f/config.xml --username=init --password=init123
	done

	touch /var/lib/jenkins/configured.ok
fi

tail -f /var/log/supervisor/supervisord.log
