package iot.tetracubered.devices.payloads;

public class ProvisioningResponse {

    private final Boolean success;

    public ProvisioningResponse(Boolean success) {
        this.success = success;
    }

    public Boolean getSuccess() {
        return success;
    }
}
