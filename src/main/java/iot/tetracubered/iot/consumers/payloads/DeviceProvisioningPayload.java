package iot.tetracubered.iot.consumers.payloads;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import java.util.List;
import java.util.UUID;

public class DeviceProvisioningPayload {

    private final UUID id;
    private final String name;
    private final String feedbackTopic;
    private final List<DeviceFeatureProvisioningPayload> features;

    @JsonbCreator
    public DeviceProvisioningPayload(@JsonbProperty("id") UUID id,
                                     @JsonbProperty("name") String name,
                                     @JsonbProperty("feedback-topic") String feedbackTopic,
                                     @JsonbProperty("features") List<DeviceFeatureProvisioningPayload> features) {
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
