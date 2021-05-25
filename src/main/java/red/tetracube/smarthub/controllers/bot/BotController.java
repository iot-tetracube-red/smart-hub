package red.tetracube.smarthub.controllers.bot;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import red.tetracube.smarthub.controllers.bot.payloads.BotDeviceFeatureItem;
import red.tetracube.smarthub.controllers.bot.payloads.DeviceFeatureAction;
import red.tetracube.smarthub.controllers.bot.payloads.TriggerFeatureActionRequest;
import red.tetracube.smarthub.enumerations.TriggerActionResult;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@OpenAPIDefinition(
        info = @Info(
                title = "Bot APIs",
                description = "APIs to serve data to bots middleware",
                version = "${quarkus.application.version}"
        )
)
@Tag(name = "Bot", description = "APIs to serve data to bots middleware")
@Path("/bot")
public class BotController {

    @Inject
    BotService botService;

    @Operation(summary = "Get features")
    @APIResponses(value = {
            @APIResponse(description = "The list of available features with related devices", name = "Found actions", responseCode = "200")
    })
    @GET
    @Path("/features")
    @Produces(APPLICATION_JSON)
    public Uni<List<BotDeviceFeatureItem>> getDevices() {
        return botService.getDeviceFeatures();
    }

    @Operation(summary = "Get actions related to certain feature")
    @APIResponses(value = {
            @APIResponse(description = "The list of available actions", name = "Found actions", responseCode = "200"),
            @APIResponse(description = "Device of feature not found", name = "Resource not found", responseCode = "404")
    })
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

    @Operation(summary = "Trigger a feature action")
    @APIResponses(value = {
            @APIResponse(description = "Action triggered correctly", name = "Action triggered", responseCode = "204"),
            @APIResponse(description = "Device, feature or action not found", name = "Resource not found", responseCode = "404"),
            @APIResponse(description = "The device is offline", name = "Device unavailable", responseCode = "503")
    })
    @PUT
    @Path("/devices/features/command")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Uni<Void> triggerFeatureAction(@RequestBody TriggerFeatureActionRequest triggerFeatureActionRequest) {
        return botService.triggerFeatureAction(triggerFeatureActionRequest)
                .invoke(result -> {
                    if (result.equals(TriggerActionResult.ACTION_NOT_FOUND)) {
                        throw new NotFoundException("Cannot find actions for specified device or feature name");
                    } else if (result.equals(TriggerActionResult.DEVICE_OFFLINE)) {
                        throw new ServiceUnavailableException("The device is offline");
                    }
                })
                .map(ignored -> null);
    }
}
