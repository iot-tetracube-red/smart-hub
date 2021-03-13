package iot.tetracubered.data.entities

import io.vertx.mutiny.sqlclient.Row
import iot.tetracubered.enumerations.FeatureType
import iot.tetracubered.enumerations.RequestSourceType
import java.util.*

data class Feature(
    val id: UUID,
    val name: String,
    val featureType: FeatureType,
    val currentValue: Float,
    val isRunning: Boolean,
    val sourceType: RequestSourceType? = null,
    val runningReferenceId: String? = null,
    val deviceId: UUID,
    val device: Device? = null,
    val actions: List<Action>? = null
) {

    constructor(row: Row) : this(
        id = row.getUUID("id"),
        name = row.getString("name"),
        featureType = FeatureType.valueOf(row.getString("feature_type")),
        currentValue = row.getFloat("current_value"),
        isRunning = row.getBoolean("is_running"),
        runningReferenceId = row.getString("running_reference_id"),
        sourceType = if (row.getString("source_type") == null) null else RequestSourceType.valueOf(row.getString("source_type")),
        deviceId = row.getUUID("device_id")
    )
}