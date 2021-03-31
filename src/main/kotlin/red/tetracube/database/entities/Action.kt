package red.tetracube.database.entities

import org.bson.codecs.pojo.annotations.BsonProperty
import java.util.*

data class Action(
        @BsonProperty("action_id") val actionId: UUID,
        @BsonProperty("trigger_topic") val triggerTopic: String,
        @BsonProperty("name") val name: String
)