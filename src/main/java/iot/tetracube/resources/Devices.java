package iot.tetracube.resources;

import io.smallrye.mutiny.Uni;
import iot.tetracube.data.entities.Device;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/devices")
public class Devices {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<Device>> getDevices() {
        return Device.findAll().list();
    }
}
