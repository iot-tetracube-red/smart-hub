package red.tetracube.smarthub.iot.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record DeviceProvisioning(
        @JsonProperty("id") UUID id,
        @JsonProperty("name") String name,
        @JsonProperty("feedback_topic") String feedbackTopic,
        @JsonProperty("features") List<FeatureProvisioning> features
) {
}
