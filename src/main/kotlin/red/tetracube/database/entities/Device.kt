package red.tetracube.database.entities

import io.quarkus.mongodb.panache.MongoEntity
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId
import java.util.*

@MongoEntity(collection = "Device")
data class Device @BsonCreator constructor(
        @BsonId var id: ObjectId? = null,
        @BsonProperty("circuitId") var circuitId: String,
        @BsonProperty("name") var name: String,
        @BsonProperty("online") var online: Boolean,
        @BsonProperty("feedbackTopic") var feedbackTopic: String,
        @BsonProperty("colorCode") var colorCode: String,
        @BsonProperty("features") var features: List<Feature>
)