package red.tetracube.database.entities

import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty
import java.util.*

data class Action @BsonCreator constructor(
        @BsonProperty("actionId") val actionId: UUID,
        @BsonProperty("triggerTopic") val triggerTopic: String,
        @BsonProperty("name") val name: String
)