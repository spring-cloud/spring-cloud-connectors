CloudFoundry connector for spring-cloud
=======================================

Provides CloudFoundry connector with support for Mysql, Postgres, RabbitMQ, MongoDB, and Redis services.

Supporting additional services
------------------------------
The cloudfoundry-connector offers extending support for additional services through the same ServiceLoader mechanism used
in the 'core' project. It allows extending to new services without modifying the cloudfoundry-connector itself. All you need
to do is:

1. Create a new project declaring dependency of cloudfoundry-connector.
2. Add one implementation of `CloudFoundryServiceInfoCreater` for each service type you wish to extend. 
   Along the way, you will, of course, add an implementation of `ServiceInfo` (consider extending `BaseServiceInfo`)
   attach an @ServiceLabel annotation, and mark appropriate properties with `@SerivceProperty` annotation.
3. Add a file on classpath `META-INF/services/org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator`
   and add all your implementations of `CloudFoundryServiceInfoCreater`.