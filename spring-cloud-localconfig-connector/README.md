#Spring Cloud local-configuration connector

This connector provides the ability to configure Spring Cloud services locally for development
or testing. The current implementation reads from Java properties only. Pull requests for also
inspecting environment variables are welcome.

##Quick start

Since service URIs contain passwords and should not be stored in code, this connector does not
attempt to read service definitions out of the classpath. You can provide service definitions
as system properties

````
java -Dspring.cloud.database='mysql://user:pass@host:1234/dbname' -jar my-app.jar
````

and from a configuration properties file either by setting the `spring.cloud.propertiesFile` system property

````
java -Dspring.cloud.propertiesFile=/path/to/spring-cloud.properties -jar my-app.jar
````

or by providing a *bootstrap* properties file on the runtime classpath named
`spring-cloud-bootstrap.properties`. This file will be inspected for only
the property named `spring.cloud.propertiesFile`, and its value will be interpolated
from the system properties.

````properties
spring.cloud.propertiesFile: ${user.home}/.config/myApp/spring-cloud.properties
````

The system properties or the configuration properties file should contain an application ID
and the desired services in this format:

````properties
spring.cloud.appId:    myApp
; spring.cloud.{id}:   URI
spring.cloud.database: mysql://user:pass@host:1234/dbname
````

Service type is determined by the URI scheme. The connector will activate if it finds a property
(in the system properties or the configuration properties file) named `spring.cloud.appId`.

##Property sources

This connector first attempts to read the system properties generally and a system property named
`spring.cloud.propertiesFile` specifically. If the system properties are not readable
(the security manager denies `checkPropertiesAccess`), then they will be treated as empty.
If a system property named `spring.cloud.propertiesFile` is found, that file will be loaded
as a property list.

###Providing a bootstrap properties file

To avoid having to manually configure run configurations or test runners with the path to the
configuration properties file, the connector supports reading a templated filename out of the
runtime classpath. This file must be named `spring-cloud-bootstrap.properties` and located
at the classpath root, and for security the connector will not attempt to read any service URIs
out of it. If the connector does find the file, it will read the property
`spring.cloud.propertiesFile` and [substitute the pattern
`${system.property}`](http://commons.apache.org/proper/commons-lang/javadocs/api-release/index.html?org/apache/commons/lang3/text/StrSubstitutor.html)
with the appropriate value from the system properties. The most useful option is generally
`${user.home}`.

A configuration properties file specified in the system properties will override any bootstrap
file that may be available on the classpath.

###Property precedence
To provide the maximum configuration flexibility, the connector will override any properties
(both application ID and service definitions) specified in the file at `spring.cloud.propertiesFile`
with system properties defined at runtime. The connector will log a message at
`WARN` if you override a service ID.

##Activating the connector

The Spring Cloud core expects exactly one cloud connector match the runtime environment.
This connector identifies the "local cloud" by the presence of a property named
`spring.cloud.appId` in a configuration properties file or the system properties,
which will be used in the `ApplicationInstanceInfo`.

##Service definitions

If the connector is activated, it will iterate through all the available properties
for keys matching the pattern `spring.cloud.{serviceId}`. Each value is interpreted as a URI
to the services, and the type of service is determined from the scheme. All of the standard
`UriBasedServiceInfo`s are supported.

##Supporting additional services

Extend [`LocalConfigServiceInfoCreator`](src/main/java/org/springframework/cloud/localconfig/LocalConfigServiceInfoCreator.java)
with a creator for [your service's `ServiceInfo` class](../spring-cloud-core/#adding-service-discovery).

Add the fully-qualified class name for your creator to

````
META-INF/service/org.springframework.cloud.localconfig.LocalConfigServiceInfoCreator
````


##Instance ID

This connector will create a UUID for use as the instance ID, as Java does not provide
any portable mechanism for reliably determining hostnames or PIDs.