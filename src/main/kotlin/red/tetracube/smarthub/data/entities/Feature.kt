package red.tetracube.smarthub.data.entities;

import red.tetracube.smarthub.enumerations.FeatureType
import red.tetracube.smarthub.enumerations.RequestSourceType
import java.util.*

data class Feature(
    val id: UUID,
    val name: String,
    val featureType: FeatureType,
    val isRunning:Boolean,
    val sourceType: RequestSourceType?,
    val runningReferenceId: String?,
    val deviceId: UUID,
    val device: Device?,
    val actions: List<Action> = emptyList()
)