FROM ubuntu:16.04
MAINTAINER Gepardec

# Install add-apt-repository
RUN apt-get update && \
    apt-get install -y software-properties-common && \
    rm -rf /var/lib/apt/lists/*

# Install Java.
RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
    add-apt-repository -y ppa:webupd8team/java && \
    apt-get update && \
    apt-get install -y oracle-java8-installer && \
    rm -rf /var/lib/apt/lists/* && \
    rm -rf /var/cache/oracle-jdk8-installer

# Copy and configure artefacts
RUN mkdir -p /root/jboss
COPY inc /root/
RUN chmod a+x /root/config/* && \
	chmod a+x /root/bin/* && \ 
	chmod a+x /root/jboss/wildfly-10.0.0.Final/bin/* && \
	cd /root && \
	ln -s /root/bin/jboss7 hogarama && \
	chmod a+x hogarama && \
	cp /root/bin/hogarama.rc .hogaramarc && \
	/root/bin/hogarama-install.sh


CMD /root/bin/hogarama start && bash