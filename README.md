# Spring Cloud Connectors [![Build Status](https://build.spring.io/plugins/servlet/buildStatusImage/CLOUD-NIGHTLY "Optional title")](https://build.spring.io/browse/CLOUD-NIGHTLY)

Spring Cloud Connectors provides a simple abstraction that JVM-based applications can use to discover information about the cloud environment on which they are running, connect to services, and have discovered services registered as Spring beans. It provides out-of-the-box support for discovering common services on Heroku and Cloud Foundry cloud platforms, and it supports custom service definitions through Java Service Provider Interfaces (SPI).

## Learn more

See the [project site](http://cloud.spring.io/spring-cloud-connectors/) and [current documentation](http://cloud.spring.io/spring-cloud-connectors/spring-cloud-connectors.html).

## Build

The project is built with Gradle. The [Gradle wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) allows you to build the project on multiple platforms and even if you do not have Gradle installed; run it in place of the `gradle` command (as `./gradlew`) from the root of the main project directory.

### To compile the project and run tests

    ./gradlew build

### To build a JAR

    ./gradlew jar

### To generate Javadoc API documentation

    ./gradlew api

### To list all available tasks

    ./gradlew tasks

## Contribute

Before we accept a non-trivial patch or pull request we will need you to sign the [contributor's agreement](https://support.springsource.com/spring_committer_signup). Signing the contributor's agreement does not grant anyone commit rights to the main repository, but it does mean that we can accept your contributions, and you will get an author credit if we do.  Active contributors might be asked to join the core team, and given the ability to merge pull requests.

## License

Spring Cloud Connectors is released under version 2.0 of the [Apache License](http://www.apache.org/licenses/LICENSE-2.0).
