FROM maven:3.6.3-jdk-8
LABEL maintainer="felix.hochleitner@gepardec.com"

RUN mkdir /.m2  && chgrp -R 0 /.m2 && \
    chmod -R g=u /.m2 && \
    mkdir /.npm && chgrp -R 0 /.npm && \
    chmod -R g=u /.npm && \
    chgrp -R 0 /home && chmod -R g=u /home

ENV HOME=/home

ENTRYPOINT /bin/bash


#current-tag: fhochleitner/maven:3.6.3-jdk8-ocp