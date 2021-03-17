package iot.tetracubered.bot

import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.asUni
import iot.tetracubered.bot.payloads.DeviceFeature
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType.APPLICATION_JSON

@Path("/api/bot")
class BotController(
    private val botServices: BotServices
) {

    @ExperimentalCoroutinesApi
    @GET
    @Path("/features")
    @Produces(APPLICATION_JSON)
    fun getDevicesList(): Uni<List<DeviceFeature>> {
        return GlobalScope.async {
            botServices.getDevices()
        }.asUni()
    }
}