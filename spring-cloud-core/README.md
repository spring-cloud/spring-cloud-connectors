Spring Cloud Core Library
=========================

The core library to let cloud applications access application information and services.  
While Spring applications is one of the main target for this library, it may be used in 
non-Spring projects as well. In fact, this library doesn't even depend on Spring.

This library is cloud-agnostic. Through connectors, it supports multiple clouds 
(with Cloud Foundry being one of the example cloud).

This library also supports an extension to create services connectors of user-desired types.

Usage pattern: Application Developers
=====================================

* Create a `CloudFactory` instance. Creation of a `CloudFactory` instance is a bit expensive, 
  so caching such an instance is recommended.
  
    CloudFactory cloudFactory = new CloudFactory();
  
* Obtain a suitable `Cloud` for the environment in which the application is running.
  
    Cloud cloud = cloudFoundry.getCloud();
    
* Use the `Cloud` instance to get access to application info, service infos, and create service connectors.

    // `ServiceInfo` has all the information necessary to connect to the underlying service
    cloud.getServiceInfos();
    
    // Alternatively, let `Cloud` create a service connector for you
    cloud.getServiceConnector("inventory-db", DataSource.class, null /* default config */);

Usage pattern: Cloud Providers
==============================
A cloud provider may configure the runtime in two ways:
1. Add new `CloudConnector`s to make spring-cloud related libraries work with a new cloud. 
   See spring-cloudfoundry-connector for an example. This is done declaratively by adding connector classes to:
    META-INF/services/org.springframework.cloud.CloudConnector
2. Add new `ServiceConnectorCreator`s to allow creation of service connector objects.	
   This is done declaratively by adding creator classes to: 
    META-INF/services/org.springframework.cloud.service.ServiceConnectorCreator