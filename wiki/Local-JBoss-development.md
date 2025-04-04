**TBD:** Need to be tested or extended for Windows with Adaption of this page

If you want to develop Hogajama locally, you can start a local Wildfly and connect to a PostgreSQL and MongoDB running
in Docker Containers.

## Compile

To get started locally, it is best to use only simple components for the runtime. For example, don't use Keycloak or
Kafka. [See Modularisation Architecture](Modularisation-Architecture) and [Optional Modules](Optional-Modules). Compile
e.g. with:

```
(cd Hogajama && mvn clean install -P security-dummy,messaging-rest)
```

See [Messaging-Modules](Messaging-Modules) for starting Tests in this setup.

## Setup Guide

### Setup PostgreSQL in Docker

```
docker run -d  --name hogarama-postgres -p 5432:5432 -e POSTGRES_DB=management -e POSTGRES_USER=hogajama -e POSTGRES_PASSWORD=changepassword postgres
```

You don't have to, but you may insert some configuration data into the management database:

```
echo 'drop schema hogajama cascade;' | docker run -i --rm  --network host -e PGPASSWORD=changepassword postgres psql -h localhost  -p 5432 -d management -U hogajama
cat Hogajama/configuration/postgres_backup_2023_07_10.txt | docker run -i --rm --network host -e PGPASSWORD=changepassword postgres psql -h localhost  -p 5432 -d management -U hogajama
```

### Setup MongoDB in Docker

```
docker run -d  --name hogarama-mongo -p 27017:27017  -e MONGODB_USERNAME=hogajama -e MONGODB_PASSWORD=changepassword -e MONGODB_DATABASE=hogajamadb -e ALLOW_EMPTY_PASSWORD=yes bitnami/mongodb:latest
```

### Setup Wildfly

Download
the [Wildfly Server (26.1.1.Final)](https://github.com/wildfly/wildfly/releases/download/26.1.1.Final/wildfly-26.1.1.Final.zip)
and place the server source folder as a .zip file in the `Downloads` folder of your user. In this case there is a
`wildfly-26.1.1.Final.zip` in the Downloads folder.

Clone JBoss Setup Scripts and setup JBSS environment:
(in this example the `JBSS` Folder is placed next to the `Hogarama` Folder)

```
git clone git@github.com:Gepardec/JBSS.git
./JBSS/bin/setup.sh -i hogajama -r wildfly-26.1.1.Final
echo export ENV_FILE=`pwd`/Hogarama/codereadyworkspace/hogarama_local.env >> ~/.hogajamarc
```

Have a look to
the [hogarama_local.env](https://github.com/Gepardec/Hogarama/blob/master/codereadyworkspace/hogarama_local.env) file,
this is the configuration of your local hogajama.

Configure your local JBoss:

```
cd  Hogarama/Hogajama/configuration/local_configuration/
hogajama configure .
```

Use `hogajama help` to find out more.

You may stop the unused MDB to prevent warnings in jboss-cli:
`/deployment=hogajama-rs-0.0.1-SNAPSHOT.war/subsystem=ejb3/message-driven-bean=WateringMDB:stop-delivery`

### Deployments

Following deployments are on the JBoss:

* hogajama-rs
* hogajama-frontend
* hogajama-angular-frontend

### Setup Hogarama in Docker

To start all services go
to [Docker-Infrastructure](https://github.com/Gepardec/Hogarama/tree/master/Docker-Infrastructure/) and run
`docker-compose -p local-hogarama up -d`.

#### AMQ

Registration on [RedHat](https://access.redhat.com/RegistryAuthentication) is required!
Run `docker login https://registry.redhat.io` for login.
Access the UI on [https://localhost:8161/console/auth/login](https://localhost:8161/console/auth/login).

#### Keycloak

Keycloak is running on port `18080`. Used to authentificate user to access data.
It was set up using following the [official documentation](https://www.keycloak.org/server/containers).

To use Keycloak for your local Hogarama, following steps needs to be done:

1. A new `Role` has to be created:

    1. Open `http://localhost:18080/` and log in with admin credentials (If you have trouble with chrome: Enable setting
       `chrome://flags/#allow-insecure-localhost`
       or enter `thisisunsafe` in the chrome tab.)
    2. Go to `Hogarama` realm > Realm roles > Create role
    3. Create role with `Role Name` `admins`

2. A new user has to be created:

    1. Open `http://localhost:18080/` and log in with admin credentials (If you have trouble with chrome: Enable setting
       `chrome://flags/#allow-insecure-localhost`
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
    4. Copy the new generated secret and set it in the `Docker-Infrastructure/hogarama/local_env/hogarama_local.env`
       file as `KEYCLOAK_CREDENTIALS_SECRET` environment variable
    5. restart hogajama WildFly server:
       ```
       docker exec -it hogajama bash
       hogajama restart
       ```

If you are using postman to call the hogarama REST endpoits, this will probably help you for the postman
setup [How to get access token from Keycloak using Postman — OAuth2](https://paulbares.medium.com/quick-tip-oauth2-with-keycloak-and-postman-cc7211b693a5)

#### MongoDB

MongoDB is running on port `27017`. Used to store received messages from AMQ

#### Postgres

Postgres is running on port `5432`. Used to save management data that is visible in the angular frontend.

#### Wildfly (Hogajma)

Wildfly server is running on port `8080` and is configured as the production instance.
It is necessery to move all `war` files into the `Docker-Infrastructure/hogajama/deployments` folder!

##### Local ENV file

Have a look at the `Docker-Infrastructure/hogajama/local_env/hogarama_local.env` file, this is the configuration of your
local hogajama.

##### Common Config

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

##### Kafka config (optional)

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

### Troubleshooting

#### Running Wildfly from IntelliJ in Debug Mode

**Problem:**
`ERROR: Cannot load this JVM TI agent twice, check your java command line for duplicate jdwp options.`

This is happening, because IntelliJ is passing JAVA_OPTS parameters already defined in the `standalone.conf`

**Solution:**

1. Edit the `standalone.conf` in your `jboss-hogajama/bin` Folder:

* Comment out this line: `JAVA_OPTS="$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,address=8787,server=y,suspend=n"`

2. IntelliJ: Disable the `Pass environment variables` within the Run/Debug Configuration->Startup/Connection for your
   Wildfly Server

***

**Problem:**
Website shows "Could not load data. Please try again later."

**Solution:**

* You didn't send data. You can send data with [Raspberry-PI-Mocks-GUI](Raspberry-PI-Mocks-GUI)
* If you just added the environment variables, restart your JBoss, IDE
