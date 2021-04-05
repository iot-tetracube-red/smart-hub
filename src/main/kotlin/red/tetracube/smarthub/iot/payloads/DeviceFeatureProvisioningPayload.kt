package iot.tetracubered.iot.payloads

import red.tetracube.smarthub.enumerations.FeatureType
import java.util.*
import javax.json.bind.annotation.JsonbCreator
import javax.json.bind.annotation.JsonbProperty

data class DeviceFeatureProvisioningPayload @JsonbCreator constructor(
    @field:JsonbProperty("id") val featureId: UUID,
    @field:JsonbProperty("name") val name: String,
    @field:JsonbProperty("feature_type") val featureType: FeatureType,
    @field:JsonbProperty("value") val value: Float,
    @field:JsonbProperty("actions") val actions: List<DeviceFeatureActionProvisioningPayload>,
)