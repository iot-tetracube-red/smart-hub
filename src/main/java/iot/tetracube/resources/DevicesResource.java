package iot.tetracube.resources;

import io.smallrye.mutiny.Multi;
import iot.tetracube.models.payloads.DeviceResponse;
import iot.tetracube.services.DeviceServices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/devices")
public class DevicesResource {

    private final DeviceServices deviceServices;

    public DevicesResource(DeviceServices deviceServices) {
        this.deviceServices = deviceServices;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    public Multi<DeviceResponse> getDevices() {
        return this.deviceServices.getDevices();
    }

}
