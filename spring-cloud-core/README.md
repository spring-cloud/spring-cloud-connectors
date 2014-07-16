#Spring Cloud Core

This core library provides programmatic access to application and service information. This library
has no Spring dependencies and may be used in non-Spring applications.

This library requires Java 6 or newer.

This library is cloud-agnostic. Using Java SPI, it supports pluggable cloud and service
connectors; support for Cloud Foundry and Heroku is available out-of-the-box, in addition to
locally-provided configuration for development and testing.

##Connecting to a cloud

> **Note:** If you are using Spring Cloud in a Spring application, you should consider
[automatically injecting Spring beans](../spring-cloud-spring-service-connector) instead.

* Include the desired cloud connectors on the runtime classpath
[as described in the main documentation](../#including-cloud-connectors).

* Create a [`CloudFactory`](src/main/java/org/springframework/cloud/CloudFactory.java) instance.
  Creation of a `CloudFactory` instance is a bit expensive, so using a singleton instance is recommended.
  If you are using a dependency injection framework such as Spring, create a bean for the `CloudFactory`.

    ```java
CloudFactory cloudFactory = new CloudFactory();
    ```

* Obtain the [`Cloud`](src/main/java/org/springframework/cloud/Cloud.java) object for the environment
  in which the application is running.

    ```java
Cloud cloud = cloudFactory.getCloud();
    ```
  Note that you must have a `CloudConnector` suitable for your deployment environment on your classpath.
  For example, if you are deploying the application to Cloud Foundry, you must add [cloudfoundry-connector](../spring-cloud-cloudfoundry-connector)
  to your classpath. If no suitable `CloudConnctor` is found, the `getCloud()` method will throw a [
  `CloudException`](../spring-cloud-core/src/main/java/org/springframework/cloud/CloudException.java).

* Use the `Cloud` instance to access application and service information and to create service
  connectors.

    ```java
// ServiceInfo has all the information necessary to connect to the underlying service
List<ServiceInfo> serviceInfos = cloud.getServiceInfos();
    ```

    ```java
// find the `ServiceInfo` definitions suitable for connecting to a particular service type
List<ServiceInfo> databaseInfos = cloud.getServiceInfos(DataSource.class);
	````

    ```java
// Alternately, let Spring Cloud create a service connector for you
String serviceId = "inventory-db";
DataSource ds = cloud.getServiceConnector(serviceId, DataSource.class, null /* default config */);
    ```

##Adding cloud connectors

A cloud provider may extend Spring Cloud by adding a new
[`CloudConnector`](src/main/java/org/springframework/cloud/CloudConnector.java)
to make Spring Cloud work with a new cloud platform. The connector is responsible for
telling whether the application is running in the specific cloud, identifying application
information (such as the name and instance ID of the particular running instance), and
mapping bound services (such as URIs exposed in environment variables) as `ServiceInfo` objects.

See the [Cloud Foundry](../spring-cloud-cloudfoundry-connector)
and [Heroku](../spring-cloud-heroku-connector) connectors for examples.

Spring Cloud uses the Java SPI to discover available connectors. New cloud connectors
should list the fully-qualified class name in the provider-configuration file at

```
META-INF/services/org.springframework.cloud.CloudConnector
```

## Adding service discovery

To allow Spring Cloud to discover a new type of service (`HelloWorldService`),
create a `ServiceInfo` class containing the information necessary to connect to your
service. If your service can be specified via a URI, extend `UriBasedServiceInfo`
and provide the URI scheme in a call to the `super` constructor.

This class will expose information for a service available at

````
helloworld://username:password@host:port/Bonjour
````

````java
public class HelloWorldServiceInfo extends UriBasedServiceInfo {
    public static final String URI_SCHEME = "helloworld";

	// needed to support structured service definitions like Cloud Foundry
    public HelloWorldServiceInfo(String id, String host, int port, String username, String password, String greeting) {
		super(id, URI_SCHEME, host, port, username, password, greeting);
    }

    // needed to support URI-based service definitions like Heroku
    public HelloWorldServiceInfo(String id, String uri) {
        super(id, uri);
    }
}
````

Then you will need to create a `ServiceInfoCreator` for each cloud platform you want to support.
You will probably want to extend the appropriate creator base class(es), such as `HerokuServiceInfoCreator`. This is
often as simple as writing a method that instantiates a new `HelloWorldServiceInfo`.

Register your `ServiceInfoCreator` classes in the appropriate provider-configuration file for
your cloud's `ServiceInfoCreator` base class.

## Adding service connectors

A service connector consumes a `ServiceInfo` discovered by the cloud connector and converts
it into the appropriate service object, such as a `DataSource` for a service definition
representing a SQL database.

Service connectors may be tightly bound to the framework whose service objects they are
creating; for example, some connectors in the
[Spring service connector](../spring-cloud-spring-service-connector) create connection
factories defined by Spring Data, for use in building Spring Data templates.

To add new service connectors, implement
[`ServiceConnectorCreator`](src/main/java/org/springframework/cloud/service/ServiceConnectorCreator.java)
in your connector classes and list the fully-qualified class names in the
provider-configuration file at

````
META-INF/services/org.springframework.cloud.service.ServiceConnectorCreator
````