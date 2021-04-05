package iot.tetracubered.iot.payloads

import java.util.*
import javax.json.bind.annotation.JsonbCreator
import javax.json.bind.annotation.JsonbProperty

data class DeviceProvisioningPayload @JsonbCreator constructor(
    @field:JsonbProperty("id") val id: UUID,
    @field:JsonbProperty("name") val name: String,
    @field:JsonbProperty("feedback_topic") val feedbackTopic: String,
    @field:JsonbProperty("features") val features: List<DeviceFeatureProvisioningPayload>
)