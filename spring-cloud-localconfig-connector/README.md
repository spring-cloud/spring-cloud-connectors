Local-configuration connector for Spring Cloud
=======================================

Provides the ability to configure Spring Cloud services locally for development or testing.
The current implementation reads from Java properties only; in order to prevent dependencies
on the Spring Framework, the placeholder functionality is unavailable in the connector.
Pull requests for also inspecting environment variables are welcome.

Property sources
----------------
This connector first attempts to read the system properties generally and a property named
`spring.cloud.propertiesFile` specifically. If the system properties are not readable
(the security manager denies `checkPropertiesAccess`), then they will be treated as empty.
If a system property named `spring.cloud.propertiesFile` is found, that file will be loaded
as a property list.

###Programmatically supplying properties
You can programmatically supply a property source by calling the static method
`LocalConfigConnector.supplyProperties(InputStream)` before invoking `getCloud()`.
Calling this method will cause the connector to read the stream as a property list
and then close the stream. Calling this method after invoking `getCloud()` will
still read the stream, but the properties will have no effect on the connector
service configuration. Calling this method multiple times will load the supplied
streams onto the same `Properties` object, overwriting duplicates.

###Property order
To provide the maximum configuration flexibility, the connector will scan the available
property sources in this order:

- programmatically-supplied properties
- properties read from `spring.cloud.propertiesFile`
- system properties

The last definition of a specific service ID wins. The connector will log a message at
`INFO` to notify of service overrides for the same type of service and at `WARN` if you
override a service ID with a URI to a different type of service.

Activating the connector
------------------------
The Spring Cloud core expects exactly one cloud connector to return `true` for
`isInMatchingCloud()`. This connector identifies the "local cloud" by the presence of
a property named `spring.cloud.appId`, which will be used in the `ApplicationInstanceInfo`.

Service definitions
-------------------
If the connector is activated, it will iterate through all the available properties
for keys matching the pattern `spring.cloud.{serviceId}`. Each value is interpreted as a URI
to the services, and the type of service is determined from the scheme. All of the standard
`UriBasedServiceInfo`s are supported.

Supporting additional services
------------------------------
Please see the documentation for [cloudfoundry-connector](../spring-cloud-cloudfoundry-connector), since the same
mechanism applies to any cloud connector.

Instance ID
-----------
This connector will create a UUID for use as the instance ID, as Java does not provide
any portable mechanism for reliably determining hostnames or PIDs.