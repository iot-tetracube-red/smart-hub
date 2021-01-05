package iot.tetracubered.messaging.payloads

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class DeviceProvisioningPayload @JsonCreator constructor(
    @JsonProperty(value = "id") val circuitId: UUID,
    @JsonProperty(value = "default_name") val name: String,
    @JsonProperty(value = "feedback_topic") val feedbackTopic: String,
    @JsonProperty(value = "querying_topic", required = false) val queryingTopic: String,
    @JsonProperty(value = "actions") val actions: List<ActionProvisioningPayload>
)