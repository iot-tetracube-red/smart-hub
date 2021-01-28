package iot.tetracubered.resources.features;

import io.smallrye.mutiny.Multi;
import iot.tetracubered.resources.features.payloads.DeviceFeatureResponse;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/features")
public class FeaturesResource {

    @Inject
    FeatureBusinessServices featureBusinessServices;

    @GET
    @Path("/{deviceId}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Multi<DeviceFeatureResponse> getDeviceFeature(@PathParam(value = "deviceId") UUID deviceId) {
        return this.featureBusinessServices.getDeviceFeatures(deviceId);
    }
}
