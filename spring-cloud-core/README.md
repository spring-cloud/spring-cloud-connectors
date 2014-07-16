#Spring Cloud Core

This core library provides manual access to application and service information. This library
has no Spring dependencies and may be used in non-Spring applications.

This library requires Java 6.

This library is cloud-agnostic. Using Java SPI, it supports pluggable cloud and service
connectors; support for Cloud Foundry and Heroku is available out-of-the-box, in addition to
locally-provided configuration for development and testing.

###Usage

> **Note:** If you are using Spring Cloud in a Spring application, you should consider
[automatically injecting Spring beans](../spring-cloud-spring-service-connector) instead.

* Include the desired cloud connectors [as described in the main documentation](../#including-cloud-connectors)

* Create a [`CloudFactory`](src/main/java/org/springframework/cloud/CloudFactory.java) instance.
  Creation of a `CloudFactory` instance is a bit expensive, so caching such an instance is recommended.
  If you are using a dependency injection frameworks such as Spring, creating a bean for `CloudFactory`
  will achieve the caching effect.

    ```java
    CloudFactory cloudFactory = new CloudFactory();
    ```
* Obtain a suitable [`Cloud`](src/main/java/org/springframework/cloud/Cloud.java) for the environment
  in which the application is running.

    ```java
    Cloud cloud = cloudFactory.getCloud();
    ```
  Note that you must have a `CloudConnector` implementation suitable
  for the environment in which the application is being deployed in your classpath. For example, if you are
  deploying the application in Cloud Foundry, you must add [cloudfoundry-connector](../spring-cloud-cloudfoundry-connector)
  in your classpath. If no suitable `CloudConnctor` is found, the `getCloud()` method will throw a `CloudException`.
* Use the `Cloud` instance to get access to application info, service infos, and create service
  connectors.

    ```java
    // ServiceInfo has all the information necessary to connect to the underlying service
    cloud.getServiceInfos();
    ```

    ```java
    // Alternatively, let the cloud create a service connector for you
    DataSource ds = cloud.getServiceConnector("inventory-db", DataSource.class, null /* default config */);
    ```

Usage pattern: Cloud and Service Providers
==========================================
A cloud provider may extends the functionality in two ways:

1. Add new [`CloudConnector`](src/main/java/org/springframework/cloud/CloudConnector.java)s to make
   spring-cloud related libraries work with a new cloud.
   See [cloudfoundry-connector](../spring-cloud-cloudfoundry-connector)
   or [heroku-connector](../spring-cloud-heroku-connector) for an example.
   This is done declaratively by adding connector classes to:
    ```
    META-INF/services/org.springframework.cloud.CloudConnector
    ```
2. Add new [`ServiceConnectorCreator`](src/main/java/org/springframework/cloud/service/ServiceConnectorCreator.java)s
   to allow creation of service connector objects.
   See [spring-service-connector](../spring-cloud-spring-service-connector) for an example.
   This is done declaratively by adding creator classes to:
   ```
    META-INF/services/org.springframework.cloud.service.ServiceConnectorCreator
   ```