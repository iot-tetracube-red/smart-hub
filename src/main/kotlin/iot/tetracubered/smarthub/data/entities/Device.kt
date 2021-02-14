package iot.tetracubered.smarthub.data.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table(value = "devices")
data class Device(
    @Id
    val id: UUID,

    @Column("circuit_id")
    val circuitId: UUID,

    @Column("name")
    val name: String,

    @Column("is_online")
    val isOnline: Boolean,

    @Column("feedback_topic")
    val feedbackTopic: String,

    @Column("color_code")
    val colorCode: String? = null
)