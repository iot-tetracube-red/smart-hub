package iot.tetracubered.messaging.payloads

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class ActionProvisioningPayload @JsonCreator constructor(
    @JsonProperty(value = "id") val actionId: UUID,
    @JsonProperty(value = "default_name") val name: String,
    @JsonProperty(value = "command_topic", required = false) val commandTopic: String
)
