package iot.tetracubered.messaging.payloads

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class DeviceProvisioningPayload @JsonCreator constructor(
    @JsonProperty("id") val circuitId: UUID,
    @JsonProperty("default_name") val name: String,
    @JsonProperty("feedback_topic") val feedbackTopic: String,
    @JsonProperty("actions") val actions: List<ActionProvisioningPayload>
)