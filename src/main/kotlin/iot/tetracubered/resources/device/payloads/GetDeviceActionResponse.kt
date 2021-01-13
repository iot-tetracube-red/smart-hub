package iot.tetracubered.resources.device.payloads

import java.util.*

data class GetDeviceActionResponse(
    var id: UUID,
    var name: String
)
