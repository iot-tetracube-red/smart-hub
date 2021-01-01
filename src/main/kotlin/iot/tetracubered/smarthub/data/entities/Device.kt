package iot.tetracubered.smarthub.data.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class Device (
    @Id
    val circuitId: UUID,
    val name: String,
    val isOnline: Boolean,
    val feedbackTopic: String?,
    val alexaSlotId: String?,
    val actions: List<Action>
)