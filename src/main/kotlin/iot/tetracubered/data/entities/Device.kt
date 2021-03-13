package iot.tetracubered.data.entities

import io.vertx.mutiny.sqlclient.Row
import java.util.*

data class Device(
    val id: UUID,
    val name: String,
    val isOnline: Boolean,
    val feedbackTopic: String,
    val colorCode: String,
    val feature: Feature? = null,
) {

    constructor(row: Row) : this(
        id = row.getUUID("id"),
        name = row.getString("name"),
        isOnline = row.getBoolean("is_online"),
        feedbackTopic = row.getString("feedback_topic"),
        colorCode = row.getString("color_code")
    )

    constructor(
        id: UUID,
        name: String,
        feedbackTopic: String,
    ) : this(
        id = id,
        name = name,
        isOnline = true,
        feedbackTopic = feedbackTopic,
        colorCode = String.format("#%06x",  Random().nextInt(0xffffff + 1))
    )
}