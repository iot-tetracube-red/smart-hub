package iot.tetracubered.mqttClient.payloads;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class DeviceFeatureActionProvisioningPayload {

    private final UUID actionId;
    private final String name;
    private final String triggerTopic;

    @JsonCreator
    public DeviceFeatureActionProvisioningPayload(@JsonProperty("id") UUID actionId,
                                                  @JsonProperty("name") String name,
                                                  @JsonProperty("trigger_topic") String triggerTopic) {
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
