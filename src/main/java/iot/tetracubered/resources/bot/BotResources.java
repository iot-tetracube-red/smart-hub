package iot.tetracubered.resources.bot;

import io.smallrye.mutiny.Multi;
import iot.tetracubered.resources.bot.payloads.GetFeaturesResponse;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    Validator validator;

    @Inject
    BotBusinessServices botBusinessServices;

    @Operation(summary = "Get features of each device")
    @GET
    @Path("/features")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Multi<GetFeaturesResponse> getFeatures() {
        return this.botBusinessServices.getFeatures();
    }
}
