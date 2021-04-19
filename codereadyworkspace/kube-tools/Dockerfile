FROM quay.io/openshift/origin-cli:latest as oc
FROM centos:7

LABEL MAINTAINER="felix.hochleitner@gepardec.com"

COPY --from=oc /usr/bin/oc /usr/bin/

ENV HELM_VERSION v3.1.1
ENV KUBECTL_VERSION v1.18.8
ENV SHELL /bin/bash

ENV HOME /home

# Install Helm
ENV FILENAME helm-${HELM_VERSION}-linux-amd64.tar.gz
ENV HELM_URL https://get.helm.sh/${FILENAME}

RUN echo $HELM_URL

RUN curl -o /tmp/$FILENAME ${HELM_URL} \
  && tar -zxvf /tmp/${FILENAME} -C /tmp \
  && mv /tmp/linux-amd64/helm /usr/bin/helm \
  && rm -rf /tmp


RUN curl -o /usr/bin/kubectl https://storage.googleapis.com/kubernetes-release/release/${KUBECTL_VERSION}/bin/linux/amd64/kubectl \
    && chmod +x /usr/bin/kubectl

RUN chgrp -R 0 /home/ && \
    chmod -R g=u /home/
