[![Build Status](https://travis-ci.org/Gepardec/Hogarama.svg?branch=master)](https://travis-ci.org/Gepardec/Hogarama) [![Quality Gate](https://sonarcloud.io/api/badges/gate?key=com.gepardec.hogajama:hogajama)](https://sonarcloud.io/dashboard/index/com.gepardec.hogajama:hogajama)

# Hogarama
Home and Garden Automation.
This is Gepardec's fun project to learn about IoT, OpenShift and various other technologies.

## Habarama
Habarama is the OpenHab/Raspberry part of the Hogarama Project.
It uses OpenHab to connect the Raspberry with each other and for collecting data from the sensors.

[More...](Habarama/README.md)

## Hogajama
Hogajama is the Java/Maven part of the Hogarama Project. It is a Java EE application supposed to be deployed in Openshift(r) environment that provides backend services for R3.14 and frontend for end user.

[More...](Hogajama/README.md)

## Hogarama-Jenkins
Jenkins docker image project. This docker image containing Jenkins for building and deployment of Hogarama project. The image provides Docker inside itself, which allows to run any Docker container in your Jenkins build script.

[More...](Infrastructure/Jenkins/README.md)

For this project, the following RedHat products are used which require at least a RedHat developer subscription:
* JBoss EAP
* JBoss A-MQ

# Run Hogajama locally with Wildfly

## Automatic

* Navigate to the folder `Templates/local_wildfly`
* Edit the bash-script (`entrypoint.sh`):
    * `CREATE_USER=true` > Should a user be created
    * `WILDFLY_USERNAME` > Username of user that gets created
    * `WILDFLY_PASSWORD` > Password of user that gets created
    * `WILDFLY_PORT` > Port where Wildfly will be running
* Build the dockerfile: `docker build . -t test`

Info: Can also be run with an existing Wildfly (`<installFolder>` is the home dir)

### Windows
* Run the dockerfile: `docker run -v %cd%/<installFolder>:/mnt test`

### Linux
* Run the dockerfile: `docker run -v $(pwd)/<installFolder>:/mnt test`

**OR**

* Edit the bash-script, to change the paths at the top
    * `SCRIPT_PATH` > Path where the `entrypoint.sh` file is located
    * `INSTALL_PATH` > Path where the `<installFolder>` folder is located (including `<installFolder>` itself)
* Run the bash-script: `./entrypoint.sh`

## Manually
* Read `Hogajama/readme.md`
* Read `PostgreSQL/readme.md`

## Post-Steps

* Run the Postgre Container with the command in `PostgreSQL/create_docker_container.sh`
* Migrate the db with flyway by running `mvn -DskipTests=true org.flywaydb:flyway-maven-plugin:6.0.1:migrate` in the `hogajama_db` module

**IntelliJ:**

* Add a postgres configuration
    * Open the `Database` window
    * Click th configure button (4th from left)
    * Add a new PostgreSQL config with:
        * Host: localhost
        * Port: 5432
        * Username: hogajama
        * Password: hogajama
        * Database: hogajama
    * In the config under "Schemas" enable the schema `hogajama/administration`
* Add Wildfly
    * In the top right, open the drop-down
    * Click on "Edit Configurations"
    * Add new JBoss > local
    * To the deployments add `hogajama-rs`
    * If you want to develop in ionic, I suggest using `ionic serve` for that
    * If not, add it to the deployments also (`hogajama-angular-frontend`)
    * Now open the "Application Servers" view and start the server