package iot.tetracubered.resources.device.payloads

import java.util.*

data class GetDevicesResponse(
    val id: UUID,
    val name: String,
    val isOnline: Boolean,
    val actions: List<GetDeviceActionResponse>
)
