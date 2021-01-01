# Read Me First

The following was discovered as part of building this project:

* The original package name 'iot.tetracube-red.smart-hub' is invalid and this project uses 'iot.tetracubered.smarthub'
  instead.

## Test cases

Device provisioning:

```json
{
  "id": "ce694f72-c12b-4e19-aa80-c3af37898615",
  "default_name": "Test device",
  "feedback_topic": "device/feedback/ce694f72-c12b-4e19-aa80-c3af37898615",
  "actions": [
    {
      "id": "3253f251-3ab3-4987-8cdf-aa686de2b52b",
      "command_topic": "device/ce694f72-c12b-4e19-aa80-c3af37898615/turn/on",
      "querying_topic": null,
      "default_name": "Turn on",
      "value": 0.0
    },
    {
      "id": "3253f251-3ab3-4987-8cdf-aa686de2b52b",
      "command_topic": "device/ce694f72-c12b-4e19-aa80-c3af37898615/turn/off",
      "querying_topic": null,
      "default_name": "Turn off",
      "value": 0.0
    }
  ]
}
```