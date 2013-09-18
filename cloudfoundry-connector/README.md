Cloud Foundry connector for spring-cloud
========================================

Provides Cloud Foundry connector with support for Mysql, Postgres, RabbitMQ, MongoDB, and Redis services.

Supporting additional services
------------------------------
The cloudfoundry-connector offers extending support for additional services 
through the same [`ServiceLoader`](http://docs.oracle.com/javase/7/docs/api/java/util/ServiceLoader.html) 
mechanism used in the [core](../core) project. 
It allows extending to new services without modifying the [cloudfoundry-connector](../cloudfoundry-connector)
itself. All you need to do is:

1. Create a new project declaring dependency of [cloudfoundry-connector](../cloudfoundry-connector).
2. Add one implementation of [`CloudFoundryServiceInfoCreator`](src/main/java/org/springframework/cloud/cloudfoundry/CloudFoundryServiceInfoCreator.java) 
   for each service type you wish to extend. 
   Along the way, you will, of course, add an implementation of [`ServiceInfo`](../core/main/java/org/springframework/cloud/service/ServiceInfo.java) 
   (consider extending [`BaseServiceInfo`](../core/main/java/org/springframework/cloud/service/BaseServiceInfo.java)), 
   attach an [`@ServiceLabel`](../core/main/java/org/springframework/cloud/service/ServiceLabel.java) annotation, 
   and mark appropriate properties with [`@ServiceProperty`]((../core/main/java/org/springframework/cloud/service/ServiceProperty.java)) annotation.
3. Add a file on classpath 
   `META-INF/services/org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator`
   and add all your implementations of `CloudFoundryServiceInfoCreator`.