FROM killercentury/jenkins-dind:latest

ADD ./user_config.xml /usr/share/jenkins/users/init/config.xml
ADD ./start.sh /usr/share/jenkins/start.sh
ADD ./initjobs /usr/share/jenkins/initjobs

CMD chmod +x /usr/share/jenkins/start.sh && /usr/share/jenkins/start.sh


