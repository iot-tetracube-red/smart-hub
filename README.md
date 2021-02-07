# smart-hub project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `smart-hub-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory. Be aware that it’s not an _
über-jar_ as the dependencies are copied into the `target/lib` directory.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar target/smart-hub-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/smart-hub-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html
.

## Test cases

```json
{
  "id": "ce694f72-c12b-4e19-aa80-c3af37898615",
  "name": "Test device",
  "feedback-topic": "devices/feedback/ce694f72-c12b-4e19-aa80-c3af37898615",
  "features": [
    {
      "id": "3253f251-3ab3-4987-8cdf-aa686de2b52b",
      "name": "Relay Switch",
      "feature_type": "SWITCH",
      "value": 0.0,
      "default_name": "Switch",
      "actions": [
        {
          "id": "3253f251-3ab3-4987-8cdf-aa686de2b52b",
          "name": "Turn ON",
          "trigger_topic": "devices/ce694f72-c12b-4e19-aa80-c3af37898615/feature/3253f251-3ab3-4987-8cdf-aa686de2b52b"
        },
        {
          "id": "7bb7f58e-68e3-4589-bbaf-e8e23b78e80a",
          "name": "Turn OFF",
          "trigger_topic": "devices/ce694f72-c12b-4e19-aa80-c3af37898615/feature/7bb7f58e-68e3-4589-bbaf-e8e23b78e80a"
        }
      ]
    }
  ]
}
```

* GET /bot/features list of devices with names and features names
* GET /bot/devices/{name}/features/{name}/commands the list of the commands by given feature's name
* PATCH /bot/devices/features/command {device name, feature name, command name, reference id, source: TELEGRAM|ALEXA}
* when the process finishes then call an hook {reference id, source, value, device name, feature name}