Heroku connector for spring-cloud
=======================================

Provides Heroku connector with support for Postgres (Mysql, RabbitMQ, MongoDB, and Redis services 
coming soon; PR requests welcome).

Supporting additional services
------------------------------
Please see the documentation for [cloudfoundry-connector](../cloudfoundry-connector), since the same 
mechanism applies to any cloud connector.

Limitations
-----------
Unlike CloudFoundry, Heroku exposes very little information about the app that is retrievable from 
within a running app. For example, there is no good way to know the name of the application. 
Therefore, if an app desires such info, it needs to make that available through environment variables.

To have sensible app name available through [`ApplicationInstanceInfo`](../core/src/main/java/org/springframework/cloud/app/ApplicationInstanceInfo.java) 
(and associated properties), set the `SPRING_CLOUD_APP_NAME `variable
  
    heroku config:add SPRING_CLOUD_APP_NAME=myappname --app myappname
    
If this env variable is not set, the app name will be set to `<unknown>`.