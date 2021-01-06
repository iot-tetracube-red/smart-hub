package iot.tetracubered.messaging.payloads

import java.util.*

class DeviceFeedbackMessage(
    val circuitId: UUID,
    val feedbackTopic: String,
    val positiveFeedback: Boolean
)
