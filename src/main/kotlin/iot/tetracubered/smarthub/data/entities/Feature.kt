package iot.tetracubered.smarthub.data.entities

import iot.tetracubered.smarthub.enumerations.FeatureType
import iot.tetracubered.smarthub.enumerations.SourceType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table(value = "features")
data class Feature(
    @Id
    val id: UUID,

    @Column("feature_id")
    val featureId: UUID,

    @Column("feature_type")
    val featureType: FeatureType,

    @Column("current_value")
    val currentValue: Float,

    @Column("device_id")
    val deviceId: UUID,

    @Column("is_running")
    val isRunning: Boolean,

    @Column("source_type")
    val sourceType: SourceType? = null,

    @Column("running_reference_id")
    val runningReferenceId: String? = null
)