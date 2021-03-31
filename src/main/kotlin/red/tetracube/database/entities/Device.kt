package red.tetracube.database.entities

import io.quarkus.mongodb.panache.MongoEntity
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId
import java.util.*

@MongoEntity(collection = "Device")
data class Device @BsonCreator constructor(
        @BsonId var id: ObjectId,
        @BsonProperty("circuit_id") var circuitId: UUID,
        @BsonProperty("name") var name: String,
        @BsonProperty("is_online") var isOnline: Boolean,
        @BsonProperty("feedback_topic") var feedbackTopic: String,
        @BsonProperty("color_code") var colorCode: String,
        @BsonProperty("features") var features: List<Feature>
)