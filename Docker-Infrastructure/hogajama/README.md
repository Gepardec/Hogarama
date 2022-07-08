# Setup and configure JBoss Server

# Setup
Download Wildfly 23.0.1.Final and place it as .zip in the `bin` folder

## Local ENV file
Have a look at the `hogarama_local.env` file, this is the configuration of your local hogajama.

## Common Config
Configure your local JBoss with all moduls (include kafka and disabled security):
```
cd /tmp/setup/configs/config-all
hogajama configure .
```
Configure your local JBoss with all mandatory moduls (without kafka):
```
cd /tmp/setup/configs/config-mandatory
hogajama configure .
```

## Kafka config
Enable Kafka in your local JBoss:
```
cd /tmp/setup/configs/config-enable-kafka
hogajama configure .
```
Disable Kafka in your local JBoss :
```
cd /tmp/setup/configs/config-disable-kafka
hogajama configure .
```

## Security config
Turning on security means that a bearer token is required for the hogarama REST endpoits.

Turning on security in your local JBoss:
```
cd /tmp/setup/configs/config-security-on
hogajama configure .
```

Turning off security in your local JBoss :
```
cd /tmp/setup/configs/config-security-off
hogajama configure .
```

## Further steps
Use `hogajama help` to find out more.

You may stop the unused MDB to prevent warnings in jboss-cli: `/deployment=hogajama-rs-0.0.1-SNAPSHOT.war/subsystem=ejb3/message-driven-bean=WateringMDB:stop-delivery`
