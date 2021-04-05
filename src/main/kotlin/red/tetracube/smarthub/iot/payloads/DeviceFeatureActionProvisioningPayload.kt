package iot.tetracubered.iot.payloads

import java.util.*
import javax.json.bind.annotation.JsonbCreator
import javax.json.bind.annotation.JsonbProperty

data class DeviceFeatureActionProvisioningPayload @JsonbCreator constructor(
    @field:JsonbProperty("id") val actionId : UUID,
    @field:JsonbProperty("name") val name: String,
    @field:JsonbProperty("trigger_topic") val triggerTopic: String
)