# Smart Hub services

## Test cases

Device provisioning:

```json
{
  "id": "ce694f72-c12b-4e19-aa80-c3af37898615",
  "default_name": "Test device",
  "feedback_topic": "device/feedback/ce694f72-c12b-4e19-aa80-c3af37898615",
  "querying_topic": "device/ce694f72-c12b-4e19-aa80-c3af37898615/status",
  "actions": [
    {
      "id": "3253f251-3ab3-4987-8cdf-aa686de2b52b",
      "command_topic": "device/ce694f72-c12b-4e19-aa80-c3af37898615/turn/on",
      "default_name": "Turn on"
    },
    {
      "id": "3253f251-3ab3-4987-8cdf-aa686de2b52c",
      "command_topic": "device/ce694f72-c12b-4e19-aa80-c3af37898615/turn/off",
      "default_name": "Turn off"
    }
  ]
}
```