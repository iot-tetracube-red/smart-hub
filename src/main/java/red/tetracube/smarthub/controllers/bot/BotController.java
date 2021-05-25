package red.tetracube.smarthub.controllers.bot;

import io.smallrye.mutiny.Uni;
import red.tetracube.smarthub.controllers.bot.payloads.BotDeviceFeatureItem;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/api/bot")
public class BotController {

    @Inject
    BotService botService;

    @GET
    @Path("/devices")
    @Produces(APPLICATION_JSON)
    public Uni<List<BotDeviceFeatureItem>> getDevices() {
        return botService.getDeviceFeatures();
    }
}
