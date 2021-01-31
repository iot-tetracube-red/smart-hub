package iot.tetracubered.resources.command;

import io.smallrye.mutiny.Uni;
import iot.tetracubered.resources.command.payloads.PlayActionResponse;
import iot.tetracubered.resources.command.services.SendDeviceCommandService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/device/{deviceName}")
public class DeviceCommand {

    @PathParam("deviceName")
    private String deviceName;

    @Inject
    SendDeviceCommandService sendDeviceCommandService;

    @PUT
    @Path("/play-action/{featureName}/{actionName}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Uni<PlayActionResponse> playAction(@PathParam("featureName") String featureName,
                                              @PathParam("actionName") String actionName) {
        return this.sendDeviceCommandService.runDeviceFeatureAction(
                this.deviceName,
                featureName,
                actionName
        )
                .map(PlayActionResponse::new);
    }
}
