FROM DUMMY

USER root

RUN buildDeps="sudo make gcc g++ libc-dev ruby-dev" \
 && apt-get update \
 && apt-get install -y --no-install-recommends $buildDeps \
 && sudo gem install \
        fluent-plugin-mongo \
 && sudo gem install \
        fluent-plugin-mqtt-io \
 && sudo gem sources --clear-all \
 && SUDO_FORCE_REMOVE=yes \
    apt-get purge -y --auto-remove \
                  -o APT::AutoRemove::RecommendsImportant=false \
                  $buildDeps \
 && rm -rf /var/lib/apt/lists/* \
           /home/fluent/.gem/ruby/2.3.0/cache/*.gem

#RUN useradd fluent -d /home/fluent -m -U
#RUN chown -R fluent:fluent /home/fluent

COPY entrypoint.sh /bin/
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
