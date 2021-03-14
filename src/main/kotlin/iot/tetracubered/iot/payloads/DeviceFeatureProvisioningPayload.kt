package iot.tetracubered.iot.payloads

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import iot.tetracubered.enumerations.FeatureType
import java.util.*

data class DeviceFeatureProvisioningPayload @JsonCreator constructor(
    @field:JsonProperty("id") val featureId: UUID,
    @field:JsonProperty("name") val name: String,
    @field:JsonProperty("feature_type") val featureType: FeatureType,
    @field:JsonProperty("value") val value: Float,
    @field:JsonProperty("actions") val actions: List<DeviceFeatureActionProvisioningPayload>,
)