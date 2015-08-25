# Spring Cloud Connectors [![Build Status](https://build.spring.io/plugins/servlet/buildStatusImage/CLOUD-NIGHTLY "Optional title")](https://build.spring.io/browse/CLOUD-NIGHTLY)

Spring Cloud Connectors provides a simple abstraction for JVM-based applications running on cloud platforms
to discover bound services and deployment information at runtime and provides support for registering
discovered services as Spring beans. It is based on a plugin model so that the identical
compiled application can be deployed locally or on multiple clouds, and it supports custom service
definitions through Java SPI.

Spring Cloud Connectors provides out-of-the-box support for discovering common services on Heroku and
Cloud Foundry clouds as well as a properties-based configuration for development and testing.

The core concepts used in this project are:

- [**Cloud connector**](./spring-cloud-core/src/main/java/org/springframework/cloud/CloudConnector.java):
  An interface specific to a cloud platform that identifies the presence of the platform and
  discovers any services bound to the application deployment.
- **Service connector**: An object, such as a `javax.sql.DataSource`, that represents a runtime
  connection to a service.
- [**Service information**](./spring-cloud-core/src/main/java/org/springframework/cloud/service/ServiceInfo.java):
  Information about the underlying service such as host, port, and credentials.
- [**Application information**](./spring-cloud-core/src/main/java/org/springframework/cloud/app/ApplicationInstanceInfo.java):
  Information about the application and the particular running instance.

The project contains three major submodules:

- **[core](spring-cloud-core)**: The core library, cloud- and Spring-agnostic, that provides
  a programmatic entry point for developers who prefer to access cloud services and application
  information manually. It also provides basic service definitions for several common services
  (databases, message queues) and an SPI-based extension mechanism to contribute
  cloud and service connectors.
- **[spring-service-connector](spring-cloud-spring-service-connector)**: A Spring Library that
  exposes application and cloud information and discovered services as Spring beans of the
  appropriate type. For example, SQL services will be exposed as `javax.sql.DataSource`s with
  optional connection pooling.
- Cloud connectors:
 - **[cloudfoundry-connector](spring-cloud-cloudfoundry-connector)**: Connector for [Cloud Foundry](http://cloudfoundry.org).
 - **[heroku-connector](spring-cloud-heroku-connector)**: Connector for [Heroku](https://www.heroku.com).
 - **[localconfig-connector](spring-cloud-localconfig-connector)**: Properties-based connector for
   manually providing configuration information for development or testing. Allows the use of the
   same Spring Cloud configuration wiring in all stages of application deployment.

##Getting Started

The following examples are written for Maven; simply include the appropriate dependencies for
your build system.

###Including cloud connectors

Include the connector for each cloud platform you want to be discoverable. Including multiple
connectors is perfectly fine; each connector will determine whether it should be active in a
particular environment.

````xml
<!-- to use Spring Cloud Connectors for development -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-localconfig-connector</artifactId>
    <version>1.2.0.RELEASE</version>
</dependency>

<!-- If you intend to deploy the app to Cloud Foundry -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-cloudfoundry-connector</artifactId>
    <version>1.2.0.RELEASE</version>
</dependency>

<!-- If you intend to deploy the app to Heroku -->
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-heroku-connector</artifactId>
	<version>1.2.0.RELEASE</version>
</dependency>
````

###Spring applications

Add the [`spring-service-connector`](spring-cloud-spring-service-connector) in
addition to your cloud connectors:

````xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-spring-service-connector</artifactId>
	<version>1.2.0.RELEASE</version>
</dependency>

<!-- to use Spring Cloud for development -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-localconfig-connector</artifactId>
    <version>1.2.0.RELEASE</version>
</dependency>

<!-- If you intend to deploy the app to Cloud Foundry -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-cloudfoundry-connector</artifactId>
    <version>1.2.0.RELEASE</version>
</dependency>

<!-- If you intend to deploy the app to Heroku -->
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-heroku-connector</artifactId>
	<version>1.2.0.RELEASE</version>
</dependency>
````

Then follow the instructions on [Spring configuration using Java configuration or
the `<cloud>` namespace](spring-cloud-spring-service-connector).

###Non-Spring applications
---------------
The [`spring-cloud-core`](core) dependency is included by each cloud connector,
so simply include the connectors for the platforms you want.

Then follow the [instructions](spring-cloud-core) on using the Spring Cloud Connectors API.

