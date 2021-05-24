package red.tetracube.smarthub.iot.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record ActionProvisioning(
        @JsonProperty("id") UUID id,
        @JsonProperty("name") String name,
        @JsonProperty("trigger_topic") String triggerTopic
) {
}
