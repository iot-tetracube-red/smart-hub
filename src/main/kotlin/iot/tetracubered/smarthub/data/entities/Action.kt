package iot.tetracubered.smarthub.data.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("actions")
data class Action(
    @Id
    val id: UUID,

    @Column("action_id")
    val actionId: UUID,

    @Column("trigger_topic")
    val triggerTopic: String,

    @Column("name")
    val name : String,

    @Column("feature_id")
    val featureId: UUID
)