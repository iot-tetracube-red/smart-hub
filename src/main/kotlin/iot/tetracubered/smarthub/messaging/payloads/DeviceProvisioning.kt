package iot.tetracubered.smarthub.messaging.payloads

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class DeviceProvisioning @JsonCreator constructor(
    @JsonProperty("id") @field:NotNull val circuitId: UUID,
    @JsonProperty("default_name") @field:NotNull val name: String,
    @JsonProperty("feedback_topic") @field:NotNull val feedbackTopic: String,
    @JsonProperty("actions") @field:NotNull @field:NotEmpty val actions: List<DeviceActionProvisioning>
)