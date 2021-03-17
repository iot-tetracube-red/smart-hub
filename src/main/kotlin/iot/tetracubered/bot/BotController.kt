package iot.tetracubered.bot

import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.asUni
import iot.tetracubered.bot.payloads.DeviceFeature
import iot.tetracubered.bot.payloads.DeviceFeatureAction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON

@ExperimentalCoroutinesApi
@Path("/api/bot")
class BotController(
    private val botServices: BotServices
) {

    @GET
    @Path("/features")
    @Produces(APPLICATION_JSON)
    fun getDevicesList(): Uni<List<DeviceFeature>> {
        return GlobalScope.async {
            botServices.getDevices()
        }.asUni()
    }

    @GET
    @Path("/devices/{deviceName}/features/{featureName}/commands")
    @Produces(APPLICATION_JSON)
    fun getDeviceFeatureActions(
        @PathParam("deviceName") deviceName: String,
        @PathParam("featureName") featureName: String
    ): Uni<DeviceFeatureAction> {
        return GlobalScope.async {
            val response = botServices.getDeviceFeatureActions(deviceName, featureName)
                ?: throw NotFoundException("Cannot find actions for specified device or feature name")
            response
        }.asUni()
    }
}