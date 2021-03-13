package iot.tetracubered.data.entities

import io.vertx.mutiny.sqlclient.Row
import java.util.*

data class Action(
    val id: UUID,
    val triggerTopic: String,
    val name: String,
    val featureId: UUID,
    val feature: Feature? = null
) {

    constructor(row: Row) : this(
        id = row.getUUID("id"),
        triggerTopic = row.getString("trigger_topic"),
        name = row.getString("name"),
        featureId = row.getUUID("feature_id")
    )
}