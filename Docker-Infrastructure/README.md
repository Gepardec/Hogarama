# Hogarama Infrastructure with Docker

To start all services run `docker-compose -p local-hogarama up -d` in the current path.

Don't forget to create a new Keycloak user and the `hogarama` client credentials secret (see [Keycloak](#keycloak))

## AMQ

Registration on [RedHat](https://access.redhat.com/RegistryAuthentication) is required! 
`docker login https://registry.redhat.io`

AMQ is used to receive messages with the current sensor values.
The WildFly server `hogajama` save received messages in the database.

Access the UI on `https://localhost:8161/console/auth/login`.

Run `mock-cli-amq.sh` to check if AMQ is running correclty.\
Check MongoDB if new entries were generated in `watering` collection.

## Keycloak

Keycloak is running on port `18080`. Used to authentificate user to access data.
It was set up using following the [official documentation](https://www.keycloak.org/server/containers).

To use Keycloak for your local Hogarama, following steps needs to be done:

1. A new `Role` has to be created:

   1. Open `http://localhost:18080/` and log in with admin credentials (If you have trouble with chrome: Enable setting `chrome://flags/#allow-insecure-localhost`
      or enter `thisisunsafe` in the chrome tab.)
   2. Go to `Hogarama` realm > Realm roles > Create role
   3. Create role with `Role Name` `admins`

2. A new user has to be created:

   1. Open `http://localhost:18080/` and log in with admin credentials (If you have trouble with chrome: Enable setting `chrome://flags/#allow-insecure-localhost`
      or enter `thisisunsafe` in the chrome tab.)
   2. Go to `Hogarama` realm > Users > Add user
   3. Fill in the form the information about the new user and click on `save`
   5. Select new user
      1. Go to `Credentials` tab > set the password for the new user (uncheck `Temporary`)
      2. Go to `Role Mapping` tab > in `Realm Roles` assign `admins` role 

3. the `hogarama` credentials secret has to be created:

   1. Open `http://localhost:18080/` and log in with admin credentials
   2. Go to `Hogarama` realm > Clients > Select `hogarama`
   3. Go to `Credentials` tab and click `Generate secret`
   4. Copy the new generated secret and set it in the [hogarama_local.env](hogajama/local_env/hogarama_local.env) file as `KEYCLOAK_CREDENTIALS_SECRET` environment variable
   5. restart hogajama WildFly server:
      ```
      docker exec -it hogajama bash
      hogajama restart
      ```

If you are using postman to call the hogarama REST endpoits, this will probably help you for the postman setup [How to get access token from Keycloak using Postman — OAuth2](https://paulbares.medium.com/quick-tip-oauth2-with-keycloak-and-postman-cc7211b693a5)

## MongoDB

MongoDB is running on port `27017`. Used to store received messages from AMQ

## Postgres

MongoDB is running on port `5432`. Used to save management data that is visible in the angular frontend.

## Wildfly (Hogajma)

Wildfly server is running in port `8080` and is configured as the production instance.

### Local ENV file
Have a look at the [hogarama_local.env](hogajama/local_env/hogarama_local.env) file, this is the configuration of your local hogajama.

### Common Config
Configure your local JBoss with all moduls (include kafka and disabled security):
```
docker exec -it hogajama bash
cd /tmp/setup/config-all
hogajama configure .
```
Configure your local JBoss with all mandatory moduls (without kafka):
```
docker exec -it hogajama bash
cd /tmp/setup/config-mandatory
hogajama configure .
```

### Kafka config
Enable Kafka in your local JBoss:
```
docker exec -it hogajama bash
cd /tmp/setup/config-enable-kafka
hogajama configure .
```
Disable Kafka in your local JBoss :
```
docker exec -it hogajama bash
cd /tmp/setup/config-disable-kafka
hogajama configure .
```

### Security config
Turning on security means that a bearer token is required for the hogarama REST endpoits.

Turning on security in your local JBoss:
```
docker exec -it hogajama bash
cd /tmp/setup/config-security-on
hogajama configure .
```

Turning off security in your local JBoss :
```
docker exec -it hogajama bash
cd /tmp/setup/config-security-off
hogajama configure .
```

### Further steps
Use `hogajama help` to find out more.

You may stop the unused MDB to prevent warnings in jboss-cli: `/deployment=hogajama-rs-0.0.1-SNAPSHOT.war/subsystem=ejb3/message-driven-bean=WateringMDB:stop-delivery`


