package iot.tetracubered.bot.payloads

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class DeviceFeatureAction @JsonCreator constructor(
    @field:JsonProperty("deviceName") val deviceName: String,
    @field:JsonProperty("featureName") val featureName: String,
    @field:JsonProperty("commands") val commands: List<String>
)
