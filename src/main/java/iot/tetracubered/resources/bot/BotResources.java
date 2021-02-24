package iot.tetracubered.resources.bot;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import io.smallrye.mutiny.Uni;
import iot.tetracubered.resources.bot.payloads.GetCommandsResponse;
import iot.tetracubered.resources.bot.payloads.GetDeviceFeatureResponse;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import io.smallrye.mutiny.Multi;

@OpenAPIDefinition(info = @Info(title = "Bot APIs",
        description = "APIs to serve data to bots middleware", version = "1.0.0"))
@Tag(name = "Bot", description = "APIs to serve data to bots middleware")
//@Path("/bot")
public class BotResources {
/*
    @Inject
    BotBusinessServices botBusinessServices;

    @Operation(summary = "Get features of each device")
    @GET
    @Path("/features")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Multi<GetDeviceFeatureResponse> getFeatures() {
        return this.botBusinessServices.getUserDevicesAndFeatures();
    }

    @Operation(summary = "Get commands of the feature's device")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "The list of the commands"),
            @APIResponse(responseCode = "404",
                    description = "No command was found for given device name and feature name")})
    @GET
    @Path("/devices/{deviceName}/features/{featureName}/commands")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Uni<GetCommandsResponse> getCommands(@PathParam("deviceName") String deviceName,
                                                @PathParam("featureName") String featureName) {
        return this.botBusinessServices.getCommandsForFeature(deviceName, featureName)
                .invoke(response -> {
                    if (response == null) {
                        throw new NotFoundException("""
                                Cannot find any command for combination device: {deviceName}
                                and feature: {featureName}
                                """.replace("{deviceName}", deviceName)
                                .replace("{featureName}", featureName)
                        );
                    }
                });
    }*/
}
