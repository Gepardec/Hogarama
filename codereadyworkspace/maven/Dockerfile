LABEL maintainer="felix.hochleitner@gepardec.com"

FROM maven:3.6.3-jdk-8

RUN mkdir /.m2  && chgrp -R 0 /.m2 && \
    chmod -R g=u /.m2

ENTRYPOINT /bin/bash


#current-tag: fhochleitner/maven:3.6.3-jdk8-ocp