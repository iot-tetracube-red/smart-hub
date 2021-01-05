package iot.tetracubered.messaging.payloads

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class ActionProvisioningPayload @JsonCreator constructor(
    @JsonProperty("id") val actionId: UUID,
    @JsonProperty("default_name") val name: String,
    @JsonProperty("command_topic") val commandTopic: String?,
    @JsonProperty("querying_topic") val queryingTopic: String?
)
