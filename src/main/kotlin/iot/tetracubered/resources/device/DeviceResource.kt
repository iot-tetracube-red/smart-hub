package iot.tetracubered.resources.device

import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import iot.tetracubered.resources.device.payloads.GetDevicesResponse
import java.util.*
import javax.ws.rs.GET
import javax.ws.rs.Path

@Path("/devices")
class DeviceResource {

    @GET
    fun getDevices(): Uni<List<GetDevicesResponse>> {
        return Uni.createFrom().item(
            listOf(
                GetDevicesResponse(
                    UUID.randomUUID(),
                    "Device 1",
                    true,
                    emptyList()
                ),
                GetDevicesResponse(
                    UUID.randomUUID(),
                    "Device 2",
                    true,
                    emptyList()
                ),
                GetDevicesResponse(
                    UUID.randomUUID(),
                    "Device 3",
                    true,
                    emptyList()
                )
            )
        )
    }
}