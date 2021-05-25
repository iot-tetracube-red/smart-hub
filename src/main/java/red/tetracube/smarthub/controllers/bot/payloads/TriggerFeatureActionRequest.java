package red.tetracube.smarthub.controllers.bot.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import red.tetracube.smarthub.enumerations.RequestSourceType;

public record TriggerFeatureActionRequest(
        @JsonProperty("deviceName") String deviceName,
        @JsonProperty("featureName") String featureName,
        @JsonProperty("commandName") String commandName,
        @JsonProperty("referenceId") String referenceId,
        @JsonProperty("source") RequestSourceType source
) {
}
