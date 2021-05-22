package red.tetracube.smarthub.data.entities

import java.util.*

data class Device(
    val id: UUID,
    val name: String,
    val isOnline: Boolean,
    val feedbackTopic: String,
    val colorCode: String,
    val features: List<Feature> = emptyList()
)