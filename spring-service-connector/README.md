Provides `ServiceConnectorCreator` implementation for `javax.sql.DataSource` and 
various spring-data connector factories.

Also provides Java config and the XML namespace support for connecting to cloud services 
as well as accessing cloud services and application properties.

The Java config
===============

Typical use of Java config involves extending the `AbstractCloudConfig` class and adding methods with 
the `@Bean` annotation to create beans for services. Apps migrating from 
[auto-reconfguration](http://spring.io/blog/2011/11/04/using-cloud-foundry-services-with-spring-part-2-auto-reconfiguration/)
might first try [the service-scanning approach](#scanning-for-services) until they need more explicit control.
Java config also offers a way to expose application and service properties, should you choose to take
a lower level access in creating service connectors yourself (or for debugging purposes, etc.).  

Creating service beans
----------------------

In the following example, the configuration creates a `DataSource` bean connecting to the only 
relational database service bound to the app (it will fail if there is no such unique service).
It also creates a `MongoDbFactory` bean, again, connecting to the only mongodb service bound to the app.
Please check Javadoc for `AbstractCloudConfig` for ways to connect to other services.

    class CloudConfig extends AbstractCloudConfig {
        @Bean
        	public DataSource inventoryDataSource() {
            return connectionFactory().dataSource();
        }
        
        @Bean
        public MongoDbFactory documentMongoDbFactory() {
            return connectionFactory().mongoDbFactory();
        }
        
        ... more beans to obtain service connectors
    }

The bean names will match the method names unless you specify an explicit value to the annotation
such as `@Bean("inventory-service")` (this just follows how Spring's Java configuration works). 
    
If you have more than one service of a type bound to the app or want to have an explicit control over
the services to which a bean is bound, you can pass the service names to methods such as `dataSource()` 
and `mongoDbFactory()` as follows:
    
    class CloudConfig extends AbstractCloudConfig {
        @Bean
        public DataSource inventoryDataSource() {
            return connectionFactory().dataSource("inventory-db-service");
        }
        
        @Bean
        public MongoDbFactory documentMongoDbFactory() {
            return connectionFactory().mongoDbFactory("document-service");
        }
        
        ... more beans to obtain service connectors
    }
 
Method such as `dataSource()` come in a additional overloaded variant that offer specifying configuration 
options such as the pooling parameters. Please see Javadoc for more details.

Connecting to generic services
------------------------------

Java config supports access to generic services (that don't have a directly mapped method--typical for a 
newly introduced service or connecting to a private service in private PaaS) through the `service()`
method. It follows the same pattern as the `dataSource()` etc, except it allows supplying the connector 
type as an additional parameters.

Scanning for services
---------------------

You can scan for each bound service using the `@ServiceScan` annotation as follows (conceptually similar 
to the @ComponentScan annotation in Spring):

    @Configuration
    @ServiceScan
    class CloudConfig {
    }
    
Here, one bean of the appropriate type (`DataSource` for a relational database service, for example) will
be created. Each created bean will have the `id` matching the corresponding service name. You can then 
inject such beans using auto-wiring:

    @Autowired DataSource inventoryDb;

If the app is bound to more than one services of a type, you can use the `@Qualifier` annotation supplying it 
the name of the service as in the following code:

    @Autowired @Qualifier("inventory-db") DataSource inventoryDb;
    @Autowired @Qualifier("shipping-db") DataSource shippingDb;

Accessing service properties
----------------------------

You can expose raw properties for all services and the app throught a bean as follows:

    class CloudPropertiesConfig extends AbstractCloudConfig {
        @Bean
        public Properties cloudProperties() {
            return properties();
        }
    }

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

This creates a `javax.sql.DataSource` bean with the `inventory-db` id, binding it to `inventory-db-service`. The created `DataSource` bean is configured with connection and pool properties as specified in the nested elements.
When the `id` attribute is not specified, the service name is used as the `id`. When the `service-name` is not specified, the bean is bound to the only service in the corresponding category (relational database, in this case). If no unique service is found, a runtime exception is thrown.

Other namespace elements that create service connector include:

    <cloud:mongo-db-factory/>
    <cloud:redis-connection-factory/>
    <cloud:rabbit-connection-factory/>

Connecting to generic services
------------------------------

We also supports a generic `<cloud:service>` namespace to allow connecting to a service that doesn't have directly mapped element (typical for a newly introduced service or connecting to a private service in private PaaS). You must specify either the `connector-type` attribute (so that it can find a unique service matching that type) or the `service-name` attribute.

     <cloud:service id="email" service-name="email-service" connector-type="com.something.EmailConnectory/>

Scanning for services
---------------------

Besides these element that create one bean per element, we also support the `<cloud:service-scan>` element in the same spirit as the `<context:component-scan>` element. It scans for all the services bound to the app and creates a bean corresponding to each service. Each created bean has id that matches the service name to allow the use of the @Qualifier annotation along with @Autowired when more than one bean of the same type is introduced.

Accessing service properties
----------------------------
Lastly, we support `<cloud:properties>` that exposes properties for the app and services.
