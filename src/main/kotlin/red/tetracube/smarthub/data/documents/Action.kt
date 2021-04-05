package red.tetracube.smarthub.data.documents;

import java.util.*

data class Action(
    val id: UUID,
    val triggerTopic: String,
    val name: String,
    val feature: Feature? = null
)
