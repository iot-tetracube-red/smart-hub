package iot.tetracubered.bot

import io.smallrye.mutiny.Multi
import iot.tetracubered.bot.payloads.DeviceFeature
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType.APPLICATION_JSON

@Path("/api/bot")
class BotController(
    private val botServices: BotServices
) {

    @GET
    @Path("/features")
    @Produces(APPLICATION_JSON)
    fun getDevicesList(): Multi<DeviceFeature> {
        return this.botServices.getDevices()
    }
}