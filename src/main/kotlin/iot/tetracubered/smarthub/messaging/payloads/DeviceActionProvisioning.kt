package iot.tetracubered.smarthub.messaging.payloads

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.validation.constraints.NotNull

class DeviceActionProvisioning @JsonCreator constructor(
    @JsonProperty("id") @NotNull val actionId: UUID,
    @JsonProperty("default_name") @NotNull val defaultName: String,
    @JsonProperty("command_topic") @NotNull val commandTopic: String?,
    @JsonProperty("querying_topic") @NotNull val queryingTopic: String?,
    @JsonProperty("value") @NotNull val value: Float
)