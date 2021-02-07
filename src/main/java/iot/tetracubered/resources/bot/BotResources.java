package iot.tetracubered.resources.bot;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import iot.tetracubered.resources.bot.payloads.GetCommandsResponse;
import iot.tetracubered.resources.bot.payloads.GetFeaturesResponse;
import iot.tetracubered.resources.bot.payloads.RunDeviceFeatureCommandRequest;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.validation.Validator;
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

    @Operation(summary = "Get commands of the feature's device")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "The list of the commands"),
            @APIResponse(responseCode = "404", description = "No command was found for given device name and feature name")
    })
    @GET
    @Path("/devices/{deviceName}/features/{featureName}/commands")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Uni<GetCommandsResponse> getCommands(@PathParam("deviceName") String deviceName,
                                                @PathParam("featureName") String featureName) {
        return this.botBusinessServices.getCommandsByDeviceAndFeature(deviceName, featureName)
                .invoke(response -> {
                    if (response == null) {
                        throw new NotFoundException(
                                """
                                        Cannot find any command for combination device: {deviceName}
                                        and feature: {featureName}
                                        """
                                        .replace("{deviceName}", deviceName)
                                        .replace("{featureName}", featureName)
                        );
                    }
                });
    }

    @Operation(summary = "Run device command")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "The command run correctly"),
            @APIResponse(responseCode = "400", description = "Invalid parameters"),
            @APIResponse(responseCode = "404", description = "No command found"),
            @APIResponse(responseCode = "503", description = "Cannot reach the device to run the command"),
    })
    @PATCH
    @Path("/devices/features/command")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Uni<Void> runDeviceFeatureCommand(RunDeviceFeatureCommandRequest runDeviceFeatureCommandRequest) {
        var violations = this.validator.validate(runDeviceFeatureCommandRequest);
        if (!violations.isEmpty()) {
            throw new BadRequestException("Invalid input");
        }
        return this.botBusinessServices.runDeviceFeatureAction(
                runDeviceFeatureCommandRequest.getDeviceName(),
                runDeviceFeatureCommandRequest.getFeatureName(),
                runDeviceFeatureCommandRequest.getCommandName()
        )
                .map(response -> {
                    if (response == null) {
                        throw new NotFoundException(
                                """
                                        Cannot find any command for combination device: {deviceName}
                                        and feature: {featureName} and command: {commandName}
                                        """
                                        .replace("{deviceName}", runDeviceFeatureCommandRequest.getDeviceName())
                                        .replace("{featureName}", runDeviceFeatureCommandRequest.getFeatureName())
                                        .replace("{commandName}", runDeviceFeatureCommandRequest.getCommandName())
                        );
                    } else if (!response) {
                        throw new ServiceUnavailableException("Cannot reach the device at the moment");
                    }

                    return null;
                });
    }
}
