FROM quay.io/openshift/origin-cli:latest as oc
FROM centos:7

LABEL MAINTAINER="clemens.kaserer@gepardec.com, felix.hochleitner@gepardec.com"

COPY --from=oc /usr/bin/oc /usr/bin/

ENV VERSION v3.1.1

ENV HOME /home

# Install Helm
ENV FILENAME helm-${VERSION}-linux-amd64.tar.gz
ENV HELM_URL https://get.helm.sh/${FILENAME}

RUN echo $HELM_URL

RUN curl -o /tmp/$FILENAME ${HELM_URL} \
  && tar -zxvf /tmp/${FILENAME} -C /tmp \
  && mv /tmp/linux-amd64/helm /usr/bin/helm \
  && rm -rf /tmp

RUN yum install -y https://centos7.iuscommunity.org/ius-release.rpm && \
    yum update -y && \
    yum install -y python36u python36u-libs python36u-devel python36u-pip && \
    yum clean all -y

RUN pip3 install j2cli j2cli[yaml] pyyaml pybase64 --upgrade
COPY jinja-filter/ /opt/jinja/filters/
WORKDIR /mnt