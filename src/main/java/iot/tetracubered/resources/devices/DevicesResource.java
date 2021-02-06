package iot.tetracubered.resources.devices;

import io.smallrye.mutiny.Multi;
import iot.tetracubered.resources.devices.payloads.DeviceResponse;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api/devices")
public class DevicesResource {

    @Inject
    DeviceBusinessServices deviceBusinessServices;

    @GET
    @Path("")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Multi<DeviceResponse> getDevices() {
        return this.deviceBusinessServices.getDevices();
    }
}
