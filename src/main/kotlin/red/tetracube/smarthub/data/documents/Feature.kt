package red.tetracube.smarthub.data.documents

import red.tetracube.smarthub.enumerations.FeatureType
import red.tetracube.smarthub.enumerations.RequestSourceType
import java.util.*

data class Feature(
    val id: UUID,
    val name: String,
    val featureType: FeatureType,
    val currentValue: Float,
    val isRunning: Boolean,
    val sourceType: RequestSourceType? = null,
    val runningReferenceId: String? = null,
    val deviceId: UUID,
    val device: Device? = null,
    val actions: List<Action> = emptyList()
)
