package iot.tetracube.models.dto;

import java.util.UUID;

public class ManageDeviceProvisioningResponse {

    private final UUID circuitId;
    private final Boolean success;

    public ManageDeviceProvisioningResponse(UUID circuitId, Boolean success) {
        this.circuitId = circuitId;
        this.success = success;
    }

    public UUID getCircuitId() {
        return circuitId;
    }

    public Boolean getSuccess() {
        return success;
    }

}
