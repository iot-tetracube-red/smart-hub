# The tetracube's IoT Smart Hub project

This project represents the service, where all appliances that compound
a smart-home, can connect to be used via cloud services such as
Alexa Skill or Telegram Bot.

## Project structure
The project is based on a broker and an application server built on Quarkus.

The folder src/main/docker is the root folder of containers files, here you can find
two of docker-compose files: one of development environment and another one for production. 
Beside the docker-compose there is an example of dot-env file
used to compile the images of PostgreSQL and RabbitMQ.<br/>
The *iot-broker* folder contains RabbitMQ custom image built to activate MQTT plugin.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./gradlew quarkusDev
```

## Packaging and running the application

The application can be packaged using:
```shell script
./gradlew build
```
It produces the `smart-hub-1.0.0-SNAPSHOT-runner.jar` file in the `/build` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/lib` directory.

If you want to build an _über-jar_, execute the following command:
```shell script
./gradlew build -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar build/smart-hub-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./gradlew build -Dquarkus.package.type=native
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./build/smart-hub-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/gradle-tooling.

# RESTEasy JAX-RS

Guide: https://quarkus.io/guides/rest-json


