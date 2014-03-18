Cloud Foundry user-provided connector for spring-cloud
======================================================

Provides Cloud Foundry connector with support for user-provided Mysql, Postgres, Oracle, and RabbitMQ services.

Cloud Foundry allows service connection information and credentials to be provided by a user. The credentials can take
form, but the connectors in this module expect the service to be created with a single `uri` field in the credentials,
using the form `<scheme>://<username>:<password>@<hostname>:<port>/<name>`.

The examples below show the command to create user-provided services for each of the supported service types, using a
`uri` field that the connectors in this module will detect.

~~~
# create a user-provided Oracle database service instance
$ cf create-user-provided-service oracle-db -p '{"uri":"oracle://username:password@dbserver.example.com:1521/mydatabase"}'

# create a user-provided MySQL database service instance
$ cf create-user-provided-service mysql-db -p '{"uri":"mysql://username:password@dbserver.example.com:3306/mydatabase"}'

# create a user-provided PostgreSQL database service instance
$ cf create-user-provided-service postgres-db -p '{"uri":"postgres://username:password@dbserver.example.com:5432/mydatabase"}'

# create a user-provided RabbitMQ service instance
$ cf create-user-provided-service rabbit-queue -p '{"uri":"amqp://username:password@rabbitserver.example.com:5672/virtualhost"}'
~~~

