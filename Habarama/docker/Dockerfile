FROM ubuntu:16.04
# FROM multiarch/ubuntu-debootstrap:armhf-wily
MAINTAINER Gepardec

# Install add-apt-repository, unzip
RUN apt-get update && \
    apt-get install -y software-properties-common unzip && \
    rm -rf /var/lib/apt/lists/* 

# Install Java
RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
    add-apt-repository -y ppa:webupd8team/java && \
    apt-get update && \
    apt-get install -y oracle-java8-installer nano && \
    rm -rf /var/lib/apt/lists/* && \
    rm -rf /var/cache/oracle-jdk8-installer

# Install OpenHab
RUN wget https://bintray.com/artifact/download/openhab/bin/distribution-1.8.3-runtime.zip && \
    wget https://bintray.com/artifact/download/openhab/bin/distribution-1.8.3-addons.zip && \
    unzip distribution-1.8.3-runtime.zip -d OpenHab && \
    unzip distribution-1.8.3-addons.zip -d OpenHab/all_addons

# Configure OpenHab
COPY third_party/addons/org.openhab.binding.systeminfo-1.8.3.jar OpenHab/addons/
COPY third_party/addons/org.openhab.binding.mqtt-1.8.3.jar OpenHab/addons/
#COPY third_party/addons/ OpenHab/addons/

COPY third_party/addons/org.openhab.io.transport.mqtt-1.8.3.jar /OpenHab/server/plugins/org.openhab.io.transport.mqtt_1.8.3.jar

COPY third_party/libsigar-amd64-linux.so OpenHab/lib/libsigar-amd64-linux.so
# COPY third_party/libsigar-arm-linux.so OpenHab/lib/libsigar-arm-linux.so

# Copy configurations
COPY example/mqtt/node1 /OpenHab/configurations
COPY etc/default/openhab2 /etc/default/openhab2
COPY etc/ssl/private/client.ts /etc/ssl/private/client.ts

# Copy patched start scripts + transform script
COPY OpenHab/ /OpenHab
# Make them executable
RUN chmod +x /OpenHab/start_patched.sh /OpenHab/start_patched_debug.sh /OpenHab/transform.sh

VOLUME ["/OpenHab/configurations"]
EXPOSE 8080

#CMD /OpenHab/start.sh
# Use patched start script to source /etc/default/openhab2
CMD /OpenHab/start_patched.sh
