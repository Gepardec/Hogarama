set CURRENT_DIRECTORY=%~dp0
set HOGAJAMA_BASE=%CURRENT_DIRECTORY%/../
set JBOSS_RELEASE_NAME=jboss-eap-7.0.0
set JBOSS_HOME=%CURRENT_DIRECTORY%/jboss/
set DOWNLOADS=%USERPROFILE%/Downloads

docker run ^
    -v %JBOSS_HOME%:/install/jboss/ ^
    -v %HOGAJAMA_BASE%:/install/hogajama_base/ ^
    -v %DOWNLOADS%:/install/Downloads ^
    -e JBOSS_RELEASE_NAME=%JBOSS_RELEASE_NAME% ^
    -it gepardec/jbss:java8 jbss configure /install/hogajama_base/configuration/local_configuration
