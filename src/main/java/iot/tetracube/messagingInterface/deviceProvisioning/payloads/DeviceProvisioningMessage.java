package iot.tetracube.messagingInterface.deviceProvisioning.payloads;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import java.util.List;
import java.util.UUID;

public class DeviceProvisioningMessage {

    private UUID id;
    private String defaultName;
    private String hostname;
    private List<DeviceActionProvisioningMessage> deviceActionProvisioningMessages;

    @JsonbCreator
    public DeviceProvisioningMessage(@JsonbProperty("id") UUID id,
                                     @JsonbProperty("default_name") String defaultName,
                                     @JsonbProperty("hostname") String hostname,
                                     @JsonbProperty("actions") List<DeviceActionProvisioningMessage> deviceActionProvisioningMessages) {
        this.id = id;
        this.defaultName = defaultName;
        this.hostname = hostname;
        this.deviceActionProvisioningMessages = deviceActionProvisioningMessages;
    }

    public UUID getId() {
        return id;
    }

    public String getHostname() {
        return hostname;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public List<DeviceActionProvisioningMessage> getDeviceActionProvisioningMessages() {
        return deviceActionProvisioningMessages;
    }
}

