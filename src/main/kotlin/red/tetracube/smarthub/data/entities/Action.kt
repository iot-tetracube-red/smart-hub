package red.tetracube.smarthub.data.entities

import java.util.*

data class Action(
    val id: UUID,
    val triggerTopic: String,
    val name: String,
    val featureId: UUID,
    val feature: Feature?
)