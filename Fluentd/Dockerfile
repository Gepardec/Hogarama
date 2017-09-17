FROM debian:stretch-slim 

MAINTAINER Matthias Kapferer <matthias.kapferer@gepardec.com>

LABEL Description="Fluentd docker image" Vendor="Gepardec IT Services GmbH" Version="1.0"

# Do not split this into multiple RUN! 
# Docker creates a layer for every RUN-Statement 
# therefore an 'apt-get purge' has no effect 

RUN apt-get update \
 && apt-get upgrade -y \
 && apt-get install -y --no-install-recommends \
            ca-certificates \
            ruby \
 && buildDeps=" \
      make gcc g++ libc-dev \
      ruby-dev \
      wget bzip2 \
    " \
 && apt-get install -y --no-install-recommends $buildDeps \
 && update-ca-certificates \
 && echo 'gem: --no-document' >> /etc/gemrc \
 && gem install oj \
 && gem install json \
 && gem install fluentd -v 0.12.32 \
 && wget -O /tmp/jemalloc-4.4.0.tar.bz2 https://github.com/jemalloc/jemalloc/releases/download/4.4.0/jemalloc-4.4.0.tar.bz2 \
 && cd /tmp && tar -xjf jemalloc-4.4.0.tar.bz2 && cd jemalloc-4.4.0/ \
 && ./configure && make \
 && mv lib/libjemalloc.so.2 /usr/lib \
 && fluent-gem install fluent-plugin-mqtt-io -v 0.2.3 \
 && fluent-gem install fluent-plugin-mongo \
 && apt-get purge -y --auto-remove \
                  -o APT::AutoRemove::RecommendsImportant=false \
                  $buildDeps \
 && rm -rf /var/lib/apt/lists/* \
 && rm -rf /tmp/* /var/tmp/* /usr/lib/ruby/gems/*/cache/*.gem

RUN useradd fluent -d /home/fluent -m -U

RUN chown -R fluent:fluent /home/fluent

# for log storage (maybe shared with host) 
RUN mkdir -p /fluentd/log

# configuration/plugins path (default: copied from .) 
RUN mkdir -p /fluentd/etc /fluentd/plugins

RUN chown -R fluent:fluent /fluentd

COPY config/fluent.conf /fluentd/etc/

USER fluent

WORKDIR /home/fluent

# Tell ruby to install packages as user RUN echo "gem: --user-install --no-document" >> ~/.gemrc

ENV PATH /home/fluent/.gem/ruby/2.3.0/bin:$PATH
ENV GEM_PATH /home/fluent/.gem/ruby/2.3.0:$GEM_PATH

ENV FLUENTD_OPT="" 
ENV FLUENTD_CONF="fluent.conf" 
ENV LD_PRELOAD="/usr/lib/libjemalloc.so.2"

# Set Timezone
ENV TZ=Europe/Vienna
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE 24224 5140 

CMD exec fluentd -v -c /fluentd/etc/$FLUENTD_CONF -p /fluentd/plugins $FLUENTD_OPT
