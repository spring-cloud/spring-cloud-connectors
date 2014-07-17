#Spring Cloud Heroku connector

This connector will discover services bound to an application running in Heroku. It
currently knows about:

- PostgreSQL (Heroku)
- MySQL (ClearDB)
- Redis (RedisToGo, Redis Cloud, RedisGreen, openredis)
- MongoDB (MongoLab, MongoHQ, MongoSoup)
- RabbitMQ (CloudAMQP)

Pull requests for adding additional services are welcome.

##Supporting additional providers for existing service types

To add support for discovering a new provider for a service already listed above, add
the provider's environment prefix to the list in `getEnvPrefixes()` on the
`ServiceInfoCreator` class.

##Supporting new service types

Extend [`HerokuServiceInfoCreator`](src/main/java/org/springframework/cloud/heroku/HerokuServiceInfoCreator.java)
with a creator for [your service's `ServiceInfo` class](../spring-cloud-core/#adding-service-discovery).

Add the fully-qualified class name for your creator to

````
META-INF/service/org.springframework.cloud.heroku.HerokuServiceInfoCreator
````

##Limitations

Unlike CloudFoundry, Heroku exposes very little information about the app that is retrievable
from within a running instance. For example, there is no good way to find the name of the
application. Therefore, if an app desires such info, it needs to make it available through
environment variables.

To have sensible app name available through [`ApplicationInstanceInfo`](../core/src/main/java/org/springframework/cloud/app/ApplicationInstanceInfo.java),
set the `SPRING_CLOUD_APP_NAME` environment variable

    heroku config:add SPRING_CLOUD_APP_NAME=myappname --app myappname

If this env variable is not set, the app name will be set to `<unknown>`.