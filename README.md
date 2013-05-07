Libraries that lets application connect to cloud services and discover information about themselves.

The core concepts used in this project are:
1. Cloud Connector: An interface that a cloud provider can implement to allow the rest of the library work with a Platform As a Service (PaaS) offering.
2. Service Connector: An object, such as `java.sql.DataSource`, that represent a connection to a service.
3. Service information: Information about the underlying service such as host, port, and credentials.
4. Applicaiton information: Information about application and instance in which these libraries are embedded.

The project comprises of three subprojects:
1. spring-cloud-core: Core library that is cloud agnostic and Spring-agnostic. Provides entry point for application developers that choose to programmatically access cloud services and application information. It also provides an extension mechanism to contribute cloud connectors and service connector creators.
2. spring-cloud-service-connector: Library that provides service connectors creators for java.sql.DataSource and various connection factories spring-data projects.
3. spring-cloudfoundry-connector: Cloud connector for [Cloud Foundry](http://www.cloudfoundry.com).