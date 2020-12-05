package iot.tetracube.messagingInterface.deviceProvisioning.payloads;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import java.util.UUID;

public class DeviceActionProvisioningMessage {

    private UUID id;
    private String defaultName;
    private Boolean isDefault;
    private String topic;

    @JsonbCreator
    public DeviceActionProvisioningMessage(@JsonbProperty("id") UUID id,
                                           @JsonbProperty("default_name") String defaultName,
                                           @JsonbProperty("is_default") Boolean isDefault,
                                           @JsonbProperty("topic") String topic) {
        this.id = id;
        this.defaultName = defaultName;
        this.isDefault = isDefault;
        this.topic = topic;
    }

    public UUID getId() {
        return id;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public String getTopic() {
        return topic;
    }
}
