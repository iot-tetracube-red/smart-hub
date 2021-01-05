package iot.tetracubered.data.entities

import io.vertx.mutiny.sqlclient.Row
import java.util.*

data class Device(
    val id: UUID,
    val circuitId: UUID,
    val name: String,
    val isOnline: Boolean,
    val feedbackTopic: String,
    val queryingTopic: String,
    val status: String?,
    val alexaSlotId: String?,
    val actions: List<Action>
) {

    constructor(row: Row): this(
        id = row.getUUID("id"),
        circuitId = row.getUUID("circuit_id"),
        name = row.getString("name"),
        isOnline= row.getBoolean("is_online"),
        feedbackTopic = row.getString("feedback_topic"),
        queryingTopic = row.getString("querying_topic"),
        status = row.getString("status"),
        alexaSlotId = row.getString("alexa_slot_id"),
        actions = emptyList()
    )
}