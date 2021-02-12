package iot.tetracubered.iotMessaging.deviceProvisioning.payloads;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public class DeviceProvisioningPayload {

    private final UUID id;
    private final String name;
    private final String feedbackTopic;
    private final List<DeviceFeatureProvisioningPayload> features;

    @JsonCreator
    public DeviceProvisioningPayload(@JsonProperty("id") UUID id,
                                     @JsonProperty("name") String name,
                                     @JsonProperty("feedback-topic") String feedbackTopic,
                                     @JsonProperty("features") List<DeviceFeatureProvisioningPayload> features) {
        this.id = id;
        this.name = name;
        this.feedbackTopic = feedbackTopic;
        this.features = features;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFeedbackTopic() {
        return feedbackTopic;
    }

    public List<DeviceFeatureProvisioningPayload> getFeatures() {
        return features;
    }
}
