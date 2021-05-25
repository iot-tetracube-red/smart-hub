package red.tetracube.smarthub.controllers.bot;

import io.smallrye.mutiny.Uni;
import red.tetracube.smarthub.controllers.bot.payloads.BotDeviceFeatureItem;
import red.tetracube.smarthub.controllers.bot.payloads.DeviceFeatureAction;

import javax.inject.Inject;
import javax.ws.rs.*;

import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/bot")
public class BotController {

    @Inject
    BotService botService;

    @GET
    @Path("/features")
    @Produces(APPLICATION_JSON)
    public Uni<List<BotDeviceFeatureItem>> getDevices() {
        return botService.getDeviceFeatures();
    }

    @GET
    @Path("/devices/{deviceName}/features/{featureName}/commands")
    @Produces(APPLICATION_JSON)
    public Uni<DeviceFeatureAction> getDeviceFeatureActions(@PathParam("deviceName") String deviceName,
                                                            @PathParam("featureName") String featureName) {
        return botService.getDeviceFeatureActions(deviceName, featureName)
                .map(optionalResponse -> {
                    if (optionalResponse.isEmpty()) {
                        throw new NotFoundException("Cannot find actions for specified device or feature name");
                    }
                    return optionalResponse.get();
                });
    }
}
