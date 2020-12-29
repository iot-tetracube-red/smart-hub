package iot.tetracubered.devices;

import io.smallrye.mutiny.Uni;
import iot.tetracubered.devices.payloads.DeviceProvisioningRequest;
import iot.tetracubered.devices.payloads.ProvisioningResponse;

import javax.inject.Inject;
import javax.validation.Validator;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path(value = "/devices")
public class DevicesResources {

    @Inject
    Validator validator;

    @POST
    @Path(value = "/provisioning")
    public Uni<ProvisioningResponse> deviceProvisioning(DeviceProvisioningRequest deviceProvisioningRequest) {
        var validationErrors = this.validator.validate(deviceProvisioningRequest);
        if (!validationErrors.isEmpty()) {
            throw new BadRequestException();
        }
    }
}
