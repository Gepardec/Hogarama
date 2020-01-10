## Install postgresql

1. extract org.zip inside your JBoss `modules` folder
2. Add the following inside of `standalone.xml` in `datasources`

```xml
<datasource jndi-name="java:jboss/datasources/Hogajama" pool-name="PostgresDS" enabled="true">
    <connection-url>jdbc:postgresql://localhost:5432/hogajama</connection-url>
    <driver>postgresql</driver>
    <security>
        <user-name>hogajama</user-name>
        <password>hogajama</password>
    </security>
</datasource>
``` 
3. Add the following inside of `standalone.xml` in `datasources/drivers`

```xml
<driver name="postgresql" module="org.postgresql">
    <driver-class>org.postgresql.Driver</driver-class>
</driver>
``` 

4. Start the docker container:
```shell
docker run -it -p 5432:5432 -e POSTGRES_USER=hogajama -e POSTGRES_PASSWORD=hogajama -e POSTGRES_DB=hogajama postgres:11
```
