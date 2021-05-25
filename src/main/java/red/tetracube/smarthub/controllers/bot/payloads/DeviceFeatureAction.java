package red.tetracube.smarthub.controllers.bot.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record DeviceFeatureAction(
        @JsonProperty("deviceName") String deviceName,
        @JsonProperty("featureName") String featureName,
        @JsonProperty("commands") List<String> commands
) {
}
