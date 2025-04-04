The components running in OpenShift are

* ~~[Fluentd](/Gepardec/Hogarama/wiki/Fluentd)~~
* [AMQ](/Gepardec/Hogarama/wiki/AMQ)
* [MongoDB](/Gepardec/Hogarama/wiki/MongoDB)
* [Kafka](/Gepardec/Hogarama/wiki/Kafka)
* [Hogajama](/Gepardec/Hogarama/wiki/Hogajama)
* [Keycloak](/Gepardec/Hogarama/wiki/Signle-Sign-On)
* PostgreSQL Database

[Raspberry Pi](/Gepardec/Hogarama/wiki/Raspberry) sends the sensor data
to [AMQ](/Gepardec/Hogarama/wiki/AMQ). [Fluentd](/Gepardec/Hogarama/wiki/Fluentd) grabs the message, transforms, it and
saves the data in the [MongoDB](/Gepardec/Hogarama/wiki/MongoDB). [Hogajama](/Gepardec/Hogarama/wiki/Hogajama) which is
divided into [Frontend](/Gepardec/Hogarama/wiki/Frontend) and [Backend](/Gepardec/Hogarama/wiki/Backend) provides REST
services and displayes the data.
