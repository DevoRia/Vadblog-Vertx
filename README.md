# Vadblog-Vertx
Alternatine of Vadblog-springboot, using Vertx

## Start the project
``` bash
 mvn clean package
 java -jar target/vadblog-1.0.0-SNAPSHOT-fat.jar
```
## Start keycloak server
``` bash
 docker run --rm -p 8180:8080 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin jboss/keycloak
```
