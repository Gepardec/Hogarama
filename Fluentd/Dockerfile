FROM fluent/fluentd:v1.1.0-debian 

LABEL maintainer Matthias Kapferer <matthias.kapferer@gepardec.com>
LABEL Description="Fluentd docker image with mqtt and mongodb plugins" Vendor="Gepardec IT Services GmbH" Version="1.0"

# Set Timezone
ENV TZ=Europe/Vienna
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Tell ruby to install packages as user RUN echo "gem: --user-install --no-document" >> ~/.gemrc
ENV PATH /home/fluent/.gem/ruby/2.3.0/bin:$PATH
ENV GEM_PATH /home/fluent/.gem/ruby/2.3.0:$GEM_PATH

# Do not split this into multiple RUN! 
# Docker creates a layer for every RUN-Statement 
# therefore an 'apt-get purge' has no effect 

RUN apt-get update \
 && apt-get upgrade -y \
 && buildDeps=" \
      make gcc g++ libc-dev \
      ruby-dev \
      wget bzip2 gnupg dirmngr \
    " \
 && apt-get install -y --no-install-recommends $buildDeps \
 && /usr/local/bin/fluent-gem install fluent-plugin-mqtt-io \
 && /usr/local/bin/fluent-gem install fluent-plugin-mongo \
 && apt-get purge -y --auto-remove \
                  -o APT::AutoRemove::RecommendsImportant=false \
                  $buildDeps \
 && rm -rf /var/lib/apt/lists/* \
 && rm -rf /tmp/* /var/tmp/* /usr/lib/ruby/gems/*/cache/*.gem

RUN useradd fluent -d /home/fluent -m -U
RUN chown -R fluent:fluent /home/fluent

# No longer needed (inherited from base image)
# for log storage (maybe shared with host) 
#RUN mkdir -p /fluentd/log
# configuration/plugins path (default: copied from .) 
#RUN mkdir -p /fluentd/etc /fluentd/plugins

COPY config/fluent.conf /fluentd/etc/
COPY config/entrypoint.sh /bin/
RUN chmod +x /bin/entrypoint.sh

RUN chown -R fluent:fluent /fluentd

USER fluent
WORKDIR /home/fluent

ENV FLUENTD_OPT="" 
ENV FLUENTD_CONF="fluent.conf" 

# Override from base image
EXPOSE 24224 5140 
ENTRYPOINT ["/bin/entrypoint.sh"]
CMD exec fluentd -c /fluentd/etc/${FLUENTD_CONF} -p /fluentd/plugins $FLUENTD_OPT
