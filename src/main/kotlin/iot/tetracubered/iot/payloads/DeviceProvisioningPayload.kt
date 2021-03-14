package iot.tetracubered.iot.payloads

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class DeviceProvisioningPayload @JsonCreator constructor(
    @field:JsonProperty("id") val id: UUID,
    @field:JsonProperty("name") val name: String,
    @field:JsonProperty("feedback_topic") val feedbackTopic: String,
    @field:JsonProperty("features") val features: List<DeviceFeatureProvisioningPayload>
)