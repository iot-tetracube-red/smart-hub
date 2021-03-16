package iot.tetracubered.bot.payloads

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class DeviceFeature @JsonCreator constructor(
    @field:JsonProperty() val deviceName: String,
    @field:JsonProperty() val featureName: String
)
