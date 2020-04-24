HOGAJAMA_BASE=`pwd`/../
JBOSS_RELEASE_NAME=jboss-eap-7.0.0
JBOSS_HOME=`pwd`/jboss/
DOWNLOADS=$HOME/Downloads

docker run \
    -v $JBOSS_HOME:/install/jboss/ \
    -v $HOGAJAMA_BASE:/install/hogajama_base/ \
    -v $DOWNLOADS:/install/Downloads \
    -e JBOSS_RELEASE_NAME=$JBOSS_RELEASE_NAME \
    -it gepardec/jbss:java8 jbss configure /install/hogajama_base/configuration/local_configuration

