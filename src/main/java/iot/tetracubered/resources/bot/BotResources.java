package iot.tetracubered.resources.bot;

import io.smallrye.mutiny.Multi;
import iot.tetracubered.resources.bot.payloads.GetFeaturesResponse;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@OpenAPIDefinition(
        info = @Info(
                title = "Bot APIs",
                description = "APIs to serve data to bots middleware",
                version = "1.0.0"
        )
)
@Tag(name = "Bot", description = "APIs to serve data to bots middleware")
@Path("/bot")
public class BotResources {

    @Inject
    BotBusinessServices botBusinessServices;

    @Operation(summary = "Get features")
    @GET
    @Path("/features")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Multi<GetFeaturesResponse> getFeatures() {
        return this.botBusinessServices.getFeatures();
    }

    @GET
    @Path("/devices/{deviceName}/features/{featureName}/commands")
    @Produces(value = MediaType.APPLICATION_JSON)
    public void getCommands(@PathParam("deviceName") String commandName,
                            @PathParam("featureName") String featureName) {

    }

    @PATCH
    @Path("/devices/features/command")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public void sendCommand() {

    }
}
