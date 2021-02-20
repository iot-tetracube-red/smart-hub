package iot.tetracubered.iot.consumers.payloads;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import java.util.UUID;

public class DeviceFeatureActionProvisioningPayload {

    private final UUID actionId;
    private final String name;
    private final String triggerTopic;

    @JsonbCreator
    public DeviceFeatureActionProvisioningPayload(@JsonbProperty("id") UUID actionId,
                                                  @JsonbProperty("name") String name,
                                                  @JsonbProperty("trigger_topic") String triggerTopic) {
        this.actionId = actionId;
        this.name = name;
        this.triggerTopic = triggerTopic;
    }

    public UUID getActionId() {
        return actionId;
    }

    public String getName() {
        return name;
    }

    public String getTriggerTopic() {
        return triggerTopic;
    }
}
