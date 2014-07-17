#Spring Cloud Cloud Foundry connector

This connector will discover services bound to an application running in Cloud Foundry.
It currently knows about:

- PostgreSQL
- MySQL
- Oracle
- Redis
- MongoDB
- RabbitMQ
- SMTP gateway
- application monitoring (New Relic)

Since Cloud Foundry tags each service with a type, Spring Cloud does not care which
service provider is providing it.

##Supporting new service types

Extend [`CloudFoundryServiceInfoCreator`]((src/main/java/org/springframework/cloud/cloudfoundry/CloudFoundryServiceInfoCreator.java))
with a creator for [your service's `ServiceInfo` class](../spring-cloud-core/#adding-service-discovery).

Add the fully-qualified class name for your creator to

````
META-INF/service/org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator
````