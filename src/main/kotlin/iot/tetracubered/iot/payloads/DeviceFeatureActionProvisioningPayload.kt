package iot.tetracubered.iot.payloads

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class DeviceFeatureActionProvisioningPayload @JsonCreator constructor(
    @field:JsonProperty("id") val actionId : UUID,
    @field:JsonProperty("name") val name: String,
    @field:JsonProperty("trigger_topic") val triggerTopic: String
)