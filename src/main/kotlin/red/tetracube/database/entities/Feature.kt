package red.tetracube.database.entities

import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty
import red.tetracube.enumerations.FeatureType
import red.tetracube.enumerations.RequestSourceType

data class Feature @BsonCreator constructor(
        @BsonProperty("featureId") val featureId: String,
        @BsonProperty("name") val name: String,
        @BsonProperty("featureType") val featureType: FeatureType,
        @BsonProperty("currentValue") val currentValue: Float,
        @BsonProperty("running") val running: Boolean,
        @BsonProperty("sourceType") val sourceType: RequestSourceType? = null,
        @BsonProperty("runningReferenceId") val runningReferenceId: String? = null,
        @BsonProperty("actions") val actions: List<Action>
)