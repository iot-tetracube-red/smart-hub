# The tetracube's IoT Smart Hub project

This project represents the service, where all appliances that compound a smart-home, can connect to be used via cloud
services such as Alexa Skill or Telegram Bot.

## Project structure

The project is based on a broker and an application server built on Quarkus.

The folder src/main/docker is the root folder of containers files, here you can find two of docker-compose files: one of
development environment and another one for production. Beside the docker-compose there is an example of dot-env file
used to compile the images of PostgreSQL and RabbitMQ.<br/>
The *iot-broker* folder contains RabbitMQ custom image built to activate MQTT plugin.

## Topic description

### Device provisioning

When a device is turned on, for the first time or not, registers itself to the hub with the message where is reported
its circuit id, a default name and a list of actions that are embeded in the devices.

* Topic: `devices/provisioning`

```json
{
  "circuit_id": "uuid string",
  "default_name": "default device name",
  "actions": [
    {
      "id": "uuid string",
      "name": "action name",
      "type": "action type compatible with ActionTimeEnum"
    }
  ]
}
```

Example of message

```json
{
  "circuit_id": "09543601-23ee-4d3b-a3a5-527b3a6a8386",
  "default_name": "Multimedia",
  "actions": [
    {
      "id": "7e0d7f80-0eef-4d4f-b47c-ab886ca05126",
      "name": "Turn on",
      "type": "BUTTON"
    },
    {
      "id": "457aad22-9659-4239-ad26-7f84472c16e7",
      "name": "Turn off",
      "type": "BUTTON"
    }
  ]
}
```

## Chatbot enpoints

Here a list of endpoints for the chatbot feature:

* *GET* `/bot/features` to get the list of all features' names and related device's name
* *GET* `/bot/devices/{name}/features/{name}/commands` to get the list of the commands by given device and feature name
* *PATCH* `/bot/devices/features/command` with payload reported below to trigger specific action to specific device
  feature

```json
  {
    "deviceName": "device name", 
    "featureName": "feature's name to trigger", 
    "commandName": "command's name to trigger", 
    "referenceId": "the id of the chat or conversation that triggered the action", 
    "source": "the kind of chatbot that has triggered the action [TELEGRAM|ALEXA]"
  }
  ```

When the process finishes then call an API hook with following body request with *POST* verb:

```json
{
  "referenceId":  "the id of the chat or conversation that triggered the action",
  "source": "the kind of chatbot that has triggered the action [TELEGRAM|ALEXA]",
  "value": "the current value that indicates the status of the feature",
  "deviceName": "device name",
  "featureName": "feature's name that has been triggered"
}
```
