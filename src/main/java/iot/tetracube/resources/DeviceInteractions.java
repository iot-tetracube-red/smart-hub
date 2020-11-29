package iot.tetracube.resources;

import io.smallrye.mutiny.Uni;
import iot.tetracube.data.entities.Action;
import iot.tetracube.services.CallDeviceActionService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

@Path("/devices/interactions")
public class DeviceInteractions {

    private final CallDeviceActionService callDeviceActionService;

    public DeviceInteractions(CallDeviceActionService callDeviceActionService) {
        this.callDeviceActionService = callDeviceActionService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/call")
    public Uni<Action> callDeviceAction(@QueryParam("actionName") Optional<String> actionName) {
        if (actionName.isEmpty()) {
            throw new NotFoundException("Action not found");
        }
        return this.callDeviceActionService.callDeviceAction(actionName.get())
                .onFailure()
                .transform(err -> new NotFoundException("Action not found"));
    }

}
