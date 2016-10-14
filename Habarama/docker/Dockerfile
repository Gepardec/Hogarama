FROM ubuntu:16.04
MAINTAINER Gepardec

# Install add-apt-repository, unzip
RUN apt-get update && \
    apt-get install -y software-properties-common unzip && \
    rm -rf /var/lib/apt/lists/* 

# Install Java
RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
    add-apt-repository -y ppa:webupd8team/java && \
    apt-get update && \
    apt-get install -y oracle-java8-installer && \
    rm -rf /var/lib/apt/lists/* && \
    rm -rf /var/cache/oracle-jdk8-installer

# Install OpenHab
RUN wget https://bintray.com/artifact/download/openhab/bin/distribution-1.8.3-runtime.zip && \
    wget https://bintray.com/artifact/download/openhab/bin/distribution-1.8.3-addons.zip && \
    unzip distribution-1.8.3-runtime.zip -d OpenHab && \
    unzip distribution-1.8.3-addons.zip -d OpenHab/all_addons

# Configure OpenHab
RUN cp OpenHab/all_addons/org.openhab.binding.systeminfo-1.8.3.jar OpenHab/addons/

COPY third_party/libsigar-amd64-linux.so OpenHab/lib/libsigar-amd64-linux.so
COPY example/configurations /OpenHab/configurations

VOLUME ["/OpenHab/configurations"]
EXPOSE 8080

CMD /OpenHab/start.sh