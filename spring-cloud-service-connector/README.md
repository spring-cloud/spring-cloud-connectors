Provides `ServiceConnectorCreator` implementation for `javax.sql.DataSource` and various spring-data connector factories.

Also provides the namespace support for connecting to cloud services as well as accessing cloud services and application properties.

The `<cloud>` namespace
=======================

Setting up
----------

The `<cloud>` namespace offers a simple way for Spring application to connect to cloud services. To use this namespace, add a declaration for the cloud namespace:

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
               xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:cloud="http://www.springframework.org/schema/cloud"
           xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/cloud http://www.springframework.org/schema/cloud/spring-cloud.xsd">

    <!-- <cloud> namespace usage here -->

Creating service beans
----------------------

Each namespace element that creates a bean corresponding to a service follows the following pattern (example is for a relational service):

    <cloud:data-source id="inventory-db" service-name="inventory-db-service">
        <cloud:connection properties="sessionVariables=sql_mode='ANSI';characterEncoding=UTF-8"/>
        <cloud:pool pool-size="20" max-wait-time="200"/>
    </cloud>

This creates a `javax.sql.DataSource` bean with the `inventory-db` id, binding it to `inventory-db-service`. The created `DataSource` bean is configured with connection and pool propeties as specified in the nested elements.
When the `id` attribute is not specified, the service name is used as the `id`. When the `service-name` is not specified, the bean is bound to the only service in the corresponding category (relational database, in this case). If no unique service is found, a runtime exception is thrown.

Other namespace elements that create service connector include:
	
    <cloud:mongo-db-factory/>
    <cloud:redis-connection-factory/>
    <cloud:rabbit-connection-factory/>

Generic `<cloud:service>` element
--------------------------------

We also supports a generic `<cloud:service>` namespace to allow connecting to a service that doesn't have directly mapped element (typical for a newly introduced service or connecting to a private service in private PaaS). You must specify either the `connector-type` attribute (so that it can find a unique service matching that type) or the `service-name` attribute.

     <cloud:service id="email" service-name="email-service" connector-type="com.something.EmailConnectory/>

Scanning for services
---------------------

Besides these element that create one bean per element, we also support the `<cloud:service-scan>` element in the same spirit as the `<context:component-scan>` element. It scans for all the services bound to the app and creates a bean corresponding to each service. Each created bean has id that matches the service name to allow the use of the @Qualifier annotaiton along with @Autowired when more than one bean of the same type is introduced.

Accessing service properties
----------------------------
Lastly, we support `<cloud:properties>` that exposes properties for the app and services.
