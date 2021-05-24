package red.tetracube.smarthub.iot.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import red.tetracube.smarthub.enumerations.FeatureType;

import java.util.List;
import java.util.UUID;

public record FeatureProvisioning(
        @JsonProperty("id") UUID featureId,
        @JsonProperty("name") String name,
        @JsonProperty("feature_type") FeatureType featureType,
        @JsonProperty("value") float value,
        @JsonProperty("actions") List<ActionProvisioning> actions
) {
}
