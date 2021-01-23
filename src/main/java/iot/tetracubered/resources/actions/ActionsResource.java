package iot.tetracubered.resources.actions;

import io.smallrye.mutiny.Multi;
import iot.tetracubered.resources.actions.payloads.DeviceActionResponse;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/actions")
public class ActionsResource {

    @Inject
    ActionBusinessServices actionBusinessServices;

    @GET
    @Path("/{deviceId}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Multi<DeviceActionResponse> getDeviceActions(@PathParam(value = "deviceId") UUID deviceId) {
        return this.actionBusinessServices.getDeviceActions(deviceId);
    }
}
