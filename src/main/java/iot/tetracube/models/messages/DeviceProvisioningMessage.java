package iot.tetracube.models.messages;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import java.util.List;
import java.util.UUID;

public class DeviceProvisioningMessage {

    private UUID circuitId;
    private String defaultName;
    private List<DeviceActionProvisioningMessage> deviceActionProvisioningMessages;

    @JsonbCreator
    public DeviceProvisioningMessage(@JsonbProperty("circuit_id") UUID circuitId,
                                     @JsonbProperty("default_name") String defaultName,
                                     @JsonbProperty("actions") List<DeviceActionProvisioningMessage> deviceActionProvisioningMessages) {
        this.circuitId = circuitId;
        this.defaultName = defaultName;
        this.deviceActionProvisioningMessages = deviceActionProvisioningMessages;
    }

    public UUID getCircuitId() {
        return circuitId;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public List<DeviceActionProvisioningMessage> getDeviceActionProvisioningMessages() {
        return deviceActionProvisioningMessages;
    }

}
