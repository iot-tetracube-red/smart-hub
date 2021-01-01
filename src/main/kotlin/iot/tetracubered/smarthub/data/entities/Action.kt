package iot.tetracubered.smarthub.data.entities

import org.springframework.data.annotation.Id
import java.util.*

data class Action(
    @Id
    val actionId: UUID,
    val currentValue: Float,
    val commandTopic: String?,
    val queryingTopic: String?,
    val alexaIntent: String?
)