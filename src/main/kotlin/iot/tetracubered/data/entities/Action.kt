package iot.tetracubered.data.entities

import io.vertx.mutiny.sqlclient.Row
import java.util.*

data class Action(
    val id: UUID,
    val actionId: UUID,
    val name: String,
    val commandTopic: String,
    val alexaIntent: String?,
    val deviceId: UUID
) {

    constructor(row: Row) : this(
        id = row.getUUID("id"),
        actionId = row.getUUID("action_id"),
        name = row.getString("name"),
        commandTopic = row.getString("command_topic"),
        alexaIntent = row.getString("alexa_intent"),
        deviceId = row.getUUID("device_id")
    )
}