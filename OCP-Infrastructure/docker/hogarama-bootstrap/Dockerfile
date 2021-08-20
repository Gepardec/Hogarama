FROM quay.io/openshift/origin-cli:latest as oc
FROM centos:7

LABEL MAINTAINER="clemens.kaserer@gepardec.com, felix.hochleitner@gepardec.com"

COPY --from=oc /usr/bin/oc /usr/bin/

ENV VERSION v3.5.1

ENV HOME /home

# Install Helm
ENV FILENAME helm-${VERSION}-linux-amd64.tar.gz
ENV HELM_URL https://get.helm.sh/${FILENAME}

RUN echo $HELM_URL

RUN curl -o /tmp/$FILENAME ${HELM_URL} \
  && tar -zxvf /tmp/${FILENAME} -C /tmp \
  && mv /tmp/linux-amd64/helm /usr/bin/helm \
  && rm -rf /tmp

RUN yum update -y && \
    yum install -y python3 python3-libs python3-devel python3-pip && \
    yum clean all -y

RUN pip3 install j2cli j2cli[yaml] pyyaml pybase64 --upgrade
COPY jinja-filter /opt/jinja/filters/
WORKDIR /mnt