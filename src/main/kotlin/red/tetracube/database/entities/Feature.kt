package red.tetracube.database.entities

import org.bson.codecs.pojo.annotations.BsonProperty
import red.tetracube.enumerations.FeatureType
import red.tetracube.enumerations.RequestSourceType
import java.util.*

data class Feature(
        @BsonProperty("feature_id") val id: UUID,
        @BsonProperty("name") val name: String,
        @BsonProperty("feature_type") val featureType: FeatureType,
        @BsonProperty("current_value") val currentValue: Float,
        @BsonProperty("is_running") val isRunning: Boolean,
        @BsonProperty("source_type") val sourceType: RequestSourceType? = null,
        @BsonProperty("running_reference_id") val runningReferenceId: String? = null,
        @BsonProperty("actions") val actions: List<Action>
)